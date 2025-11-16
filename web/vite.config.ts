import { fileURLToPath, URL } from 'node:url';
import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';

// 统一使用中文注释，便于审阅
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const backend = env.VITE_BACKEND || 'http://localhost:8080';
  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    server: {
      host: '0.0.0.0',
      port: 5173,
      proxy: {
        '/admin': {
          target: backend,
          changeOrigin: true
        },
        '/ai': {
          target: backend,
          changeOrigin: true
        },
        '/file': {
          target: backend,
          changeOrigin: true
        },
        '/portal': {
          target: backend,
          changeOrigin: true
        }
      }
    }
  };
});
