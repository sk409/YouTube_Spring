import ajax from "./ajax.js";
import ChannelScaffold from "./components/ChannelScaffold.vue";
import NavbarChannel from "./components/NavbarChannel.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";

new Vue({
    el: "#app",
    vuetify,
    components: {
        ChannelScaffold,
        NavbarChannel
    },
    data: {
        tabs: "tab-upload",
    }
})