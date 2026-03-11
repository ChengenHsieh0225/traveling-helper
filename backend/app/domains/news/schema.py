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