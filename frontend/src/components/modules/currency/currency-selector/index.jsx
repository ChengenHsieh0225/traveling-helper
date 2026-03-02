import styles from "./style.module.css";

function CurrencySelector() {
  return (
    <div className={styles.outerContainer}>
      <div className={styles.iconContainer}>
        <img className="img-fit" src="https://hatscripts.github.io/circle-flags/flags/tw.svg"></img>
      </div>
      <select className={`body-bold ${styles.select}`}>
        <option>新台幣</option>
        <option>日幣</option>
        <option>韓元</option>
        <option>歐元</option>
        <option>美金</option>
      </select>
    </div>
  );
}

export default CurrencySelector;
