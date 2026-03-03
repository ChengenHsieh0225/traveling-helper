import styles from "./style.module.css";
import TextButton from "../../../common/buttons/text-button";
import IconButton from "../../../common/buttons/icon-button";
import WeatherSnapshot from "../weather-snapshot";
import WeatherDetail from "../weather-detail";

function WeatherPage({ isStarred }) {
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
            <p className="body-bold">新北市, 台灣</p>
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
                src="/assets/cloud-filled.svg"
              ></img>
            </div>
            <p className="body-bold">多雲</p>
          </div>
          <div className={styles.tempContainer}>
            <p className="h2">26°C</p>
            <p className="body-bold">體感溫度 25°C</p>
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
          <WeatherSnapshot
            time="01:00"
            weather="sunny"
            temp="20°C"
            pop="30%"
          ></WeatherSnapshot>
          <WeatherSnapshot
            time="02:00"
            weather="sunny"
            temp="20°C"
            pop="30%"
          ></WeatherSnapshot>
          <WeatherSnapshot
            time="03:00"
            weather="sunny"
            temp="20°C"
            pop="30%"
          ></WeatherSnapshot>
          <WeatherSnapshot
            time="04:00"
            weather="sunny"
            temp="20°C"
            pop="30%"
          ></WeatherSnapshot>
          <WeatherSnapshot
            time="05:00"
            weather="sunny"
            temp="20°C"
            pop="30%"
          ></WeatherSnapshot>
        </div>
      </div>
      <div className={styles.detailContainer}>
        <WeatherDetail
          iconImgSrc="/assets/sunny-outlined.svg"
          title="紫外線指數"
          content="弱"
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/droplet-outlined.svg"
          title="濕度"
          content="76%"
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/air-outlined.svg"
          title="空氣品質 AQI"
          content="良好 (41)"
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/visibility.svg"
          title="能見度"
          content="9.66 公里"
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/sunrise.svg"
          title="日出時間"
          content="06:21"
        ></WeatherDetail>
        <WeatherDetail
          iconImgSrc="/assets/sunset.svg"
          title="日落時間"
          content="17:53"
        ></WeatherDetail>
      </div>
    </div>
  );
}

export default WeatherPage;
