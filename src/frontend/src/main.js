import { createApp } from 'vue';
import { createPinia } from "pinia";
import App from './App.vue';
import router from './router';

//import './assets/main.css'; getting rid 
//import '@coreui/coreui/dist/css/coreui.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'ant-design-vue/dist/antd.css';

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount('#app');
