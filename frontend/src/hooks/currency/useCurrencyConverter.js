import { useState, useEffect } from "react";
import { toLowerCase } from "../../utils/stringConversion";
import { isNumeric } from "../../utils/numberProcessing";

const API_BASE_URL =
  window.location.hostname === "localhost" ||
  window.location.hostname === "127.0.0.1"
    ? "http://127.0.0.1:8000"
    : "https://traveling-helper.onrender.com";

function toTimespanAbbr(timespan) {
  const mapping = {
    '1 週': '1w',
    '2 個月': '2m',
    '6 個月': '6m',
    '1 年': '1y',
    '2 年': '2y'
  };

  if (timespan in mapping) {
    return mapping[timespan];
  }
  else {
    return timespan;
  }
}

export function useCurrencyConverter() {
  const [amount, setAmount] = useState('');
  const [result, setResult] = useState(0);
  const [fromCurrency, setFromCurrency] = useState("twd");
  const [toCurrency, setToCurrency] = useState("jpy");
  const [timespan, setTimespan] = useState('1 週');
  const [rateHistory, setRateHistory] = useState([]);

  const exchangeUrl = `${API_BASE_URL}/api/currency/latest?from_ccy=${toLowerCase(fromCurrency)}&to_ccy=${toLowerCase(toCurrency)}&amount=${amount}`;
  const historyUrl = `${API_BASE_URL}/api/currency/history?from_ccy=${toLowerCase(fromCurrency)}&to_ccy=${toLowerCase(toCurrency)}&timespan=${toTimespanAbbr(timespan)}`;

  const fetchExchange = async () => {
    fetch(exchangeUrl)
      .then((response) => {
        if (!response.ok) {
          throw new Error("fetch failed!!");
        }
        return response.json();
      })
      .then((data) => {
        // console.log(data.amount);
        setResult(data.amount);
      })
      .catch((error) => {
        console.error("error: ", error);
      });
  };
  const fetchHistory = async () => {
    console.log(historyUrl);
    fetch(historyUrl)
      .then((response) => {
        if (!response.ok) {
          throw new Error("fetch failed!!");
        }
        return response.json();
      })
      .then((data) => {
        // console.log(data.amount);
        setRateHistory(
          data.map((element) => ({
            'time': element.time,
            '匯率': element.rate
          }))
        );
      })
      .catch((error) => {
        console.error("error: ", error);
      });
  }

  useEffect(() => {
    const timer2 = setTimeout(fetchHistory, 300);
    if (isNumeric(amount)) {
      const timer1 = setTimeout(fetchExchange, 300); // Debounce
      return () => {
        clearTimeout(timer1);
        clearTimeout(timer2);
      }
    }
    else return () => clearTimeout(timer2);
  }, [amount, fromCurrency, toCurrency, timespan]);

  const handleSwap = () => {
    setFromCurrency(toCurrency);
    setToCurrency(fromCurrency);
  };

  return {
    amount, setAmount,
    result,
    fromCurrency, setFromCurrency,
    toCurrency, setToCurrency,
    handleSwap,
    timespan, setTimespan,
    rateHistory
  };
}
