import styles from "./style.module.css";

function WeatherSnapshot({ time, weather, temp, pop }) {
  return (
    <div className={styles.snapshotContainer}>
      <p className="caption">{time}</p>
      <div className={styles.innerContainer}>
        <div className={styles.weatherIconContainer}>
          <img className="img-fit" src="/assets/sunny.svg"></img>
        </div>
        <p className="body-bold">{temp}</p>
      </div>
      <div className={styles.popContainer}>
        <div className={styles.dropletIconContainer}>
          <img className="img-fit" src="/assets/droplet.svg"></img>
        </div>
        <p className="caption">{pop}</p>
      </div>
    </div>
  );
}

export default WeatherSnapshot;
