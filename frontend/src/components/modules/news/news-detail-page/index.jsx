import { useLocation, useParams } from "react-router-dom";

import styles from "./style.module.css";

function NewsDetailPage() {
  const { newsId } = useParams();
  const location = useLocation();
  const newsData = location.state?.newsData;

  return (
    <div className={styles.contentContainer}>
      <div className={styles.sourceInfoContainer}>
        <div className={styles.iconContainer}>
          <p className={styles.iconFontStyle}>N</p>
        </div>
        <div className="align-col">
          <p className="body text-align-left">{newsData?.publish_time}</p>
          <p className="body-bold text-align-left">{newsData?.source}</p>
        </div>
      </div>
      <p className="title text-align-left">{newsData?.title}</p>
      <img className={styles.image} src={newsData?.image}></img>
      <p className="body-medium text-align-left">
        {newsData?.content}
        {newsData?.url && (
          <a
            href={newsData.url}
            target="_blank"
            rel="noopener noreferrer"
            className={styles.readMoreLink}
          >
            [閱讀全文]
          </a>
        )}
      </p>
    </div>
  );
}

export default NewsDetailPage;
