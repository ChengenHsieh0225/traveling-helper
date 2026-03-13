from ....core.http_client import BaseAPIClient
from httpx import AsyncClient
from fastapi import Depends
from app.core.deps import get_http_client

import logging

logger = logging.getLogger(__name__)

class OpenMeteoClient(BaseAPIClient):
    def __init__(self, client: AsyncClient = Depends(get_http_client)):
        super().__init__()
        self.client = client
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
        return await self.get_request(self.client, url_key='coordinate', params=params)

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
        return await self.get_request(self.client, url_key='weather', params=params)
    
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
        return await self.get_request(self.client, url_key='weather', params=params)
    
    async def get_air_quality(self, latitude: float, longitude: float):
        params = {
            "latitude": latitude,
            "longitude": longitude,
            "current": ["us_aqi", "uv_index"]
        }
        return await self.get_request(self.client, url_key='air_quality', params=params)