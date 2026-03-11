import styles from "./style.module.css";
import NewsSnapshot from "../news-snapshot";
import NewsOptionSelector from "../news-option-selector";

import { useNewsQuery } from "../../../../hooks/news/useNewsQuery";

const transformNewsType = (newsType) => {
  if (newsType === 'headline') return '熱門';
  if (newsType === 'latest') return '最新';
  if (newsType === 'relevant') return '相關程度';
  return '';
}

const SUPPORTED_LANGUAGES = {
  'zh': {
    value: 'zh',
    label: '中文'
  },
  'en': {
    value: 'en',
    label: '英文'
  }
}

const SUPPORTED_NEWS_TYPES = {
  'headlines': {
    value: 'headlines',
    label: '熱門'
  },
  'latest': {
    value: 'latest',
    label: '最新'
  },
  'relevant': {
    value: 'relevant',
    label: '最相關'
  }
}

const SUPPORTED_CITIES = {
  'taipei': {
    value: {
      city: 'taipei',
      country: 'taiwan'
    },
    label: '台北, 台灣'
  },
  'new taipei': {
    value: {
      city: 'new taipei',
      country: 'taiwan'
    },
    label: '新北, 台灣'
  },
  'tokyo': {
    value: {
      city: 'tokyo',
      country: 'japan'
    },
    label: '東京, 日本'
  },
  'paris': {
    value: {
      city: 'paris',
      country: 'france'
    },
    label: '巴黎, 法國'
  }
}

function NewsPage() {
  const {
    city, setCity,
    country, setCountry,
    newsList,
    newsType, setNewsType,
    lang, setLang,
    updateLocation
  } = useNewsQuery();

  return (
    <div className={styles.contentContainer}>
      <div className={styles.optionContainer}>
        <NewsOptionSelector
          value={city}
          valueList={SUPPORTED_CITIES}
          iconUrl="/assets/other/location-pin.svg"
          onChange={(key) => {
            const selectedData = SUPPORTED_CITIES[key].value;
            updateLocation(selectedData);
          }}
        ></NewsOptionSelector>
        <NewsOptionSelector
          value={newsType}
          valueList={SUPPORTED_NEWS_TYPES}
          iconUrl="/assets/other/sort.svg"
          onChange={setNewsType}
        ></NewsOptionSelector>
        <NewsOptionSelector
          value={lang}
          valueList={SUPPORTED_LANGUAGES}
          iconUrl="/assets/other/language.svg"
          onChange={setLang}
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
          ></NewsSnapshot>
        );
      })}
    </div>
  );
}

export default NewsPage;
