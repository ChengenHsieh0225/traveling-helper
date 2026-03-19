import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "./index.css";
import App from "./App.jsx";

import { registerSW } from "virtual:pwa-register";

const updateSW = registerSW({
  onNeedRefresh() {
    // 改用更直覺的 UI，或者維持 confirm
    if (confirm('發現新版本！是否立即更新以獲得最新天氣資訊？')) {
      updateSW(true); // 關鍵：這會強制 SW 跳過等待並立刻更新
    }
  },
  onOfflineReady() {
    console.log('App 已準備好離線使用！');
  },
});

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
