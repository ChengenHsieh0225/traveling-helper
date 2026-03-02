import styles from "./style.module.css";
import IconButton from "../buttons/icon-button";

function NavigationBar() {
  return (
    <div className={styles.outerContainer}>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          borderStyle="round"
          imgSrcActive="/assets/currency-filled.svg"
          imgSrcInactive="/assets/currency-outlined.svg"
        ></IconButton>
        <p className="caption">匯率</p>
      </div>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          isActive="true"
          borderStyle="round"
          imgSrcActive="/assets/cloud-filled.svg"
          imgSrcInactive="/assets/cloud-outlined.svg"
        ></IconButton>
        <p className="caption">天氣</p>
      </div>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          borderStyle="round"
          imgSrcActive="/assets/news-filled.svg"
          imgSrcInactive="/assets/news-outlined.svg"
        ></IconButton>
        <p className="caption">新聞</p>
      </div>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          borderStyle="round"
          imgSrcActive="/assets/lock-filled.svg"
          imgSrcInactive="/assets/lock-outlined.svg"
        ></IconButton>
        <p className="caption">尚未開放</p>
      </div>
    </div>
  );
}

export default NavigationBar;
