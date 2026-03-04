import styles from "./style.module.css";
import TextButton from "../../../common/buttons/text-button";
import IconButton from "../../../common/buttons/icon-button";
import CurrencySelector from "../currency-selector";
import MyLineChart from "../../../common/charts/line-chart";

import { useCurrencyConverter } from "../../../../hooks/currency/useCurrencyConverter";

const data = [
  { time: "01-01", rate: 0.26 },
  { time: "01-08", rate: 0.27 },
  { time: "01-15", rate: 0.273 },
  { time: "01-22", rate: 0.264 },
  { time: "02-01", rate: 0.285 },
  { time: "02-08", rate: 0.281 },
  { time: "02-15", rate: 0.29 },
  { time: "02-22", rate: 0.286 },
];

function CurrencyPage() {

  const {
    amount, setAmount,
    result,
    fromCurrency, setFromCurrency,
    toCurrency, setToCurrency,
    handleSwap,
  } = useCurrencyConverter();

  return (
    <div className={styles.contentContainer}>
      <div className={styles.exchangeContainer}>
        <div className={styles.currencyRowContainer}>
          <CurrencySelector
            value={fromCurrency}
            onChange={setFromCurrency}
          ></CurrencySelector>
          <input
            className={`body-bold ${styles.input}`}
            placeholder="請輸入數值"
            type="nuber"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          ></input>
        </div>
        <IconButton
          height="20px"
          imgSrcActive="/assets/exchange.svg"
          imgSrcInactive="/assets/exchange.svg"
          noPadding="true"
          noBackground="true"
          onClick={handleSwap}
        ></IconButton>
        <div className={styles.currencyRowContainer}>
          <CurrencySelector
            value={toCurrency}
            onChange={setToCurrency}
          ></CurrencySelector>
          <p className={`body-bold ${styles.output}`}>{result}</p>
        </div>
      </div>
      <div className={styles.historyContainer}>
        <div className={styles.timespansContainer}>
          <TextButton
            fontContent="1 週"
            fontStyle="caption"
            borderStyle="round"
          ></TextButton>
          <TextButton
            isActive="true"
            fontContent="2 個月"
            fontStyle="caption"
            borderStyle="round"
          ></TextButton>
          <TextButton
            fontContent="6 個月"
            fontStyle="caption"
            borderStyle="round"
          ></TextButton>
          <TextButton
            fontContent="1 年"
            fontStyle="caption"
            borderStyle="round"
          ></TextButton>
          <TextButton
            fontContent="2 年"
            fontStyle="caption"
            borderStyle="round"
          ></TextButton>
        </div>
        <MyLineChart
          data={data}
          xKey="time"
          yKey="rate"
          height={180}
        ></MyLineChart>
      </div>
    </div>
  );
}

export default CurrencyPage;
