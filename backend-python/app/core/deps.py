import httpx
from sqlmodel import Session
from typing import Generator
from app.core.db import engine

_http_client: httpx.AsyncClient = None

async def get_http_client() -> httpx.AsyncClient:
    return _http_client

def get_session() -> Generator[Session, None, None]:
    with Session(engine) as session:
        yield session