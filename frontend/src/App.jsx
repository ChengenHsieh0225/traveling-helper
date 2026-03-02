import "./App.css";
import WeatherPage from "./components/modules/weather/weather-page";
import CurrencyPage from "./components/modules/currency/currency-page";

function App() {
  return (
    <div className="app-container">
      {/* <WeatherPage></WeatherPage> */}
      <CurrencyPage></CurrencyPage>
    </div>
  );
}

export default App;
