import styles from "./style.module.css";

function NewsSnapshot({ title, description, imgSrc, date, source, onClick }) {
  return (
    <div className={styles.outerContainer} onClick={() => onClick()} >
      <img className={styles.image} src={imgSrc}></img>
      <div className={styles.newsInfoContainer}>
        <p className="body-bold text-align-left">{title}</p>
        <div className={styles.sourceInfoContainer}>
          <div className={styles.dateContainer}>
            <div className={styles.iconContainer}>
              <img className="img-fit" src="/assets/other/date.svg"></img>
            </div>
            <p className="caption-2">{date}</p>
          </div>
          <p className="caption-2">{source}</p>
        </div>
      </div>
    </div>
  );
}

export default NewsSnapshot;
