import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import '@/styles/index.scss';
import App from './App.vue';
import router from './router';
import pinia from './store';
import { registerGuardDirective } from './utils/guard';

// 创建根实例，注入 Pinia、路由与 Element Plus
const app = createApp(App);
app.use(pinia);
app.use(router);
app.use(ElementPlus);

// 注册按钮级别的权限指令
registerGuardDirective(app);

app.mount('#app');
