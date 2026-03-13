from fastapi import APIRouter, Depends
import asyncio

from .service import get_weather_service, WeatherService
from .schema import WeatherDetail

router = APIRouter()
 
@router.get("/details")
async def get_weather_details(city: str, country_code: str = None, service: WeatherService = Depends(get_weather_service)):
    city_info = await service.get_coordinates(city)
    weather_task = service.get_weather_info(latitude=city_info.latitude, longitude=city_info.longitude)
    air_quality_task = service.get_air_quality(latitude=city_info.latitude, longitude=city_info.longitude)
    weather_info, air_quality = await asyncio.gather(weather_task, air_quality_task)

    return WeatherDetail(**weather_info.model_dump(), **air_quality.model_dump())

@router.get("/forecast")
async def get_weather_forecast(city: str, country_code: str = None, timespan: str = '1d', service: WeatherService = Depends(get_weather_service)):
    city_info = await service.get_coordinates(city)
    return await service.get_weather_forecast(latitude=city_info.latitude, longitude=city_info.longitude, timespan=timespan)