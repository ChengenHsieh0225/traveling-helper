class BaseHandler:
    def __init__(self):
        self._next_handler = None

    def set_next(self, handler: 'BaseHandler'):
        self._next_handler = handler
        return handler

    # each child handler would invoke the base hander's function if it cannot handle
    async def get_coordinates(self, city: str, country_code: str = None):
        if self._next_handler: return await self._next_handler.get_coordinates(city)
        return None
    
    async def get_weather_info(self, latitude: float, longitude: float):
        if self._next_handler: return await self._next_handler.get_weather_info(latitude, longitude)
        return None
    
    async def get_air_quality(self, latitude: float, longitude: float):
        if self._next_handler: return await self._next_handler.get_air_quality(latitude, longitude)
        return None
    
    async def get_weather_forecast(self, latitude: float, longitude: float, timespan: str):
        if self._next_handler: return await self._next_handler.get_weather_forecast(latitude, longitude, timespan)
        return None