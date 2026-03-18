import { useNavigate } from "react-router-dom";

import styles from "./style.module.css";
import NewsSnapshot from "../news-snapshot";
import NewsOptionSelector from "../news-option-selector";

import { useNewsQuery } from "../../../../hooks/news/useNewsQuery";

const SUPPORTED_LANGUAGES = [
  {
    value: 'zh',
    label: '中文'
  },
  {
    value: 'en',
    label: '英文'
  }
]

const SUPPORTED_NEWS_TYPES = [
  {
    value: 'headlines',
    label: '熱門'
  },
  {
    value: 'latest',
    label: '最新'
  },
  {
    value: 'relevant',
    label: '最相關'
  }
]

function NewsPage() {

  const navigate = useNavigate()

  const {
    city,
    country,
    newsList,
    newsType, setNewsType,
    lang, setLang,
    cityIndex, setCityIndex,
    supportedCities
  } = useNewsQuery();

  return (
    <div className={styles.contentContainer}>
      <div className={styles.optionContainer}>
        <NewsOptionSelector
          value={cityIndex}
          valueList={supportedCities}
          iconUrl="/assets/other/location-pin.svg"
          onChange={(index) => setCityIndex(index)}
        ></NewsOptionSelector>
        <NewsOptionSelector
          value={newsType}
          valueList={SUPPORTED_NEWS_TYPES}
          iconUrl="/assets/other/sort.svg"
          onChange={(index) => setNewsType(SUPPORTED_NEWS_TYPES[index].value)}
        ></NewsOptionSelector>
        <NewsOptionSelector
          value={lang}
          valueList={SUPPORTED_LANGUAGES}
          iconUrl="/assets/other/language.svg"
          onChange={(index) => setLang(SUPPORTED_LANGUAGES[index].value)}
        ></NewsOptionSelector>
      </div>
      {newsList.map((item) => {
        return (
          <NewsSnapshot
            key={item.id}
            title={item.title}
            description={item.description}
            imgSrc={item.image}
            date={item.publish_time}
            source={item.source}
            onClick={() => navigate(`./${item.id}`, { state: { newsData: item } })}
          ></NewsSnapshot>
        );
      })}
    </div>
  );
}

export default NewsPage;
