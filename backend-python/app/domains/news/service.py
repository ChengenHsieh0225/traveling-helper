from httpx import AsyncClient

from .schema import News, NewsList
from .client import GNewsClient
from ..common import utils
import logging
from app.core.config import settings

gnews_client = GNewsClient(settings.GNEWS_API_KEY)

logger = logging.getLogger(__name__)

def convert_data_to_news_list(data) -> NewsList:
    try:
        news_list: list[News] = []
        for article in data.get('articles'):
            news_list.append(News(
                id=article.get('id'),
                title=article.get('title'),
                description=article.get('description'),
                content=article.get('content'),
                url=article.get('url'),
                image=article.get('image'),
                source=article.get('source').get('name'),
                publish_time=article.get('publishedAt')
            ))
        return NewsList(items=news_list, count=len(news_list))

    except Exception as e:
        logger.error(f'Errors occurred while converting the news data to NewsList: {e}')

async def get_headlines(client: AsyncClient, country_code: str, city: str, lang: str):
    raw_args = {
        'lang': lang,
        'country_code': country_code,
        'keyword': utils.wrap_str_with_quote(city)
    }
    filtered_args = {k: v for k, v in raw_args.items() if v is not None}
    
    data = await gnews_client.get_headlines(client, **filtered_args)
    
    return convert_data_to_news_list(data)

async def get_recent_news(client: AsyncClient, country_code: str, city: str, lang: str):
    raw_args = {
        'lang': lang,
        'country_code': country_code,
        'keyword': utils.wrap_str_with_quote(city)
    }
    filtered_args = {k: v for k, v in raw_args.items() if v is not None}

    data = await gnews_client.get_latest_news(client, **filtered_args)
    return convert_data_to_news_list(data)

async def get_most_relevant_news(client: AsyncClient, country_code: str, city: str, lang: str):
    raw_args = {
        'lang': lang,
        'country_code': country_code,
        'keyword': utils.wrap_str_with_quote(city)
    }
    filtered_args = {k: v for k, v in raw_args.items() if v is not None}

    data = await gnews_client.get_most_relevant_news(client, **filtered_args)
    return convert_data_to_news_list(data)