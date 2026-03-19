from pydantic import BaseModel

class CityRead(BaseModel):
    ch_name: str
    en_name: str
    ch_country_name: str | None
    en_country_name: str | None
    country_code: str | None
    lat: float | None
    lon: float | None

class CityInfo(BaseModel):
    name: str
    country: str | None
    latitude: float
    longitude: float

class WeatherInfo(BaseModel):
    weather_code: int
    temp: float
    apparent_temp: float
    is_day: bool # is day or night
    humidity: float
    pop: float
    visibility: float
    sunrise: str
    sunset: str
    uv_index: float

class AirQuality(BaseModel):
    # uv_index: float
    aqi: float

class WeatherDetail(WeatherInfo, AirQuality):
    pass

class WeatherSnapshot(BaseModel):
    time: str
    weather_code: int
    temp_max: float
    temp_min: float | None = None
    pop: float
    is_day: bool | None = None

class WeatherSnapshotList(BaseModel):
    items: list[WeatherSnapshot]
    count: int | None = None