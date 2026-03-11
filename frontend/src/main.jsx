import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "./index.css";
import App from "./App.jsx";

import { registerSW } from "virtual:pwa-register";

if (import.meta.env.PROD) {
  registerSW({
    onNeedRefresh() {
      if (confirm('有新版本內容，是否立即更新？')) {
        window.location.reload();
      }
    },
    onOfflineReady() {
      console.log('App 已準備好離線使用！');
    },
  });
}

const queryClient = new QueryClient();

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </QueryClientProvider>
  </StrictMode>
);
