from fastapi import APIRouter, Depends
from httpx import AsyncClient
from app.core.deps import get_http_client
import logging

router = APIRouter()
logger = logging.getLogger(__name__)
