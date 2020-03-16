import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Buefy from 'buefy'
import 'buefy/dist/buefy.css'
import './bs_import.scss';

Vue.config.productionTip = false;
Vue.use(Buefy, {
  defaultIconPack: 'fas',
  defaultFieldLabelPosition: "on-border",
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
