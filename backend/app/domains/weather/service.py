from httpx import AsyncClient

from .schema import CityInfo, WeatherInfo, AirQuality, WeatherSnapshot, WeatherSnapshotList

async def get_coordinates(city: str, client: AsyncClient, countryCode: str = None):
    url = "https://geocoding-api.open-meteo.com/v1/search"
    params = {
        "name": city,
        "count": 2,
        "language": "en",
        "format": "json",
        **({"countryCode": countryCode} if countryCode is not None else {})
    }

    response = await client.get(url, params=params, timeout=20.0)
    data = response.json()

    if 'results' not in data or not data['results']:
        return None

    return CityInfo(**data['results'][0])
    
async def get_weather_info(latitude: float, longitude: float, client: AsyncClient):
    url = "https://api.open-meteo.com/v1/forecast"
    params = {
        "latitude": latitude,
        "longitude": longitude,
        "daily": ["sunrise", "sunset"],
        "current": [
            "temperature_2m", 
            "relative_humidity_2m", 
            "apparent_temperature", 
            "weather_code", 
            "is_day", 
            "precipitation_probability",
            "visibility"
        ],
        "timezone": "auto",
    }

    response = await client.get(url, params=params, timeout=20.0)
    data = response.json()
    return WeatherInfo(
        weather_code=data['current']['weather_code'],
        temp=data['current']['temperature_2m'],
        apparent_temp=data['current']['apparent_temperature'],
        is_day=data['current']['is_day'],
        humidity=data['current']['relative_humidity_2m'],
        pop=data['current']['precipitation_probability'],
        visibility=data['current']['visibility'],
        sunrise=data['daily']['sunrise'][0],
        sunset=data['daily']['sunset'][0]
    )

async def get_air_quality(latitude: float, longitude: float, client: AsyncClient):
    url = "https://air-quality-api.open-meteo.com/v1/air-quality"
    params = {
        "latitude": latitude,
        "longitude": longitude,
        "current": ["us_aqi", "uv_index"],
    }

    response = await client.get(url, params=params, timeout=20.0)
    data = response.json()
    return AirQuality(
        uv_index=data['current']['uv_index'],
        aqi=data['current']['us_aqi']
    )
    
async def get_weather_forecast(latitude: float, longitude: float, timespan: str, client: AsyncClient):
    url = "https://api.open-meteo.com/v1/forecast"

    snaphots = WeatherSnapshotList(items=[])

    if timespan == '1d':
        params = {
            "latitude": latitude,
            "longitude": longitude,
            "hourly": ["precipitation_probability", "temperature_2m", "weather_code"],
            "timezone": "auto",
            "forecast_days": 2,
        }
        response = await client.get(url, params=params, timeout=20.0)
        hourly_data = response.json()['hourly']
        for i in range(24):
            snapshot = WeatherSnapshot(
                time=hourly_data['time'][i],
                weather_code=hourly_data['weather_code'][i],
                temp_max=hourly_data['temperature_2m'][i],
                pop=hourly_data['precipitation_probability'][i]
            )
            snaphots.items.append(snapshot)
    elif timespan == '1w':
        params = {
            "latitude": latitude,
            "longitude": longitude,
            "daily": ["weather_code", "temperature_2m_max", "temperature_2m_min", "precipitation_probability_max"],
            "timezone": "auto",
        }
        response = await client.get(url, params=params, timeout=20.0)
        daily_data = response.json()['daily']
        # return daily_data
        for i in range(7):
            snapshot = WeatherSnapshot(
                time=daily_data['time'][i],
                weather_code=daily_data['weather_code'][i],
                temp_max=daily_data['temperature_2m_max'][i],
                temp_min=daily_data['temperature_2m_min'][i],
                pop=daily_data['precipitation_probability_max'][i]
            )
            snaphots.items.append(snapshot)
    return snaphots