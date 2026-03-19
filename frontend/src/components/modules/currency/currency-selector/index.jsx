import { CircleFlag } from 'react-circle-flags';

import styles from "./style.module.css";

function CurrencySelector({ value, onChange, currencyDict }) {
  return (
    <div className={styles.outerContainer}>
      <CircleFlag
        countryCode={currencyDict[value]?.icon_code}
        height="24px"
        width="24px"
      ></CircleFlag>
      <select 
        className={`body-bold ${styles.select}`}
        value={value}
        onChange={(e) => onChange(e.target.value)}
      >
        {Object.entries(currencyDict).map(([code, info]) => {
          return <option key={code} value={code}>{info.ch_name}</option>
        })}
      </select>
    </div>
  );
}

export default CurrencySelector;
