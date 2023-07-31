import { createApp } from 'vue';
import ArcoVue, { Message } from '@arco-design/web-vue';
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import 'tailwindcss/tailwind.css';
import globalComponents from '@/components';
import router from './router';
import store from './store';
import i18n from './locale';
import directive from './directive';
import './mock';
import App from './App.vue';
import '@arco-themes/vue-hsbr/css/arco.css';
import '@/assets/style/highlight.less';
import '@/assets/style/github-markdown.less';
import 'katex/dist/katex.min.css';
import '@/assets/style/global.less';
import '@/api/interceptor';

const app = createApp(App);

app.use(ArcoVue, {});
app.use(ArcoVueIcon);
// eslint-disable-next-line no-underscore-dangle
Message._context = app._context;

app.use(router);
app.use(store);
app.use(i18n);
app.use(globalComponents);
app.use(directive);

app.mount('#app');
