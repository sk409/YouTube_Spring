import Vue from "vue";
import {
    constants,
    routes,
    serverUrl,
    transition,
    uuid
} from "./utils.js";

Vue.prototype.$constants = constants;
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

Vue.filter("dateAgo", str => {
    const date = new Date(str);
    const now = new Date();
    const milliseconds = now - date;
    if (milliseconds < 1000) {
        return "0秒前";
    }
    const seconds = Math.floor(milliseconds / 1000);
    if (seconds < 60) {
        return `${seconds}秒前`;
    }
    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) {
        return `${minutes}分前`;
    }
    const hours = Math.floor(minutes / 60);
    if (hours < 24) {
        return `${hours}時間前`;
    }
    const days = Math.floor(hours / 24);
    if (days < 7) {
        return `${days}日前`;
    }
    const weeks = Math.floor(days / 7);
    if (weeks < 4) {
        return `${weeks}週間前`;
    }
    const months = Math.floor(weeks / 4);
    if (months < 12) {
        return `${months}ヵ月前`;
    }
    const years = Math.floor(months / 12);
    return `${years}年前`;
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

Vue.filter("timeColonSeconds", str => {
    const padding = t => t < 10 ? "0" + t : t;
    const seconds = Math.round(str);
    if (seconds < 60) {
        return `00:${padding(seconds)}`;
    }
    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) {
        return `${padding(minutes)}:${padding(seconds % 60)}`;
    }
    const hours = Math.floor(minutes / 60);
    return `${padding(hours)}:${padding(minutes % 60)}:${padding(seconds % 3600)}`;
});

Vue.filter("truncate", (str, maxLength, suffix = "...") => {
    if (str.length <= maxLength) {
        return str;
    }
    const sub = str.substring(0, maxLength);
    return sub + suffix;
});