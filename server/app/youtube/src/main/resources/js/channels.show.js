import GuideScaffold from "./components/GuideScaffold.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";

new Vue({
    el: "#app",
    vuetify,
    components: {
        GuideScaffold,
        NavbarSearch
    }
});