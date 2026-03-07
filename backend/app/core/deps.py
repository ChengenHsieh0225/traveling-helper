import httpx

_http_client: httpx.AsyncClient = None

async def get_http_client() -> httpx.AsyncClient:
    return _http_client