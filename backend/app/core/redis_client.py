import logging
import redis.asyncio as redis
import os

logger = logging.getLogger(__name__)

class RedisClient():
    _instance = None
    _client = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._client = redis.from_url(
                os.getenv("REDIS_URL"),
                decode_responses=True,
                socket_timeout=5,
                health_check_interval=30
            )
        return cls._instance

    async def set(self, key: str, value: str, ex: int = 600):
        try:
            await self._client.set(key, value, ex=ex)
        except Exception as e:
            logger.error(f"Redis Set Error: {e}")

    async def get(self, key: str):
        return await self._client.get(key)

def get_redis_client() -> RedisClient:
    return RedisClient()