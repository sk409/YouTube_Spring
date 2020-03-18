import Vue from "vue";
import vuetify from "./vuetify.js";

new Vue({
    el: "#app",
    vuetify,
    data() {
        return {
            video: null
        }
    },
    mounted() {
        const videoJSON = this.$refs.videoJSON.textContent;
        this.video = videoJSON ? JSON.parse(videoJSON) : null;
    }
});