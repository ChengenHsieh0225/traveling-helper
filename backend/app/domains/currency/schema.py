from pydantic import BaseModel

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