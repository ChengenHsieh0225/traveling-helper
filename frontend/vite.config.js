import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
   registerType: 'prompt',
   /// To test the SW functionality in dev environment
   includeAssets: ['favicon.ico', 'apple-touch-icon.png', 'mask-icon.svg'],
   devOptions: {
     enabled: true,
     type: 'module',
     navigateFallback: 'index.html',
     suppressWarnings: true
   },
   manifest: {
     name: 'Traveling Helper',
     short_name: 'TravelHelper',
     theme_color: '#ffffff',
     display: 'standalone',
     scope: '/',
     start_url: '/',
     icons: [
      {
        src: 'pwa/pwa-192x192.png',
        sizes: '192x192',
        type: 'image/png'
      },
      {
        src: 'pwa/pwa-512x512.png',
        sizes: '512x512',
        type: 'image/png'
      },
      {
        src: 'pwa/pwa-512x512.png',
        sizes: '512x512',
        type: 'image/png',
        purpose: 'any maskable' // 讓圖示在 Android 上能適應不同形狀
      }
    ]
   },
   workbox: {
    // 讓 PWA 自動快取打包後的靜態檔案（html, js, css）
    globPatterns: ['**/*.{js,css,html,ico,png,svg}'],
    // 針對新聞圖片可以設定緩存，避免重複下載
    runtimeCaching: [
      {
        urlPattern: /\.(?:png|jpg|jpeg|svg)$/,
        handler: 'CacheFirst',
        options: {
          cacheName: 'images',
          expiration: { maxEntries: 50 } // 最多快取 50 張
        }
      }
    ]
  }
 })
  ],
})
