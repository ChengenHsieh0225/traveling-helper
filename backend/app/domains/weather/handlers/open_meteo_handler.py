from fastapi import Depends
import logging

from .base_handler import BaseHandler
from ..clients.open_meteo_client import OpenMeteoClient

logger = logging.getLogger(__name__)

class OpenMeteoHandler(BaseHandler):
    def __init__(self, client: OpenMeteoClient=Depends()):
        super().__init__()
        self.client=client
    
    async def get_coordinates(self, city: str, country_code: str = None):
        try:
            data = await self.client.get_coordinates(city=city, country_code=country_code)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenMeteo failed, trying next handler: {e}")

        return await super().get_coordinates(city, country_code)
    
    async def get_weather_info(self, latitude: float, longitude: float):
        try:
            data = await self.client.get_weather_info(latitude=latitude, longitude=longitude)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenMeteo failed, trying next handler: {e}")

        return await super().get_weather_info(latitude, longitude)
    
    async def get_air_quality(self, latitude: float, longitude: float):
        try:
            data = await self.client.get_air_quality(latitude=latitude, longitude=longitude)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenMeteo failed, trying next handler: {e}")

        return await super().get_air_quality(latitude, longitude)
    
    async def get_weather_forecast(self, latitude: float, longitude: float, timespan: str):
        try:
            data = await self.client.get_weather_forecast(latitude=latitude, longitude=longitude, timespan=timespan)
            if data:
                return data
        except Exception as e:
            logger.warning(f"OpenMeteo failed, trying next handler: {e}")

        return await super().get_weather_forecast(latitude, longitude, timespan)