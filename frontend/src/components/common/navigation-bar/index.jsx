import { useLocation, useNavigate } from "react-router-dom";

import styles from "./style.module.css";
import IconButton from "../buttons/icon-button";

function NavigationBar() {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div className={styles.outerContainer}>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          isActive={location.pathname === '/currency'}
          borderStyle="round"
          imgSrcActive="/assets/other/currency-filled.svg"
          imgSrcInactive="/assets/other/currency-outlined.svg"
          onClick={() => navigate('./currency')}
        ></IconButton>
        <p className="caption">匯率</p>
      </div>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          isActive={location.pathname === '/weather'}
          borderStyle="round"
          imgSrcActive="/assets/other/cloud-filled.svg"
          imgSrcInactive="/assets/other/cloud-outlined.svg"
          onClick={() => navigate('./weather')}
        ></IconButton>
        <p className="caption">天氣</p>
      </div>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          isActive={location.pathname === '/news'}
          borderStyle="round"
          imgSrcActive="/assets/other/news-filled.svg"
          imgSrcInactive="/assets/other/news-outlined.svg"
          onClick={() => navigate('./news')}
        ></IconButton>
        <p className="caption">新聞</p>
      </div>
      <div className={styles.navigationPillContainer}>
        <IconButton
          height="32px"
          isActive={location.pathname === '/locked'}
          borderStyle="round"
          imgSrcActive="/assets/other/lock-filled.svg"
          imgSrcInactive="/assets/other/lock-outlined.svg"
          onClick={() => navigate('./locked')}
        ></IconButton>
        <p className="caption">尚未開放</p>
      </div>
    </div>
  );
}

export default NavigationBar;
