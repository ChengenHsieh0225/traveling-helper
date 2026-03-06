from fastapi import APIRouter
import httpx

from . import service
from .schema import WeatherDetail

router = APIRouter()
 
@router.get("/details")
async def get_weather_details(city: str, countryCode: str = None):
    cityInfo = await service.get_coordinates(city)
    weatherInfo = await service.get_weather_info(latitude=cityInfo.latitude, longitude=cityInfo.longitude)
    airQuality = await service.get_air_quality(latitude=cityInfo.latitude, longitude=cityInfo.longitude)

    return WeatherDetail(**weatherInfo.model_dump(), **airQuality.model_dump())

@router.get("/forecast")
async def get_weather_forecast(city: str, countryCode: str = None, timespan: str = '1d'):
    cityInfo = await service.get_coordinates(city)
    return await service.get_weather_forecast(latitude=cityInfo.latitude, longitude=cityInfo.longitude, timespan=timespan)