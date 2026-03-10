import styles from "./style.module.css";
import NewsSnapshot from "../news-snapshot";

import { useNewsQuery } from "../../../../hooks/news/useNewsQuery";

function NewsPage() {
  const {
    city,
    setCity,
    country,
    setCountry,
    newsList,
    newsType,
    setNewsType,
  } = useNewsQuery();

  return (
    <div className={styles.contentContainer}>
      {newsList.map((item) => {
        return (
          <NewsSnapshot
            key={item.id}
            title={item.title}
            description={item.description}
            imgSrc={item.image}
            date={item.publish_time}
            source={item.source}
          ></NewsSnapshot>
        );
      })}
    </div>
  );
}

export default NewsPage;
