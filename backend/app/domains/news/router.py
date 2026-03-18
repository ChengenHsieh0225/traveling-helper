from fastapi import APIRouter, Depends
from httpx import AsyncClient
from sqlmodel import Session, select
from app.core.deps import get_http_client, get_session
import logging

from . import service
from ..common.models.city import City
from .schema import CityRead

router = APIRouter()
logger = logging.getLogger(__name__)

@router.get("/support-city", response_model=list[CityRead])
async def get_support_city(session: Session = Depends(get_session)):
    statement = select(City)
    results = session.exec(statement).all()
    return results

@router.get("/headlines")
async def get_headlines(client: AsyncClient = Depends(get_http_client),
                        published_country_code: str = None,
                        related_city: str = None,
                        lang: str = None):
    return await service.get_headlines(client=client, country_code=published_country_code, city=related_city, lang=lang)

@router.get("/latest-news")
async def get_latest_news(client: AsyncClient = Depends(get_http_client),
                        published_country_code: str = None,
                        related_city: str = None,
                        lang: str = None):
    return await service.get_recent_news(client=client, country_code=published_country_code, city=related_city, lang=lang)

@router.get("/relevant-news")
async def get_most_relevant_news(client: AsyncClient = Depends(get_http_client),
                        published_country_code: str = None,
                        related_city: str = None,
                        lang: str = None):
    return await service.get_most_relevant_news(client=client, country_code=published_country_code, city=related_city, lang=lang)