import "./App.css";
import { Routes, Route, Navigate } from "react-router-dom";
import WeatherPage from "./components/modules/weather/weather-page";
import CurrencyPage from "./components/modules/currency/currency-page";
import NewsLayout from "./components/modules/news/news-layout";
import NewsPage from "./components/modules/news/news-page";
import NewsDetailPage from "./components/modules/news/news-detail-page";
import EmptyPage from "./components/modules/empty-page";
import Header from "./components/common/header";
import NavigationBar from "./components/common/navigation-bar";

import { useEffect } from "react";

function App() {

  useEffect(() => {
    const handler = (e) => {
      console.log('瀏覽器已準備好顯示安裝提示');
    };
  
    window.addEventListener('beforeinstallprompt', handler);
    return () => window.removeEventListener('beforeinstallprompt', handler);
  }, []);

  return (
    <div className="app-container">
      <Header></Header>
      <Routes>
        <Route path="/weather" element={<WeatherPage />} />
        <Route path="/currency" element={<CurrencyPage />} />
        <Route path="/news" element={<NewsLayout />}>
          <Route index element={<NewsPage />} />
          <Route path=":newsId" element={<NewsDetailPage />} />
        </Route>
        <Route path="/locked" element={<EmptyPage />} />
        <Route path="/" element={<Navigate to="/currency" />} />
      </Routes>
      <NavigationBar></NavigationBar>
    </div>
  );
}

export default App;
