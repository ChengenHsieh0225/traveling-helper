import logging
from httpx import AsyncClient, HTTPStatusError

logger = logging.getLogger(__name__)

class BaseAPIClient:
    def __init__(self):
        self.base_url = {}
    
    def add_base_url(self, key: str, url: str):
        self.base_url[key] = url

    async def get_request(self, client: AsyncClient, url_key: str = None, params: dict = None, override_url: str = None):
        if url_key is None and override_url is None:
            logger.error(f"You must give either an url_key or an override_url.")
        elif override_url is not None:
            url = override_url
        elif url_key not in self.base_url:
            logger.error(f"The given url_key doesn't exist: {url_key}")
        else:
            url = self.base_url[url_key]

        try:
            if params is not None:
                params = {k: v for k, v in params.items() if v is not None}
            response = await client.get(url, params=params, timeout=20.0)
            response.raise_for_status()
            return response.json()
        except HTTPStatusError as e:
            logger.error(f'External API Fetching Error {e.response.status_code}: {e.response.text}')
            raise