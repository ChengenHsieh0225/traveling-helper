from sqlmodel import SQLModel, Field

class Currency(SQLModel, table=True):
    __tablename__ = 'currencies'

    code: str = Field(primary_key=True, index=True)
    ch_name: str
    en_name: str | None = None
    symbol: str | None = None
    
    @property
    def icon_code(self) -> str:
        return self.code[:2].lower() if self.code else ""