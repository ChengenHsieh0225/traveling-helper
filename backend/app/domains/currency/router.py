from fastapi import APIRouter
import httpx
from datetime import datetime
from dateutil.relativedelta import relativedelta

from .schema import Currency, CurrencyList, ConversionResult
from . import service

router = APIRouter()

@router.get("/")
def read_root():
    return {"Hello": "Currency Router"}

@router.get("/currency-list")
async def get_support_currency():
    url = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies.json"

    async with httpx.AsyncClient() as client:
        response = await client.get(url)
        data = response.json()
        items = [Currency(code=key, name=val) for key, val in data.items()]
        count = len(data)
        currencyList = CurrencyList(items=items, count=count)

        return currencyList

# GET: /currency-exchange/latest?from_ccy={}&to_ccy={}&amount={}
@router.get("/latest")
async def convert_currency(from_ccy: str, to_ccy: str, amount: float):
    if from_ccy == to_ccy:
        return ConversionResult(
            amount = amount,
            exchange_rate = 1.0
        )
    
    url = f"https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/{from_ccy}.json"

    async with httpx.AsyncClient() as client:
        response = await client.get(url)
        data = response.json()
        rate = float(f"{data.get(from_ccy).get(to_ccy):.5g}")
        converted_amount = amount * rate
        if converted_amount < 1e-4:
            converted_amount = float(f"{converted_amount:.5g}")
        else:
            converted_amount = round(converted_amount, 4)
        return ConversionResult(
            amount = converted_amount,
            exchange_rate = rate
        )
    
# GET: /currency-exchange/history?from_ccy={}&to_ccy={}&timespan={}
@router.get("/history")
async def get_rate_history(from_ccy: str, to_ccy: str, timespan: str):
    return await service.get_rate_history(from_ccy=from_ccy, to_ccy=to_ccy, timespan=timespan)

# GET: /currency-exchange/history?from_ccy={}&to_ccy={}
@router.get("/history/allspan")
async def get_all_rate_histories(from_ccy: str, to_ccy: str):
    valid_span = ['1w', '2m', '6m', '1y', '2y']

    all_histories = {}
    for span in valid_span:
        all_histories[span] = await service.get_rate_history(from_ccy=from_ccy, to_ccy=to_ccy, timespan=span)

    return all_histories
