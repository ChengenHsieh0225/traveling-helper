from ....core.http_client import BaseAPIClient
from httpx import AsyncClient
from fastapi import Depends
from app.core.deps import get_http_client
from ..schema import CityInfo, WeatherInfo, AirQuality, WeatherSnapshot, WeatherSnapshotList

import logging

logger = logging.getLogger(__name__)

class OpenMeteoAdapter:
    def toCityInfo(self, data) -> CityInfo:
        return CityInfo(**data['results'][0])
    
    def toWeatherInfo(self, data) -> WeatherInfo:
        return WeatherInfo(
            weather_code=data['current']['weather_code'],
            temp=data['current']['temperature_2m'],
            apparent_temp=data['current']['apparent_temperature'],
            is_day=data['current']['is_day'],
            humidity=data['current']['relative_humidity_2m'],
            pop=data['current']['precipitation_probability'],
            visibility=data['current']['visibility'],
            sunrise=data['daily']['sunrise'][0],
            sunset=data['daily']['sunset'][0]
        )
    
    def toWeatherSnapshotList(self, data, timespan) -> WeatherSnapshot:
        snaphots = WeatherSnapshotList(items=[])
        if timespan == '1d':
            hourly_data = data['hourly']
            for i in range(24):
                snapshot = WeatherSnapshot(
                    time=hourly_data['time'][i],
                    weather_code=hourly_data['weather_code'][i],
                    temp_max=hourly_data['temperature_2m'][i],
                    pop=hourly_data['precipitation_probability'][i],
                    is_day=hourly_data['is_day'][i]
                )
                snaphots.items.append(snapshot)
        elif timespan == '1w':
            daily_data = data['daily']
            for i in range(7):
                snapshot = WeatherSnapshot(
                    time=daily_data['time'][i],
                    weather_code=daily_data['weather_code'][i],
                    temp_max=daily_data['temperature_2m_max'][i],
                    temp_min=daily_data['temperature_2m_min'][i],
                    pop=daily_data['precipitation_probability_max'][i]
                )
                snaphots.items.append(snapshot)
        return snaphots

    def toAirQuality(self, data) -> AirQuality:
        return AirQuality(
            uv_index=data['current']['uv_index'],
            aqi=data['current']['us_aqi']
        )

class OpenMeteoClient(BaseAPIClient):
    def __init__(self, client: AsyncClient = Depends(get_http_client), adapter: OpenMeteoAdapter = Depends()):
        super().__init__()
        self.client = client
        self.adapter = adapter
        self.add_base_url('weather', 'https://api.open-meteo.com/v1/forecast')
        self.add_base_url('coordinate', 'https://geocoding-api.open-meteo.com/v1/search')
        self.add_base_url('air_quality', 'https://air-quality-api.open-meteo.com/v1/air-quality')
    
    async def get_coordinates(self, city: str, country_code: str = None):
        params = {
            "name": city,
            "count": 2,
            "language": "en",
            "format": "json",
            **({"countryCode": country_code} if country_code is not None else {})
        }
        data = await self.get_request(self.client, url_key='coordinate', params=params)
        return self.adapter.toCityInfo(data)

    async def get_weather_info(self, latitude: float, longitude: float):
        params = {
            "latitude": latitude,
            "longitude": longitude,
            "daily": ["sunrise", "sunset"],
            "current": [
                "temperature_2m", 
                "relative_humidity_2m", 
                "apparent_temperature", 
                "weather_code", 
                "is_day", 
                "precipitation_probability",
                "visibility"
            ],
            "timezone": "auto",
        }
        data = await self.get_request(self.client, url_key='weather', params=params)
        return self.adapter.toWeatherInfo(data)
    
    async def get_weather_forecast(self, latitude: float, longitude: float, timespan: str):
        if timespan not in [ '1d', '1w' ]:
            logger.error(f"The given timespan is invalid: {timespan}, use '1d' as default timespan")
            timespan = '1d'

        if timespan == '1d':
            params = {
                "latitude": latitude,
                "longitude": longitude,
                "hourly": ["precipitation_probability", "temperature_2m", "weather_code", "is_day"],
                "timezone": "auto",
                "forecast_days": 2,
            }
        else:
            params = {
                "latitude": latitude,
                "longitude": longitude,
                "daily": ["weather_code", "temperature_2m_max", "temperature_2m_min", "precipitation_probability_max"],
                "timezone": "auto",
            }
        data = await self.get_request(self.client, url_key='weather', params=params)
        return self.adapter.toWeatherSnapshotList(data, timespan=timespan)
    
    async def get_air_quality(self, latitude: float, longitude: float):
        params = {
            "latitude": latitude,
            "longitude": longitude,
            "current": ["us_aqi", "uv_index"]
        }
        data = await self.get_request(self.client, url_key='air_quality', params=params)
        return self.adapter.toAirQuality(data)