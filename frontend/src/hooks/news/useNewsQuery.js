import { useState, useEffect } from "react";
import { newsApi } from "../../api/news";

export function useNewsQuery() {
  const [city, setCity] = useState("new taipei");
  const [country, setCountry] = useState("taiwan");
  const [newsList, setNewsList] = useState([]);
  const [newsType, setNewsType] = useState('headlines');
  const [lang, setLang] = useState('zh');

  const updateLocation = ({ 'city': city, 'country': country }) => {
    setCity(city);
    setCountry(country);
  };

  const fetchNewsList = async () => {
    try {
      let newsData;
      if (newsType === 'headlines') newsData = await newsApi.getHeadlines(city, lang, country);
      else if (newsType === 'latest') newsData = await newsApi.getLatestNews(city, lang, country);
      else if (newsType === 'relevant') newsData = await newsApi.getLatestNews(city, lang, country);
      else throw new Error(`Invalid news type: ${newsType}`);

      setNewsList(newsData);
    } catch (error) {
      console.error("[Fetch News List Failed]: ", error);
    }
  }

  useEffect(() => {
    const timer = setTimeout(fetchNewsList, 300);
    return () => clearTimeout(timer);
  }, [city, newsType, lang]);

  return {
    city, setCity,
    country, setCountry,
    newsList,
    newsType, setNewsType,
    lang, setLang,
    updateLocation
  };
}
