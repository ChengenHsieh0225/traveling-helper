from fastapi import APIRouter
import httpx
from datetime import datetime
from dateutil.relativedelta import relativedelta

from .schema import Currency, CurrencyList, ConversionResult

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
    
# GET: /currency-exchange/history?from_ccy={}&to_ccy={}&amount={}
@router.get("/history")
async def exchange_rate_history(from_ccy: str, to_ccy: str, amount: float):
    # span: 1 week / 2 month / half year / 1 year / 2 years

    today = datetime.now()
    latest_date = today - relativedelta(days=1)

    dates = {}
    dates["1 week"] = [(latest_date - relativedelta(days=i)).strftime("%Y-%m-%d") for i in range(7)]
    dates["2 months"] = [(latest_date - relativedelta(weeks=i)).strftime("%Y-%m-%d") for i in range(8)]
    dates["6 months"] = [(latest_date - relativedelta(weeks=2*i)).strftime("%Y-%m-%d") for i in range(12)]
    dates["1 year"] = [(latest_date - relativedelta(months=i)).strftime("%Y-%m-%d") for i in range(12)]
    dates["2 years"] = [(latest_date - relativedelta(months=2*i)).strftime("%Y-%m-%d") for i in range(12)]

    date_to_rate = {}

    for span in dates:
        for date in dates[span]:
            if date not in date_to_rate:
                url = f"https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@{date}/v1/currencies/{from_ccy}.json"
                async with httpx.AsyncClient() as client:
                    response = await client.get(url)
                    data = response.json()
                    rate = data.get(from_ccy).get(to_ccy)
                    date_to_rate[date] = rate
    
    history = {}
    for span in dates:
        history[span] = {}
        for date in dates[span]:
            history[span][date] = {}
            history[span][date]["rate"] = date_to_rate[date]
            history[span][date]["amount"] = round(amount * rate, 5)

    # pprint(history)
    return 1