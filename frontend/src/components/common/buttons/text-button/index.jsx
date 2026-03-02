import styles from './style.module.css'
import { kebabToPascal } from '../../../../utils/stringConversion';

function TextButton({ isActive, fontStyle, borderStyle, fontContent }) {

  const buttonClasses = [
    styles.btnBase,
    isActive ? styles.btnActive : styles.btnInactive,
    styles[`btn${kebabToPascal(fontStyle)}`],
    styles[`btnBorder${kebabToPascal(borderStyle)}`]
  ].filter(Boolean).join(' ');

  return (
    <div>
      <button 
        className={buttonClasses}
      >
        {fontContent}
      </button>
    </div>
  );
}

export default TextButton;
