import { useState, useEffect } from "react";
import { weatherApi } from "../../api/weather";

export function useWeatherQuery() {
  const [city, setCity] = useState('新北市');
  const [country, setCountry] = useState('台灣');
  const [timespan, setTimespan] = useState('24 小時');
  const [weatherDetail, setWeatherDetail] = useState('');
  const [forecast, setForecast] = useState([]);

  const fetchDetails = async () => {
    try {
      const detailData = await weatherApi.getDetail(city);
      setWeatherDetail(detailData);
    } catch (error) {
      console.error("[Fetch Weather Detail Failed]: ", error);
    }
  }
  const fetchForecast = async () => {
    try {
      const forecastData = await weatherApi.getForecast(city, timespan);
      setForecast(forecastData);
    } catch (error) {
      console.error("[Fetch Weather Forecast Failed]: ", error);
    }
  }

  useEffect(() => {
    const timer = setTimeout(fetchDetails, 300);
    return () => clearTimeout(timer);
  }, [city]);

  useEffect(() => {
    const timer = setTimeout(fetchForecast, 300);
    return () => clearTimeout(timer);
  }, [city, timespan]);

  return {
    city, setCity,
    country, setCountry,
    timespan, setTimespan,
    weatherDetail,
    forecast
  };
}