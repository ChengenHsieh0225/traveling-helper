import styles from "./style.module.css";
import IconButton from "../buttons/icon-button";

function Header({ pageTitle, isLoggedIn, isLightMode }) {

  return (
    <div className={styles.headerContainer}>
      <p className="body-bold">{pageTitle}</p>
      <div className={styles.btnContainer}>
        <IconButton
          height="20px"
          isActive={isLoggedIn}
          borderStyle="square"
          imgSrcActive="/assets/login.svg"
          imgSrcInactive="/assets/logout.svg"
          noPadding="true"
        ></IconButton>
        <IconButton
          height="20px"
          isActive={isLightMode}
          borderStyle="square"
          imgSrcActive="/assets/sun.svg"
          imgSrcInactive="/assets/moon.svg"
          noPadding="true"
        ></IconButton>
      </div>
    </div>
  );
}

export default Header;