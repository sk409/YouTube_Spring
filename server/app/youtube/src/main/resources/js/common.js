import Vue from "vue";
import {routes, serverUrl, transition, uuid} from "./utils.js";

Vue.prototype.$routes = routes;
Vue.prototype.$serverUrl = serverUrl;
Vue.prototype.$transition = transition;
Vue.prototype.$uuid = uuid;