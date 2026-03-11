from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from contextlib import asynccontextmanager
from app.core import deps
import httpx

from app.api.api import api_router

@asynccontextmanager
async def lifespan(app: FastAPI):
    deps._http_client = httpx.AsyncClient(
        limits=httpx.Limits(
            max_connections=20,
            max_keepalive_connections=5
        ),
        timeout=20.0
    )
    yield
    await deps._http_client.aclose()

app = FastAPI(lifespan=lifespan)

origins = [
    "http://127.0.0.1:5500",
    "http://localhost:5500",
    "http://127.0.0.1:5173", # for Vite (npm run dev)
    "http://localhost:5173",
    "http://127.0.0.1:4173", # for Vite (npm run preview)
    "http://localhost:4173",
    "https://traveling-helper.vercel.app" # Vercel
]
app.add_middleware(
    CORSMiddleware,
    allow_origins = origins,
    allow_credentials = True,
    allow_methods = ["*"],
    allow_headers = ["*"]
)

app.include_router(api_router, prefix='/api')