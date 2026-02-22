from fastapi import APIRouter
import httpx

router = APIRouter()

@router.get("/")
async def get_weather():
    url = "https://api.open-meteo.com/v1/forecast"
    params = {
        "latitude": 25.062,
        "longitude": 121.457,
        "hourly": ["temperature_2m", "weather_code"],
        "timezone": "auto",
        "forecast_days": 1,
    }

    async with httpx.AsyncClient() as client:
        response = await client.get(url, params=params)
        data = response.json()
        return data