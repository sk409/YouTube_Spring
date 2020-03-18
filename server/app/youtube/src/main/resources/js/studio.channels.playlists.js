import PlaylistForm from "./components/PlaylistForm.vue";
import SnackbarView from "./components/SnackbarView.vue";
import StudioScaffold from "./components/StudioScaffold.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";

new Vue({
    el: "#app",
    vuetify,
    components: {
        PlaylistForm,
        SnackbarView,
        StudioScaffold
    },
    data() {
        return {
            channel: null,
            dialogPlaylist: false,
            notification: "",
            snackbar: false,
        }
    },
    mounted() {
        const channelJSON = this.$refs.channelJSON.textContent;
        this.channel = channelJSON ? JSON.parse(channelJSON) : null;
    },
    methods: {
        createdPlaylist(playlist) {
            this.notification = `プレイリスト「${playlist.name}」を作成しました`;
            this.snackbar = true;
            setTimeout(() => {
                location.href = location.href;
            }, 1000);
        }
    }
})