import styles from "./style.module.css";
import { getWeatherIconUrl } from "../helper";

function WeatherSnapshot({ time, weatherCode: weatherCode, temp, pop, isDay }) {
  return (
    <div className={styles.snapshotContainer}>
      <p className="caption">{time}</p>
      <div className={styles.innerContainer}>
        <div className={styles.weatherIconContainer}>
          <img className="img-fit" src={getWeatherIconUrl(weatherCode, isDay)}></img>
        </div>
        <p className="body-bold">{temp}°C</p>
      </div>
      <div className={styles.popContainer}>
        <div className={styles.dropletContainer}>
          <img className="img-fit" src="/assets/droplet.svg"></img>
        </div>
        <p className="caption">{pop}%</p>
      </div>
    </div>
  );
}

export default WeatherSnapshot;
