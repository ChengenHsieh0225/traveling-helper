import "./App.css";
import WeatherPage from "./components/modules/weather/weather-page";
// import Header from "./components/common/header";

function App() {
  return (
    <div className="app-container">
      <WeatherPage></WeatherPage>
      {/* <Header pageTitle="嘗試頁面"></Header> */}
    </div>
  );
}

export default App;
