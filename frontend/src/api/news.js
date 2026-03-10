import { request } from ".";

const formatNewsContent = (content, description) => {
  if (!content) return "";

  const junkWords = [/Copied!/gi, /\[\d+ chars\]/gi];

  let cleanedContent = content;

  junkWords.forEach(regex => {
    cleanedContent = cleanedContent.replace(regex, "");
  });

  const searchSnippet = description.substring(0, 15);
  const startIndex = cleanedContent.indexOf(searchSnippet);

  if (startIndex !== -1) {
    cleanedContent = cleanedContent.substring(startIndex);
  }

  return cleanedContent.trim();
};

const formatNewsList = (data) => {
  return data.items.map(
    (item) => {
      return {
        id: item.id,
        title: item.title,
        description: item.description,
        content: formatNewsContent(item.content, item.description),
        url: item.url,
        image: item.image,
        source: item.source,
        publish_time: item.publish_time
      };
    }
  );
};

export const newsApi = {
  getHeadlines: async (city, countryCode = "tw") => {
    const endpoint = `/api/news/headlines?published_country_code=${countryCode}&lang=${"zh"}`;
    const data = await request(endpoint);
    return formatNewsList(data);
  },
  getLatestNews: async (city, countryCode = "tw") => {
    const endpoint = `/api/news/latest-news?related_city=${"Tokyo"}&lang=${"en"}`;
    const data = await request(endpoint);
    return formatNewsList(data);
  },
  getMostRelevantNews: async (city, countryCode = "tw") => {
    const endpoint = `/api/news/relevant-news?related_city=${"Paris"}&lang=${"en"}`;
    const data = await request(endpoint);
    return formatNewsList(data);
  }
};
