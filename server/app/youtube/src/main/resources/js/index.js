import NavbarIndex from "./components/NavbarIndex.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js"

new Vue({
    el: "#app",
    vuetify,
    components: {
        NavbarIndex
    },
});