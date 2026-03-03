import "./App.css";
import WeatherPage from "./components/modules/weather/weather-page";
import CurrencyPage from "./components/modules/currency/currency-page";
import Header from "./components/common/header";
import NavigationBar from "./components/common/navigation-bar";

function App() {
  return (
    <div className="app-container">
      <Header pageTitle="匯率查詢"></Header>
      <WeatherPage></WeatherPage>
      {/* <CurrencyPage></CurrencyPage> */}
      <NavigationBar></NavigationBar>
    </div>
  );
}

export default App;
