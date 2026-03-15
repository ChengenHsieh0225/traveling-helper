from fastapi import APIRouter, Depends

from .service import get_weather_service, WeatherService

router = APIRouter()
 
@router.get("/details")
async def get_weather_details(city: str, country_code: str = None, service: WeatherService = Depends(get_weather_service)):
    return await service.get_weather_details_by_city(city=city, country_code=country_code)

@router.get("/forecast")
async def get_weather_forecast(city: str, country_code: str = None, timespan: str = '1d', service: WeatherService = Depends(get_weather_service)):
    return await service.get_weather_forecast_by_city(city=city, country_code=country_code, timespan=timespan)