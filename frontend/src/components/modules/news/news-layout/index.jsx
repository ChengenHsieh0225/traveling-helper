import { Outlet } from "react-router-dom";
import { NewsProvider } from "../../../../contexts/NewsContext";

function NewsLayout() {
  return (
    <NewsProvider>
      <Outlet />
    </NewsProvider>
  );
};

export default NewsLayout;