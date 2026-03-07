from fastapi import APIRouter, Depends
from httpx import AsyncClient
import asyncio
from app.core.deps import get_http_client

from . import service
from .schema import WeatherDetail

router = APIRouter()
 
@router.get("/details")
async def get_weather_details(city: str, country_code: str = None, client: AsyncClient = Depends(get_http_client)):
    city_info = await service.get_coordinates(city, client=client)
    weather_task = service.get_weather_info(latitude=city_info.latitude, longitude=city_info.longitude, client=client)
    air_quality_task = service.get_air_quality(latitude=city_info.latitude, longitude=city_info.longitude, client=client)
    weather_info, air_quality = await asyncio.gather(weather_task, air_quality_task)

    return WeatherDetail(**weather_info.model_dump(), **air_quality.model_dump())

@router.get("/forecast")
async def get_weather_forecast(city: str, country_code: str = None, timespan: str = '1d', client: AsyncClient = Depends(get_http_client)):
    city_info = await service.get_coordinates(city, client=client)
    return await service.get_weather_forecast(latitude=city_info.latitude, longitude=city_info.longitude, timespan=timespan, client=client)