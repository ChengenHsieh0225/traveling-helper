import { useState, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { currencyApi } from "../../api/currency";
import { isNumeric } from "../../utils/numberProcessing";

export function useCurrencyConverter() {
  const [amount, setAmount] = useState('');
  const [result, setResult] = useState(0);
  const [fromCurrency, setFromCurrency] = useState("twd");
  const [toCurrency, setToCurrency] = useState("jpy");
  const [timespan, setTimespan] = useState('1 週');

  const fetchExchange = async () => {
    try {
      const exchangeResult = await currencyApi.getExchangeResult(fromCurrency ,toCurrency, amount);
      setResult(exchangeResult);
    } catch (error) {
      console.error("[Fetch Exchange Failed]: ", error);
    }
  }

  useEffect(() => {
    if (isNumeric(amount)) {
      const timer = setTimeout(fetchExchange, 300); // Debounce
      return () => clearTimeout(timer);
    }
  }, [amount, fromCurrency, toCurrency]);

  const { data: rateHistory = [], isLoading: isHistoryLoading, error} = useQuery({
    queryKey: [fromCurrency, toCurrency, timespan],
    queryFn: async () => {
      return await currencyApi.getRateHistory(fromCurrency, toCurrency, timespan);
    },

    staleTime: 10 * 60 * 1000,
    gcTime: 60 * 60 * 1000,
    retry: 1,
    enabled: !!fromCurrency && !!toCurrency && !!timespan
  });

  const handleSwap = () => {
    setFromCurrency(toCurrency);
    setToCurrency(fromCurrency);
  };

  const { data: currencyDict = {}, isLoading: isCurrenciesLoading } = useQuery({
    queryKey: ['supportedCurrencies'], 
    
    queryFn: async () => {
      const data = await currencyApi.getSupportedCurrencies();
      return Object.fromEntries(data.map(c => [c.code, c]));
    },
    
    staleTime: 60 * 60 * 1000,
    gcTime: 24 * 60 * 60 * 1000,
    retry: 1
  });

  return {
    amount, setAmount,
    result,
    fromCurrency, setFromCurrency,
    toCurrency, setToCurrency,
    handleSwap,
    timespan, setTimespan,
    rateHistory,
    currencyDict
  };
}
