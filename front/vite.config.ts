import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import { pa } from 'element-plus/es/locales.mjs'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server:{
    proxy: {
      "/my-backend-api": {
        target: "http://localhost:8080",
        rewrite: (path: string) => path.replace(/^\/my-backend-api/, ''),

      },
    }
  }
})
