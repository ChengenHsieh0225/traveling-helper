import { useLocation } from "react-router-dom";

import styles from "./style.module.css";
import IconButton from "../buttons/icon-button";

function Header({ isLoggedIn, isLightMode }) {

  const location = useLocation();

  const pageTitles = {
    '/currency': '匯率查詢',
    '/weather': '天氣查詢',
    '/news': '近期新聞',
    '/locked': '此頁尚未開放'
  }

  return (
    <div className={styles.headerContainer}>
      <p className="body-bold">{pageTitles[location.pathname]}</p>
      <div className={styles.btnContainer}>
        <IconButton
          height="20px"
          isActive={isLoggedIn}
          borderStyle="square"
          imgSrcActive="/assets/other/login.svg"
          imgSrcInactive="/assets/other/logout.svg"
          noPadding="true"
        ></IconButton>
        <IconButton
          height="20px"
          isActive={isLightMode}
          borderStyle="square"
          imgSrcActive="/assets/other/sun.svg"
          imgSrcInactive="/assets/other/moon.svg"
          noPadding="true"
        ></IconButton>
      </div>
    </div>
  );
}

export default Header;