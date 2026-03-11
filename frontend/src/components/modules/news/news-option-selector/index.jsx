import styles from "./style.module.css";

function NewsOptionSelector({ value, valueList, iconUrl, onChange }) {
  return (
    <div className={styles.selectorContainer}>
      <select
        className={styles.select}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        style={{
          backgroundImage: `url(${iconUrl})`,
        }}
      >
        {Object.entries(valueList).map(([key, info]) => {
          return (
            <option key={key} value={key}>
              {info.label}
            </option>
          );
        })}
        {/* <option>台北</option> */}
      </select>
    </div>
  );
}

export default NewsOptionSelector;
