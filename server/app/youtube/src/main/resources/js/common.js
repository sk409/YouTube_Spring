import Vue from "vue";
import { constans, routes, serverUrl, transition, uuid } from "./utils.js";

Vue.prototype.$constans = constans;
Vue.prototype.$routes = routes;
Vue.prototype.$serverUrl = serverUrl;
Vue.prototype.$transition = transition;
Vue.prototype.$uuid = uuid;

Vue.filter("date", (str, format) => {
  const date = new Date(str);
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const d = date.getDate();
  return `${year}/${month}/${d}`;
});

Vue.filter("default", (str, d) => {
  return !str || str.length === 0 ? d : str;
});

Vue.filter("percentage", str => {
  if (!str || str.length === 0 || typeof str !== "number") {
    return str;
  }
  return Math.round(str * 100) + "%";
});

Vue.filter("truncate", (str, maxLength, suffix = "...") => {
  if (str.length <= maxLength) {
    return str;
  }
  const sub = str.substring(0, maxLength);
  return sub + suffix;
});
