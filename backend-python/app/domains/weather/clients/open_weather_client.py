from ....core.http_client import BaseAPIClient
from httpx import AsyncClient
from fastapi import Depends
from app.core.deps import get_http_client
from app.core.config import settings
from ..schema import CityInfo, WeatherInfo, AirQuality, WeatherSnapshot, WeatherSnapshotList

import logging
from datetime import datetime, timezone, timedelta

logger = logging.getLogger(__name__)

class OpenWeatherAdapter:
    def _convert_to_us_aqi(self, components: dict) -> int:
        
        def _calculate_piecewise(conc, breakpoints):
            for low_c, high_c, low_i, high_i in breakpoints:
                if low_c <= conc <= high_c:
                    return round(((high_i - low_i) / (high_c - low_c)) * (conc - low_c) + low_i)
            return 500  # 超過上限則回傳最大值

        # 1. 定義各污染物的斷點 (Breakpoints)
        # PM2.5 (µg/m³, 24hr avg)
        pm25_bp = [
            (0.0, 12.0, 0, 50), (12.1, 35.4, 51, 100), (35.5, 55.4, 101, 150),
            (55.5, 150.4, 151, 200), (150.5, 250.4, 201, 300), (250.5, 500.4, 301, 500)
        ]
        
        # PM10 (µg/m³, 24hr avg)
        pm10_bp = [
            (0, 54, 0, 50), (55, 154, 51, 100), (155, 254, 101, 150),
            (255, 354, 151, 200), (355, 424, 201, 300), (425, 604, 301, 500)
        ]

        # O3 (µg/m³ 轉 ppm: 約除以 2000, 8hr avg)
        # 這裡直接用 OpenWeather 的 µg/m³ 對應 EPA 的 ppb 斷點 (µg/m³ * 0.51 ≈ ppb)
        o3_conc_ppb = components.get("o3", 0) * 0.51
        o3_bp = [
            (0, 54, 0, 50), (55, 70, 51, 100), (71, 85, 101, 150),
            (86, 105, 151, 200), (106, 200, 201, 300)
        ]

        # 2. 計算各別分指數 (Individual Indices)
        indices = [
            _calculate_piecewise(components.get("pm2_5", 0), pm25_bp),
            _calculate_piecewise(components.get("pm10", 0), pm10_bp),
            _calculate_piecewise(o3_conc_ppb, o3_bp)
        ]

        # 3. 根據 EPA 規範，AQI 為所有分指數中的最大值
        return max(indices)
    def _convert_to_is_day(self, icon_id):
        return icon_id.endswith('d')
    def _convert_to_iso_time_format(self, dt: int, timezone_offset: int) -> str:
        utc_time = datetime.fromtimestamp(dt, tz=timezone.utc)
        local_time = utc_time + timedelta(seconds=timezone_offset)
        return local_time.strftime("%Y-%m-%dT%H:%M")
    def _convert_to_wmo_code(self, condition_id: int) -> int:
        # 2xx: Thunderstrom
        if 200 <= condition_id < 300: return 95
        # 3xx: Drizzle
        elif 300 <= condition_id < 400: return 51
        # 5xx: Rain & Shower Rain
        elif 500 <= condition_id < 600:
            if condition_id == 500: return 61
            elif condition_id in [501, 502]: return 63
            elif condition_id in [503, 504]: return 65
            elif condition_id == 511: return 66
            elif condition_id == 520: return 80
            elif condition_id == 521: return 81
            elif condition_id in [522, 523]: return 82
        # 6xx: Snow & Shower Snow
        elif 600 <= condition_id < 700:
            if condition_id in [600, 601, 602, 615, 616]: return 71
            elif condition_id == 611: return 77
            elif condition_id in [612, 613, 620]: return 85
            elif condition_id in [621, 622]: return 86
        # 7xx: Atmosphere
        elif 700 <= condition_id < 800: return 45
        # 8xx: Sunny or Cloudy
        elif 800 <= condition_id < 900:
            if condition_id == 800: return 0
            elif condition_id == 801: return 1
            elif condition_id in [802, 803]: return 2
            elif condition_id == 804: return 3
        else:
            logger.warning(f'Unknown condition id from open weather: {condition_id}')
        return 0

    def toCityInfo(self, data) -> CityInfo:
        return CityInfo(
            name=data[0].get('name'),
            country=data[0].get('country'),
            latitude=data[0].get('lat'),
            longitude=data[0].get('lon')
        )
    def toWeatherInfo(self, data) -> WeatherInfo:
        current_data = data.get('current')
        hourly_data = data.get('hourly')
        try:
            return WeatherInfo(
                weather_code=self._convert_to_wmo_code(current_data.get('weather')[0].get('id')),
                temp=round(current_data.get('temp'), 1),
                apparent_temp=round(current_data.get('feels_like'), 1),
                is_day=self._convert_to_is_day(current_data.get('weather')[0].get('icon')),
                humidity=current_data.get('humidity'),
                pop=hourly_data[0].get('pop') * 100,
                visibility=current_data.get('visibility'),
                sunrise=self._convert_to_iso_time_format(current_data.get('sunrise'), timezone_offset=data.get("timezone_offset")),
                sunset=self._convert_to_iso_time_format(current_data.get('sunset'), timezone_offset=data.get("timezone_offset")),
                uv_index=current_data.get('uvi')
            )
        except Exception as e:
            logger.error(f"Error occurred while converting to WeatherInfo: {e}")
    def toAirQuality(self, data) -> AirQuality:
        components_data = data.get('list')[0].get('components')
        us_aqi = self._convert_to_us_aqi(components_data)
        return AirQuality(aqi=us_aqi)
    def toHourlyWeatherSnapshotList(self, data) -> WeatherSnapshotList:
        snapshots = WeatherSnapshotList(items=[])
        hourly_data = data.get('hourly')
        for i in range(1, 25):
            snapshot = WeatherSnapshot(
                time=self._convert_to_iso_time_format(hourly_data[i].get('dt'), timezone_offset=data.get("timezone_offset")),
                weather_code=self._convert_to_wmo_code(hourly_data[i].get('weather')[0].get('id')),
                temp_max=round(hourly_data[i].get('temp'), 1),
                pop=hourly_data[i].get('pop') * 100,
                is_day=self._convert_to_is_day(hourly_data[i].get('weather')[0].get('icon'))
            )
            snapshots.items.append(snapshot)
        snapshots.count = 24
        return snapshots
    def toDailyWeatherSnapshotList(self, data) -> WeatherSnapshotList:
        snapshots = WeatherSnapshotList(items=[])
        daily_data = data.get('daily')
        for i in range(1, 8):
            snapshot = WeatherSnapshot(
                time=self._convert_to_iso_time_format(daily_data[i].get('dt'), timezone_offset=data.get("timezone_offset")),
                weather_code=self._convert_to_wmo_code(daily_data[i].get('weather')[0].get('id')),
                temp_max=round(daily_data[i].get('temp').get('max'), 1),
                temp_min=round(daily_data[i].get('temp').get('min'), 1),
                pop=daily_data[i].get('pop') * 100,
                is_day=True
            )
            snapshots.items.append(snapshot)
        snapshots.count = 7
        return snapshots

