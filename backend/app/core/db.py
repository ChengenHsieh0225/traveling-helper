from sqlmodel import create_engine, SQLModel
from app.domains.common.models.currency import Currency
from app.core.config import settings

database_url = settings.POSTGRESQL_DATABASE_URL
engine = create_engine(
    database_url, 
    echo=True,
    pool_pre_ping=True,
    pool_recycle=300
)

def init_db():
    SQLModel.metadata.create_all(engine)