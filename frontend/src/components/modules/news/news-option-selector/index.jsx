import styles from "./style.module.css";

function NewsOptionSelector({ value, valueList, iconUrl, onChange }) {

  const currentItem = valueList.find((item, index) => {
    if (typeof value === 'number') {
      return index === value;
    }
    return item.value === value;
  });

  const labelText = currentItem ? currentItem.label : "";
  const dynamicWidth = labelText.length * 10 + 36;

  return (
    <div className={styles.selectorContainer}>
      <select
        className={styles.select}
        value={value}
        onChange={(e) => onChange(e.target.selectedIndex)}
        style={{
          backgroundImage: `url(${iconUrl})`,
          width: `${dynamicWidth}px`
        }}
      >
        {valueList.map((item, index) => {
          const val = typeof value === 'number' ? index : item.value;
          return (
            <option key={index} value={val}>
              {item.label}
            </option>
          );
        })}
      </select>
    </div>
  );
}

export default NewsOptionSelector;
