import { useContext, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { newsApi } from "../../api/news";
import { NewsContext } from "../../contexts/NewsContext";

export function useNewsQuery() {
  const {
    city, setCity,
    country, setCountry,
    newsType, setNewsType,
    lang, setLang,
    cityIndex, setCityIndex
  } = useContext(NewsContext);

  useEffect(() => {
    if (supportedCities.length > 0) {
      const isEn = lang === 'en';
      setCity(isEn ? supportedCities[cityIndex].en_name : supportedCities[cityIndex].ch_name),
      setCountry(isEn ? supportedCities[cityIndex].en_country_name: supportedCities[cityIndex].ch_country_name)
    }
  }, [cityIndex, lang])

  const { data: newsList = [], isLoading, error } = useQuery({
    queryKey: [city, newsType, lang],
    queryFn: async () => {
      if (newsType === 'headlines') return await newsApi.getHeadlines(city, lang, country);
      if (newsType === 'latest') return await newsApi.getLatestNews(city, lang, country);
      if (newsType === 'relevant') return await newsApi.getMostRelevantNews(city, lang, country);
      throw new Error(`Invalid news type: ${newsType}`);
    },

    staleTime: 30 * 60 * 1000, // 30 mins
    gcTime: 60 * 60 * 1000,
    retry: 1,
    enabled: !!city && !!country
  });

  const { data: supportedCities = [], isCitiesLoading } = useQuery({
    queryKey: ['supportedCities'],
    queryFn: async () => {
      const data = await newsApi.getSupportedCities();
      return data;
    },

    staleTime: 60 * 60 * 1000,
    gcTime: 24 * 60 * 60 * 1000,
    retry: 1
  });

  return {
    city,
    country,
    newsList,
    newsType, setNewsType,
    lang, setLang,
    cityIndex, setCityIndex,
    supportedCities
  };
}
