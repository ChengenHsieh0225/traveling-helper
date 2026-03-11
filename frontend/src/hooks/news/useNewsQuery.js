import { useContext } from "react";
import { useQuery } from "@tanstack/react-query";
import { newsApi } from "../../api/news";
import { NewsContext } from "../../contexts/NewsContext";

export function useNewsQuery() {
  const {
    city, setCity,
    country, setCountry,
    newsType, setNewsType,
    lang, setLang
  } = useContext(NewsContext);

  const updateLocation = ({ 'city': city, 'country': country }) => {
    setCity(city);
    setCountry(country);
  };

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

  return {
    city, setCity,
    country, setCountry,
    newsList,
    newsType, setNewsType,
    lang, setLang,
    updateLocation
  };
}
