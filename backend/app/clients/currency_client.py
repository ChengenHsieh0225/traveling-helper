from .http_client import BaseAPIClient
from httpx import AsyncClient

import logging

logger = logging.getLogger(__name__)

class FawazClient(BaseAPIClient):
    def __init__(self):
        super().__init__()

    async def get_rate_with_date(self, client: AsyncClient, from_ccy: str, to_ccy: str, date: str):
        url = f"https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@{date}/v1/currencies/{from_ccy}.json"
        return await self.get_request(client, override_url=url)
    
    async def get_latest_rate(self, client: AsyncClient, from_ccy: str, to_ccy: str):
        url = f"https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/{from_ccy}.json"
        return await self.get_request(client, override_url=url)

    async def get_support_currency(self, client: AsyncClient):
        url = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies.json"
        return await self.get_request(client, override_url=url)