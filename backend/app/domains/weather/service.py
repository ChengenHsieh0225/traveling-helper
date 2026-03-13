from fastapi import Depends

from .schema import CityInfo, WeatherInfo, AirQuality, WeatherSnapshot, WeatherSnapshotList
from .handlers.base_handler import BaseHandler
from .handlers.open_meteo_handler import OpenMeteoHandler
from .handlers.redis_handler import RedisHandler

class WeatherService:
    def __init__(self, handler: BaseHandler):
        self.handler = handler

    async def get_coordinates(self, city: str, country_code: str = None):
        data = await self.handler.get_coordinates(city=city, country_code=country_code)

        return CityInfo(**data['results'][0])
        
    async def get_weather_info(self, latitude: float, longitude: float):
        data = await self.handler.get_weather_info(latitude=latitude, longitude=longitude)
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

    async def get_air_quality(self, latitude: float, longitude: float):
        data = await self.handler.get_air_quality(latitude=latitude, longitude=longitude)
        return AirQuality(
            uv_index=data['current']['uv_index'],
            aqi=data['current']['us_aqi']
        )
        
    async def get_weather_forecast(self, latitude: float, longitude: float, timespan: str):
        snaphots = WeatherSnapshotList(items=[])

        data = await self.handler.get_weather_forecast(latitude=latitude, longitude=longitude, timespan=timespan)

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
    
def get_weather_service(
        open_meteo_handler: OpenMeteoHandler = Depends(),
        redis_handler: RedisHandler = Depends()
) -> WeatherService:
    chain = redis_handler
    redis_handler.set_next(open_meteo_handler)
    return WeatherService(chain)