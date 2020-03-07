import ChannelScaffold from "./components/ChannelScaffold.vue";
import NavbarChannel from "./components/NavbarChannel.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";
import "../sass/channels.videos.upload.scss";

new Vue({
    el: "#app",
    vuetify,
    components: {
        ChannelScaffold,
        NavbarChannel
    },
    data: {
        tabs: "tab-upload",
        videos: [],
    },
    mounted() {
        this.videos = JSON.parse(this.$refs.videos.textContent);
    },
    methods: {
        createdVideo() {
            location.href = location.href;
        }
    }
})