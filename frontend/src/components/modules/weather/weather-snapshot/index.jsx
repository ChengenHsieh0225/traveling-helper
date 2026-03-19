import styles from "./style.module.css";
import { getWeatherIconUrl } from "../helper";

function WeatherSnapshot({ time, weatherCode: weatherCode, temp_max, temp_min, pop, isDay }) {
  const temp_display = temp_min ? `${Math.round(temp_max)}°/${Math.round(temp_min)}°` : `${temp_max.toFixed(1)}°C`;
  return (
    <div className={styles.snapshotContainer}>
      <p className="caption">{time}</p>
      <div className={styles.innerContainer}>
        <div className={styles.weatherIconContainer}>
          <img className="img-fit" src={getWeatherIconUrl(weatherCode, isDay)}></img>
        </div>
        <p className="body-bold">{temp_display}</p>
      </div>
      <div className={styles.popContainer}>
        <div className={styles.dropletContainer}>
          <img className="img-fit" src="/assets/other/droplet.svg"></img>
        </div>
        <p className="caption">{pop}%</p>
      </div>
    </div>
  );
}

export default WeatherSnapshot;
