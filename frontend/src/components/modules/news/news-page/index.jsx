import styles from "./style.module.css";

import NewsSnapshot from "../news-snapshot";

function NewsPage() {
  return (
    <div className={styles.contentContainer}>
      <NewsSnapshot></NewsSnapshot>
    </div>
  );
}

export default NewsPage;
