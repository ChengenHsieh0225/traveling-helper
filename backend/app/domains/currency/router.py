from fastapi import APIRouter, Depends
from httpx import AsyncClient
from sqlmodel import Session, select
from app.core.deps import get_http_client, get_session

from .schema import CurrencyRead, ConversionResult
from .models import Currency
from . import service

router = APIRouter()

@router.get("/")
def read_root():
    return {"Hello": "Currency Router"}

@router.get("/support-currency", response_model=list[CurrencyRead])
async def get_support_currency(session: Session = Depends(get_session)):
    statement = select(Currency)
    results = session.exec(statement).all()
    return results

# GET: /currency-exchange/latest?from_ccy={}&to_ccy={}&amount={}
@router.get("/latest")
async def convert_currency(from_ccy: str, to_ccy: str, amount: float, client: AsyncClient = Depends(get_http_client)):
    if from_ccy == to_ccy:
        return ConversionResult(
            amount = amount,
            exchange_rate = 1.0
        )
    
    rate = await service.fetch_latest_rate(client, from_ccy=from_ccy, to_ccy=to_ccy)
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
async def get_rate_history(from_ccy: str, to_ccy: str, timespan: str, client: AsyncClient = Depends(get_http_client)):
    return await service.get_rate_history(from_ccy=from_ccy, to_ccy=to_ccy, timespan=timespan, client=client)

# GET: /currency-exchange/history?from_ccy={}&to_ccy={}
@router.get("/history/allspan")
async def get_all_rate_histories(from_ccy: str, to_ccy: str, client: AsyncClient = Depends(get_http_client)):
    valid_span = ['1w', '2m', '6m', '1y', '2y']

    all_histories = {}
    for span in valid_span:
        all_histories[span] = await service.get_rate_history(from_ccy=from_ccy, to_ccy=to_ccy, timespan=span, client=client)

    return all_histories
