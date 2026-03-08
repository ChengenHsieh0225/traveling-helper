from httpx import AsyncClient
import asyncio
from datetime import datetime
from dateutil.relativedelta import relativedelta

from ...clients.currency_client import FawazClient

currency_client = FawazClient()

def get_dates(timespan: str):
    today = datetime.now()
    latest_date = today - relativedelta(days=1)

    if timespan == '1w': return [(latest_date - relativedelta(days=i)).strftime("%Y-%m-%d") for i in range(6, -1, -1)]
    if timespan == '2m': return [(latest_date - relativedelta(days=4*i)).strftime("%Y-%m-%d") for i in range(14, -1, -1)]
    if timespan == '6m': return [(latest_date - relativedelta(weeks=i)).strftime("%Y-%m-%d") for i in range(23, -1, -1)]
    if timespan == '1y': return [(latest_date - relativedelta(months=i)).strftime("%Y-%m-%d") for i in range(11, -1, -1)]
    if timespan == '2y': return [(latest_date - relativedelta(months=i)).strftime("%Y-%m-%d") for i in range(23, -1, -1)]

    return []

async def fetch_single_rate(client: AsyncClient, from_ccy: str, to_ccy: str, date: str):
    data = await currency_client.get_rate_with_date(client, from_ccy=from_ccy, to_ccy=to_ccy, date=date)
    rate = data.get(from_ccy).get(to_ccy)
    return {
        'time': date,
        'rate': rate
    }

async def fetch_latest_rate(client: AsyncClient, from_ccy: str, to_ccy: str):
    data = await currency_client.get_latest_rate(client, from_ccy=from_ccy, to_ccy=to_ccy)
    return data.get(from_ccy).get(to_ccy)

async def get_rate_history(client: AsyncClient, from_ccy: str, to_ccy: str, timespan: str):
    valid_span = ['1w', '2m', '6m', '1y', '2y']

    if timespan not in valid_span:
        return []
    
    dates = get_dates(timespan)

    tasks = [fetch_single_rate(client, from_ccy, to_ccy, date) for date in dates]
    date_to_rate_list = await asyncio.gather(*tasks)

    return date_to_rate_list

async def get_support_currency(client: AsyncClient):
    return await currency_client.get_support_currency(client)