import styles from "./style.module.css";

function NewsSnapshot() {
  return (
    <div className={styles.outerContainer}>
      <img className={styles.image} src="https://www.gamereactor.cn/media/93/geoffkeighleyopens_4269353b.jpg"></img>
      <div className={styles.newsInfoContainer}>
        <p className="body-bold text-align-left">報告：「樂高地平線之所以只在 Switch 上推出，是因為樂高的規定」</p>
        <div className={styles.sourceInfoContainer}>
          <div className={styles.dateContainer}>
            <div className={styles.iconContainer}>
              <img className="img-fit" src="/assets/other/date.svg"></img>
            </div>
            <p className="caption-2">03/09</p>
          </div>
          <p className="caption-2">gamereactor.cn</p>
        </div>
      </div>
    </div>
  );
}

export default NewsSnapshot;