class OpenWeatherClient(BaseAPIClient):
    def __init__(self, 
                 client: AsyncClient = Depends(get_http_client),
                 adapter: OpenWeatherAdapter = Depends()):
        super().__init__()
        self.client = client
        self.adapter = adapter
        self.api_key = settings.OPEN_WEATHER_API_KEY
        self.add_base_url('weather', 'https://api.openweathermap.org/data/3.0/onecall')
        self.add_base_url('coordinate', 'http://api.openweathermap.org/geo/1.0/direct')
        self.add_base_url('air_quality', 'http://api.openweathermap.org/data/2.5/air_pollution')

    async def get_coordinates(self, city: str, country_code: str = None):
        query = f"{city}" + (f", {country_code}" if country_code is not None else "")
        params = {
            "q": query,
            "limit": 1,
            "appid": settings.OPEN_WEATHER_API_KEY
        }
        raw_data = await self.get_request(self.client, url_key='coordinate', params=params)
        return self.adapter.toCityInfo(raw_data)

    async def get_weather_info(self, lat: float, lon: float):
        params = {
            "lat": lat,
            "lon": lon,
            "appid": settings.OPEN_WEATHER_API_KEY,
            "exclude": "minutely,daily,alerts",
            "units": "metric"
        }
        raw_data = await self.get_request(self.client, url_key='weather', params=params)
        # print(raw_data, flush=True)
        return self.adapter.toWeatherInfo(raw_data)
    
    async def get_weather_forecast_hourly(self, lat: float, lon: float):
        params = {
            "lat": lat,
            "lon": lon,
            "appid": settings.OPEN_WEATHER_API_KEY,
            "exclude": "current,minutely,daily,alerts",
            "units": "metric"
        }
        raw_data = await self.get_request(self.client, url_key='weather', params=params)
        return self.adapter.toHourlyWeatherSnapshotList(raw_data)

    async def get_weather_forecast_daily(self, lat: float, lon: float):
        params = {
            "lat": lat,
            "lon": lon,
            "appid": settings.OPEN_WEATHER_API_KEY,
            "exclude": "current,minutely,hourly,alerts",
            "units": "metric"
        }
        raw_data = await self.get_request(self.client, url_key='weather', params=params)
        return self.adapter.toDailyWeatherSnapshotList(raw_data)
    
    async def get_air_quality(self, lat: float, lon: float):
        params = {
            "lat": lat,
            "lon": lon,
            "appid": settings.OPEN_WEATHER_API_KEY,
        }
        raw_data = await self.get_request(self.client, url_key="air_quality", params=params)
        return self.adapter.toAirQuality(raw_data)
    