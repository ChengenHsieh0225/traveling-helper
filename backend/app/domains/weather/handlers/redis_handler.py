from fastapi import Depends
import logging, json

from .base_handler import BaseHandler
from ....core.redis_client import RedisClient, get_redis_client

logger = logging.getLogger(__name__)

class RedisHandler(BaseHandler):
    def __init__(self, redis_client: RedisClient = Depends(get_redis_client)):
        super().__init__()
        self.redis_client = redis_client
        self.prefix = 'weather'

    def _get_key(self, key):
        return f'{self.prefix}:{key}'    
    
    def _get_coordinates_key(self, city: str, country_code: str = None):
        key = f'coord:{city}:{country_code or 'any'}'
        return self._get_key(key)
    
    def _get_weather_info_key(self, latitude: float, longitude: float):
        key = f'info:{round(latitude, 2)}:{round(longitude, 2)}'
        return self._get_key(key)
    
    def _get_weather_forecast_key(self, latitude: float, longitude: float, timespan: str):
        key = f'forecast:{round(latitude, 2)}:{round(longitude, 2)}:{timespan}'
        return self._get_key(key)
    
    def _get_air_quality_key(self, latitude: float, longitude: float):
        key = f'air-quality:{round(latitude, 2)}:{round(longitude, 2)}'
        return self._get_key(key)

    async def _get_json(self, key):
        raw = await self.redis_client.get(key)
        return json.loads(raw) if raw else None

    async def _set_json(self, key, data, ex):
        try:
            await self.redis_client.set(key, json.dumps(data), ex=ex)
        except Exception as e:
            logger.warning(f"Redis _set_json writing error: {e}")
    
    async def get_coordinates(self, city: str, country_code: str = None):
        try:
            key = self._get_coordinates_key(city=city, country_code=country_code)
            data = await self._get_json(key)
            if data:
                return data
            
            data = await super().get_coordinates(city, country_code)
            if data:
                await self._set_json(key, data=data, ex=24*60*60)
            return data
        
        except Exception as e:
            logger.warning(f"Redis failed, trying next handler:: {e}")
            return await super().get_coordinates(city, country_code)
    
    async def get_weather_info(self, latitude: float, longitude: float):
        try:
            key = self._get_weather_info_key(latitude=latitude, longitude=longitude)
            data = await self._get_json(key)
            if data:
                return data
            
            data = await super().get_weather_info(latitude, longitude)
            if data:
                await self._set_json(key, data=data, ex=10*60)
            return data
        
        except Exception as e:
            logger.warning(f"Redis failed, trying next handler:: {e}")
            return await super().get_weather_info(latitude, longitude)
    
    async def get_air_quality(self, latitude: float, longitude: float):
        try:
            key = self._get_air_quality_key(latitude=latitude, longitude=longitude)
            data = await self._get_json(key)
            if data:
                return data
            
            data = await super().get_air_quality(latitude, longitude)
            if data:
                await self._set_json(key, data=data, ex=10*60)
            return data
        
        except Exception as e:
            logger.warning(f"Redis failed, trying next handler:: {e}")
            return await super().get_air_quality(latitude, longitude)
    
    async def get_weather_forecast(self, latitude: float, longitude: float, timespan: str):
        try:
            key = self._get_weather_forecast_key(latitude=latitude, longitude=longitude, timespan=timespan)
            data = await self._get_json(key)
            if data:
                return data
            
            data = await super().get_weather_forecast(latitude, longitude, timespan)
            if data:
                await self._set_json(key, data=data, ex=30*60)
            return data
        
        except Exception as e:
            logger.warning(f"Redis failed, trying next handler:: {e}")
            return await super().get_weather_forecast(latitude, longitude, timespan)