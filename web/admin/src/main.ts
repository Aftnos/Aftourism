import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import zhCn from 'element-plus/es/locale/lang/zh-cn';
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
app.use(ElementPlus, { locale: zhCn });

// 注册按钮级别的权限指令
registerGuardDirective(app);

app.mount('#app');