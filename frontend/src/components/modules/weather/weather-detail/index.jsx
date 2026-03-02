import styles from "./style.module.css";

function WeatherDetail({ iconImgSrc, title, content }) {
  return (
    <div className="align-row align-space-between">
      <div className={styles.titleContainer}>
        <div className={styles.iconContainer}>
          <img className="img-fit" src={iconImgSrc}></img>
        </div>
        <p className="body-medium">{title}</p>
      </div>
      <p className="body-medium">{content}</p>
    </div>
  );
}

export default WeatherDetail;
