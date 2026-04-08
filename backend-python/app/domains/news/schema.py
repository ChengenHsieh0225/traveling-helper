from pydantic import BaseModel

class News(BaseModel):
    id: str
    title: str
    description: str = ''
    content: str
    url: str
    image: str | None
    source: str | None
    publish_time: str | None

class NewsList(BaseModel):
    items: list[News]
    count: int | None

class CityRead(BaseModel):
    ch_name: str
    en_name: str
    ch_country_name: str | None = None
    en_country_name: str | None = None
    is_popular: bool = False