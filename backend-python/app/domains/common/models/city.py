from sqlmodel import SQLModel, Field, Relationship
from .currency import Currency

class City(SQLModel, table=True):
    __tablename__ = 'cities'

    id: str = Field(primary_key=True)
    ch_name: str
    en_name: str
    ch_country_name: str | None = None
    en_country_name: str | None = None
    country_code: str | None = None
    currency_code: str = Field(foreign_key="currencies.code")
    lat: float | None = None
    lon: float | None = None
    is_popular: bool = False

    currency: Currency = Relationship() 