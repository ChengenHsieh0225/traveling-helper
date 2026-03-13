import os
from dotenv import load_dotenv

load_dotenv()

class Settings:
    GNEWS_API_KEY = os.getenv('GNEWS_API_KEY')
    REDIS_URL = os.getenv('REDIS_URL')
    OPEN_WEATHER_API_KEY = os.getenv('OPEN_WEATHER_API_KEY')

settings = Settings()