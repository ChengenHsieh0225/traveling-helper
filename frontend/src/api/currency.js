import { request } from ".";
import { toLowerCase } from "../utils/stringConversion";

function toTimespanAbbr(timespan) {
  const mapping = {
    "1 週": "1w",
    "2 個月": "2m",
    "6 個月": "6m",
    "1 年": "1y",
    "2 年": "2y",
  };

  if (timespan in mapping) {
    return mapping[timespan];
  } else {
    return timespan;
  }
}

export const currencyApi = {
  getExchangeResult: async (fromCurrency, toCurrency, amount) => {
    const endpoint = `/api/currency/latest?from_ccy=${toLowerCase(fromCurrency)}&to_ccy=${toLowerCase(toCurrency)}&amount=${amount}`;
    const data = await request(endpoint);
    return data.amount;
  },
  getRateHistory: async (fromCurrency, toCurrency, timespan) => {
    const endpoint = `/api/currency/history?from_ccy=${toLowerCase(fromCurrency)}&to_ccy=${toLowerCase(toCurrency)}&timespan=${toTimespanAbbr(timespan)}`;
    const data = await request(endpoint);
    return data.map((element) => ({
      'time': element.time,
      '匯率': element.rate,
    }));
  },
  getSupportedCurrencies: async () => {
    const endpoint = `/api/currency/support-currency`
    const data = await request(endpoint)
    return data
  }
};
