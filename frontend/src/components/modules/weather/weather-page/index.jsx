import styles from "./style.module.css";
import TextButton from "../../../common/buttons/text-button";
import IconButton from "../../../common/buttons/icon-button";
import WeatherSnapshot from "../weather-snapshot";
import WeatherDetail from "../weather-detail";

import { useWeatherQuery } from "../../../../hooks/weather/useWeatherQuery";
import { transformUvIndex, transformAQI, getWeatherDescription, getWeatherIconUrl, transformTime } from "../helper";

function WeatherPage({ isStarred }) {

  const {
    city, setCity,
    country, setCountry,
    timespan, setTimespan,
    weatherDetail,
    forecast
  } = useWeatherQuery();

  return (
    <div className={styles.contentContainer}>
      <div className={styles.overviewContainer}>
        <div className="align-row align-space-between align-items-center">
          <div className={styles.locationContainer}>
            <IconButton
              height="16px"
              isActive={isStarred}
              borderStyle="square"
              imgSrcActive="/assets/star-filled.svg"
              imgSrcInactive="/assets/star-outlined.svg"
              noPadding="true"
              noBackground="true"
            ></IconButton>
            <p className="body-bold">{city}, {country}</p>
            <IconButton
              height="25px"
              isActive="true"
              borderStyle="semi"
              imgSrcActive="/assets/search.svg"
              imgSrcInactive="/assets/search.svg"
            ></IconButton>
          </div>
          <TextButton
            isActive="true"
            fontContent="現在"
            fontStyle="body-bold"
            borderStyle="semi"
          ></TextButton>
        </div>
        <div className="align-row align-space-between align-items-end">
          <div className={styles.weatherContainer}>
            <div className={styles.weatherIconContainer}>
              <img
                className="img-fit img-pos-bottom"
                src={getWeatherIconUrl(weatherDetail.weather_code)}
              ></img>
            </div>
            <p className="body-bold">{getWeatherDescription(weatherDetail.weather_code)}</p>
          </div>
          <div className={styles.tempContainer}>
            <p className="h2">{weatherDetail.temp}°C</p>
            <p className="body-bold">體感溫度 {weatherDetail.apparent_temp}°C</p>
          </div>
        </div>
      </div>
      <div className={styles.forcastContainer}>
        <div className={styles.timespanContainer}>
          <TextButton
            isActive="true"
            fontStyle="caption"
            borderStyle="round"
            fontContent="24 小時"
          ></TextButton>
          <TextButton
            // isActive="true"
            fontStyle="caption"
            borderStyle="round"
            fontContent="未來 7 天"
          ></TextButton>
        </div>
        <div className={styles.snapshotContainer}>
          {forecast.map((item) => {
            return (
            <WeatherSnapshot
              key={`${city}-${item.time}`}
              time={transformTime(item.time, timespan)}
              weather_code={item.weather_code}
              temp={item.temp}
              pop={item.pop}
            ></WeatherSnapshot>
            );
          })}
        </div>
      </div>
      <div className={styles.detailContainer}>
        <WeatherDetail
          iconImgSrc="/assets/sunny-outlined.svg"
          title="紫外線指數"
          content={transformUvIndex(weatherDetail.uv_index)}
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/droplet-outlined.svg"
          title="濕度"
          content={`${weatherDetail.humidity}%`}
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/air-outlined.svg"
          title="空氣品質 AQI"
          content={transformAQI(weatherDetail.aqi)}
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/visibility.svg"
          title="能見度"
          content={`${weatherDetail.visibility} 公尺`}
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/sunrise.svg"
          title="日出時間"
          content={weatherDetail.sunrise}
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/sunset.svg"
          title="日落時間"
          content={weatherDetail.sunset}
        ></WeatherDetail>
      </div>
    </div>
  );
}

export default WeatherPage;
