import NavbarSearch from "./components/NavbarSearch.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";
import "../sass/watch.scss";

new Vue({
    el: "#app",
    vuetify,
    components: {
        NavbarSearch
    },
});