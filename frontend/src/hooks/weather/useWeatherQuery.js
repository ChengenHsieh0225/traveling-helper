import { useState, useEffect, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import { weatherApi } from "../../api/weather";

export function useWeatherQuery() {
  const [timespan, setTimespan] = useState('24 小時');
  const [isSearching, setIsSearching] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [cityIndex, setCityIndex] = useState(0);

  const { data: cityList = [] } = useQuery({
    queryKey: ['cityList'],
    queryFn: async () => {
      const data = await weatherApi.getSupportedCities();
      return data;
    },

    staleTime: 60 * 60 * 1000,
    gcTime: 24 * 60 * 60 * 1000,
    retry: 1
  });

  const filteredCityList = useMemo(() => {
    if (!searchTerm) return cityList;
    return cityList.filter(city => 
      city.ch_name.includes(searchTerm) || 
      city.en_name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      city.ch_country_name.includes(searchTerm) || 
      city.en_country_name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [searchTerm, cityList]);

  const city = useMemo(() => {
    return filteredCityList[cityIndex]?.en_name || filteredCityList[0]?.en_name || "New Taipei";
  }, [cityIndex]);

  const country_code = useMemo(() => {
    return filteredCityList[cityIndex]?.country_code || filteredCityList[0]?.country_code || "tw";
  }, [cityIndex]);

  const { data: weatherDetail = '', isLoading: isDetailLoading, error: detailError } = useQuery({
    queryKey: [city, country_code],
    queryFn: async () => {
      return await weatherApi.getDetail(city, country_code);
    },
    staleTime: 3 * 60 * 1000,
    gcTime: 60 * 60 * 1000,
    retry: 1,
    enabled: !!city
  });

  const { data: forecast = [], isLoading: isForecastLoading, error: forecastError } = useQuery({
    queryKey: [city, timespan, country_code],
    queryFn: async () => {
      return await weatherApi.getForecast(city, timespan, country_code);
    },
    staleTime: 15 * 60 * 1000,
    gcTime: 60 * 60 * 1000,
    retry: 1,
    enabled: !!city && !!timespan
  });

  return {
    timespan, setTimespan,
    weatherDetail,
    forecast,
    isSearching, setIsSearching,
    searchTerm, setSearchTerm,
    cityIndex, setCityIndex,
    filteredCityList
  };
}