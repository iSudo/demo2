import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import axios from "axios";
import vuetify from "./plugins/vuetify";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080"
});

Vue.prototype.$http = axiosInstance;

Vue.config.productionTip = false;

new Vue({
  router,
  vuetify,
  render: h => h(App)
}).$mount("#app");
