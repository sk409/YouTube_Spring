import StudioScaffold from "./components/StudioScaffold.vue";
import NavbarChannel from "./components/NavbarChannel.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";
import "../sass/channels.videos.upload.scss";

new Vue({
    el: "#app",
    vuetify,
    components: {
        StudioScaffold,
        NavbarChannel
    },
    data: {
        channel: null,
        tabs: "tab-upload"
    },
    mounted() {
        this.channel = JSON.parse(this.$refs.channel.textContent);
    },
    methods: {
        createdVideo() {
            location.href = location.href;
        }
    }
});