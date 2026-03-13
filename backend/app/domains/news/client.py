from ...core.http_client import BaseAPIClient
from httpx import AsyncClient
from datetime import datetime, timedelta, timezone

import logging

logger = logging.getLogger(__name__)

class GNewsClient(BaseAPIClient):
    def __init__(self, api_key: str):
        super().__init__()
        self.add_base_url('top_headlines', 'https://gnews.io/api/v4/top-headlines')
        self.add_base_url('search', 'https://gnews.io/api/v4/search')

        self.api_key = api_key

    async def get_headlines(self, 
                            client: AsyncClient, 
                            category: str = 'general',
                            lang: str = 'zh',
                            country_code: str = 'any',
                            keyword: str = None):
        params = {
            'apikey': self.api_key,
            'category': category,
            'lang': lang,
            'country': country_code,
            'q': keyword
        }
        return await self.get_request(client, url_key='top_headlines', params=params)
    
    async def get_headlines_with_publication_time(self, 
                            client: AsyncClient, 
                            category: str = 'general',
                            lang: str = 'zh',
                            country_code: str = 'any',
                            keyword: str = None,
                            from_time: str = None,
                            to_time: str = None):
        params = {
            'apikey': self.api_key,
            'category': category,
            'lang': lang,
            'country': country_code,
            'q': keyword,
            'from': from_time,
            'to': to_time
        }
        return await self.get_request(client, url_key='top_headlines', params=params)
    
    async def get_latest_news(self, 
                            client: AsyncClient,
                            keyword: str,
                            lang: str = 'zh',
                            country_code: str = 'any',
                            search_scope: str = 'time, description'):
        params = {
            'apikey': self.api_key,
            'q': keyword,
            'lang': lang,
            'country': country_code,
            'in': search_scope,
            'sortby': 'publishedAt'
        }
        return await self.get_request(client, url_key='search', params=params)

    async def get_most_relevant_news(self, 
                            client: AsyncClient,
                            keyword: str,
                            lang: str = 'zh',
                            country_code: str = 'any',
                            search_scope: str = 'time, description'):
        now = datetime.now(timezone.utc)
        valid_date = now - timedelta(days=29)
        formatted_valid_date = valid_date.strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3] + 'Z'

        params = {
            'apikey': self.api_key,
            'q': keyword,
            'lang': lang,
            'country': country_code,
            'in': search_scope,
            'sortby': 'relevance',
            'from': formatted_valid_date
        }
        return await self.get_request(client, url_key='search', params=params)