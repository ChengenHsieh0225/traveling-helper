from fastapi import APIRouter

from ..domains.currency.router import router as currency_router
from ..domains.weather.router import router as weather_router
from ..domains.news.router import router as news_router

api_router = APIRouter()

api_router.include_router(currency_router, prefix='/currency', tags=['currency'])
api_router.include_router(weather_router, prefix='/weather', tags=['weather'])
api_router.include_router(news_router, prefix='/news', tags=['news'])