from fastapi import Depends
import logging

from .base_handler import BaseHandler
from ..clients.open_weather_client import OpenWeatherClient

logger = logging.getLogger(__name__)

class OpenWeatherHandler(BaseHandler):
    def __init__(self, client: OpenWeatherClient=Depends()):
        super().__init__()
        self.client=client
    
    async def get_coordinates(self, city: str, country_code: str = None):
        try:
            data = await self.client.get_coordinates(city=city, country_code=country_code)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenWeather failed, trying next handler: {e}")

        return await super().get_coordinates(city, country_code)
    
    async def get_weather_info(self, latitude: float, longitude: float):
        try:
            data = await self.client.get_weather_info(lat=latitude, lon=longitude)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenWeather failed, trying next handler: {e}")

        return await super().get_weather_info(latitude, longitude)
    
    async def get_air_quality(self, latitude: float, longitude: float):
        try:
            data = await self.client.get_air_quality(lat=latitude, lon=longitude)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenWeather failed, trying next handler: {e}")

        return await super().get_air_quality(latitude, longitude)
    
    async def get_weather_forecast(self, latitude: float, longitude: float, timespan: str):
        try:
            if timespan == '1w':
                data = await self.client.get_weather_forecast_daily(lat=latitude, lon=longitude)
            # default case: hourly forecast for one day
            else:
                data = await self.client.get_weather_forecast_hourly(lat=latitude, lon=longitude)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenWeather failed, trying next handler: {e}")

        return await super().get_weather_forecast(latitude, longitude, timespan)