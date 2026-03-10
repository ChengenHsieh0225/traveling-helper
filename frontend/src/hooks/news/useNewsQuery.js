import { useState, useEffect } from "react";
import { newsApi } from "../../api/news";

export function useNewsQuery() {
  const [city, setCity] = useState("新北市");
  const [country, setCountry] = useState("台灣");
  const [newsList, setNewsList] = useState([]);
  const [newsType, setNewsType] = useState('headlines');

  const fetchNewsList = async () => {
    try {
      let newsData;
      if (newsType === 'headlines') newsData = await newsApi.getHeadlines(city);
      else if (newsType === 'latest') newsData = await newsApi.getLatestNews(city);
      else if (newsType === 'relevant') newsData = await newsApi.getLatestNews(city);
      else throw new Error(`Invalid news type: ${newsType}`);

      setNewsList(newsData);
      console.log(newsData);
    } catch (error) {
      console.error("[Fetch News List Failed]: ", error);
    }
  }

  useEffect(() => {
    const timer = setTimeout(fetchNewsList, 300);
    return () => clearTimeout(timer);
  }, [newsType]);

  return {
    city, setCity,
    country, setCountry,
    newsList,
    newsType, setNewsType
  };
}
