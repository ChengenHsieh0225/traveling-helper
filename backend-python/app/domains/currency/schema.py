from pydantic import BaseModel

class CurrencyRead(BaseModel):
    code: str
    ch_name: str
    en_name: str | None = None
    symbol: str | None = None
    icon_code: str

class ConversionResult(BaseModel):
    amount: float
    exchange_rate: float | None = None