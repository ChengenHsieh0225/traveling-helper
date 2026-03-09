from fastapi import APIRouter, Depends
from httpx import AsyncClient
from app.core.deps import get_http_client
from dotenv import load_dotenv
import os
import logging

router = APIRouter()
logger = logging.getLogger(__name__)
load_dotenv()

@router.get("/test")
async def test(client: AsyncClient = Depends(get_http_client)):
    api_key = os.getenv('GNEWS_API_KEY')
    url = f"https://gnews.io/api/v4/search?q=example&lang=en&max=10&truncate=content&apikey={api_key}"
    try:
        response = await client.get(url, timeout=20.0)
        data = response.json()
        return data
    except Exception as e:
        logger.error(f'GNEWS API Error: {type(e).__name__} - {str(e)}')
        return {"status": "error", "message": str(e)}