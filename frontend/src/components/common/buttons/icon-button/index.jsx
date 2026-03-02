import styles from './style.module.css'
import { kebabToPascal } from '../../../../utils/stringConversion';

function IconButton({ height, isActive, borderStyle, imgSrcActive, imgSrcInactive }) {

  const buttonClasses = [
    styles.btnBase,
    isActive ? styles.btnActive : styles.btnInactive,
    styles[`btnBorder${kebabToPascal(borderStyle)}`]
  ].filter(Boolean).join(' ');

  const imgSrc = isActive ? imgSrcActive : imgSrcInactive;

  // console.log(buttonClasses);

  return (
    <div>
      <button className={buttonClasses} style={{ height: height}}>
        <div className={styles.imgContainer}>
          <img className={styles.imgBase} src={imgSrc}></img>
        </div>
      </button>
    </div>
  );
}

export default IconButton;
