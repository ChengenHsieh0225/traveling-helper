import { request } from ".";

const timespanMapping = (timespan) => {
  const mapping = {
    "24 小時": "1d",
    "未來 7 天": "1w",
  };

  if (timespan in mapping) {
    return mapping[timespan];
  } else {
    return "1d";
  }
};

const formatDetail = (data) => {
  return {
    temp: data.temp,
    apparent_temp: data.apparent_temp,
    uv_index: Number(data.uv_index),
    humidity: data.humidity,
    aqi: Number(data.aqi),
    visibility: (Number(data.visibility) / 1000).toFixed(2),
    sunrise: data.sunrise.split('T')[1],
    sunset: data.sunset.split('T')[1],
    is_day: Boolean(data.is_day),
    weather_code: data.weather_code,
  };
}

const formatForecast = (data, timespan) => {
  return data.items.map(
    (item) => {
      // console.log('item: ', item.weather_code);
      return {
        time: item.time,
        weather_code: item.weather_code,
        temp: item.temp_max,
        temp_min: item.temp_min,
        pop: item.pop
      };
    }
  )
};

export const weatherApi = {
  getDetail: async (city, countryCode = "") => {
    // const endpoint = `/api/weather/details?city=${city}&countryCode=${countryCode}`;
    const endpoint = `/api/weather/details?city=${"New Taipei"}`;
    const data = await request(endpoint);
    return formatDetail(data);
  },
  getForecast: async (city, countryCode = "", timespan = "24 小時") => {
    // const endpoint = `/api/weather/forecast?city=${city}&countryCode=${countryCode}&timespan=${timespanMapping(timespan)}`;
    const endpoint = `/api/weather/forecast?city=${"New Taipei"}&timespan=${timespanMapping(timespan)}`;
    const data = await request(endpoint);
    return formatForecast(data, timespan);
  },
};
