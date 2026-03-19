from fastapi import APIRouter, Depends
from sqlmodel import Session, select

from app.core.deps import get_session
from .service import get_weather_service, WeatherService
from ..common.models.city import City
from .schema import CityRead

router = APIRouter()
 
@router.get("/details")
async def get_weather_details(city: str, country_code: str = None, service: WeatherService = Depends(get_weather_service)):
    return await service.get_weather_details_by_city(city=city, country_code=country_code)

@router.get("/forecast")
async def get_weather_forecast(city: str, country_code: str = None, timespan: str = '1d', service: WeatherService = Depends(get_weather_service)):
    return await service.get_weather_forecast_by_city(city=city, country_code=country_code, timespan=timespan)

@router.get("/support-city", response_model=list[CityRead])
async def get_support_city(session: Session = Depends(get_session)):
    statement = select(City)
    results = session.exec(statement).all()
    return results