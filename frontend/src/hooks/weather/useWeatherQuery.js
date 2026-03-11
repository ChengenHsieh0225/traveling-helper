import { useState, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { weatherApi } from "../../api/weather";

export function useWeatherQuery() {
  const [city, setCity] = useState('新北市');
  const [country, setCountry] = useState('台灣');
  const [timespan, setTimespan] = useState('24 小時');

  const { data: weatherDetail = '', isLoading: isDetailLoading, error: detailError } = useQuery({
    queryKey: [city],
    queryFn: async () => {
      return await weatherApi.getDetail(city);
    },
    staleTime: 3 * 60 * 1000,
    gcTime: 60 * 60 * 1000,
    retry: 1,
    enabled: !!city
  });

  const { data: forecast = [], isLoading: isForecastLoading, error: forecastError } = useQuery({
    queryKey: [city, timespan],
    queryFn: async () => {
      return await weatherApi.getForecast(city, timespan);
    },
    staleTime: 15 * 60 * 1000,
    gcTime: 60 * 60 * 1000,
    retry: 1,
    enabled: !!city && !!timespan
  });

  return {
    city, setCity,
    country, setCountry,
    timespan, setTimespan,
    weatherDetail,
    forecast
  };
}