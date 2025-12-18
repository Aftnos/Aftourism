import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import './styles/global.scss';
import { ApiError } from '@/services/http';

// 中文注释：创建应用实例并挂载全局插件
const app = createApp(App);
app.use(ElementPlus);
app.use(createPinia());
app.use(router);
app.mount('#app');

// 全局错误处理：统一提示并避免崩溃
import { ElMessage } from 'element-plus';
app.config.errorHandler = (err) => {
  // 401 错误已在 http 拦截器中处理，此处忽略
  if (err instanceof ApiError && err.code === 401) {
    return;
  }
  console.error(err);
  ElMessage.error('出现异常，请稍后重试');
};
