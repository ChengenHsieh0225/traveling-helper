import { CircleFlag } from 'react-circle-flags';

import styles from "./style.module.css";
import { SUPPORTED_CURRENCIES } from "../../../../constants/currencies";

function CurrencySelector({ value, onChange }) {
  return (
    <div className={styles.outerContainer}>
      <CircleFlag
        countryCode={SUPPORTED_CURRENCIES[value].iconCode}
        // countryCode='tw'
        height="24px"
        width="24px"
      ></CircleFlag>
      {/* <div className={styles.iconContainer}>
        <img className="img-fit" src="https://hatscripts.github.io/circle-flags/flags/tw.svg"></img>
      </div> */}
      <select 
        className={`body-bold ${styles.select}`}
        value={value}
        onChange={(e) => onChange(e.target.value)}
      >
        {Object.entries(SUPPORTED_CURRENCIES).map(([code, info]) => {
          return <option key={code} value={code}>{info.chName}</option>
        })}
      </select>
    </div>
  );
}

export default CurrencySelector;
