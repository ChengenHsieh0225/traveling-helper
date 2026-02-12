from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import httpx
from pydantic import BaseModel

from datetime import datetime
from dateutil.relativedelta import relativedelta
from pprint import pprint

class Currency(BaseModel):
    code: str
    country: str | None = None
    name: str | None = None
    symbol: str | None = None

class CurrencyList(BaseModel):
    items: list[Currency]
    count: int | None = None

class ConversionResult(BaseModel):
    amount: float
    exchange_rate: float | None = None

app = FastAPI()

origins = [
    "http://127.0.0.1:5500",
    "http://localhost:5500"
]
app.add_middleware(
    CORSMiddleware,
    allow_origins = origins,
    allow_credentials = True,
    allow_methods = ["*"],
    allow_headers = ["*"]
)

@app.get("/")
def read_root():
    return {"Hello": "World"}

@app.get("/currency-exchange/currency-list")
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
@app.get("/currency-exchange/latest")
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
@app.get("/currency-exchange/history")
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