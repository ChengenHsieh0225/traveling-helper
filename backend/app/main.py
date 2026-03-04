from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from domains.api import api_router

app = FastAPI()

origins = [
    "http://127.0.0.1:5500",
    "http://localhost:5500",
    "http://127.0.0.1:5173",
    "http://localhost:5173",
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


