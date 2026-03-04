import { useState, useEffect } from "react";
import { toLowerCase } from "../../utils/stringConversion";
import { isNumeric } from "../../utils/numberProcessing";

const API_BASE_URL =
  window.location.hostname === "localhost" ||
  window.location.hostname === "127.0.0.1"
    ? "http://127.0.0.1:8000"
    : "https://traveling-helper.onrender.com";

export function useCurrencyConverter() {
  const [amount, setAmount] = useState('');
  const [result, setResult] = useState(0);
  const [fromCurrency, setFromCurrency] = useState("twd");
  const [toCurrency, setToCurrency] = useState("jpy");

  const url = `${API_BASE_URL}/api/currency/latest?from_ccy=${toLowerCase(fromCurrency)}&to_ccy=${toLowerCase(toCurrency)}&amount=${amount}`;

  useEffect(() => {
    const fetchExchange = async () => {
      fetch(url)
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

    if (isNumeric(amount)) {
      const timer = setTimeout(fetchExchange, 300); // Debounce
      return () => clearTimeout(timer);
    }
  }, [amount, fromCurrency, toCurrency]);

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
  };
}
