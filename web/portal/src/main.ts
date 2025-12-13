import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import './styles/global.scss';

// 中文注释：创建应用实例并挂载全局插件
const app = createApp(App);
app.use(ElementPlus);
app.use(createPinia());
app.use(router);
app.mount('#app');
