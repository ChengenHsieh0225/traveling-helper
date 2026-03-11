import { createContext, useState } from "react";

export const NewsContext = createContext();

export function NewsProvider({ children }) {
  const [city, setCity] = useState("news taipei");
  const [country, setCountry] = useState("taiwan");
  const [newsType, setNewsType] = useState("headlines");
  const [lang, setLang] = useState("zh");

  return (
    <NewsContext.Provider
      value={{
        city, setCity,
        country, setCountry,
        newsType, setNewsType,
        lang, setLang
      }}
    >
      {children}
    </NewsContext.Provider>
  );
}
