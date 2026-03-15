from fastapi import Depends
import asyncio

from .schema import WeatherDetail
from .handlers.base_handler import BaseHandler
from .handlers.open_meteo_handler import OpenMeteoHandler
from .handlers.redis_handler import RedisHandler

class WeatherService:
    def __init__(self, handler: BaseHandler):
        self.handler = handler

    async def get_coordinates(self, city: str, country_code: str = None):
        return await self.handler.get_coordinates(city=city, country_code=country_code)
        
    async def get_weather_details_by_city(self, city: str, country_code: str = None):
        city_info = await self.get_coordinates(city, country_code)
        return await self.get_weather_details_by_coordinates(city_info.latitude, city_info.longitude)

    async def get_weather_details_by_coordinates(self, latitude: float, longitude: float):
        weather_task = self.handler.get_weather_info(latitude, longitude)
        air_quality_task = self.handler.get_air_quality(latitude, longitude)
        weather_info, air_quality = await asyncio.gather(weather_task, air_quality_task)
        return WeatherDetail(**weather_info.model_dump(), **air_quality.model_dump())
    
    async def get_weather_forecast_by_city(self, city: str, country_code: str = None, timespan: str = '1d'):
        city_info = await self.get_coordinates(city, country_code)
        return await self.get_weather_forecast_by_coordinates(city_info.latitude, city_info.longitude, timespan)

    async def get_weather_forecast_by_coordinates(self, latitude: float, longitude: float, timespan: str):
        return await self.handler.get_weather_forecast(latitude, longitude, timespan)

def get_weather_service(
        open_meteo_handler: OpenMeteoHandler = Depends(),
        redis_handler: RedisHandler = Depends()
) -> WeatherService:
    chain = redis_handler
    redis_handler.set_next(open_meteo_handler)
    return WeatherService(chain)