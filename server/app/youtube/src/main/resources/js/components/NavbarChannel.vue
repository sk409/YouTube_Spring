<template>
    <v-app-bar>
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
        <v-spacer></v-spacer>
        <input type="text" placeholder="チャンネル内で検索">
        <v-spacer></v-spacer>
        <v-btn color="grey" depressed outlined @click="dialogVideo=true">
            <v-icon class="primary--text">mdi-video-plus</v-icon>
            <span class="black--text ml-1">作成</span>
        </v-btn>
        <v-dialog width="70%" v-model="dialogVideo">
            <VideoForm :channel="channel" @created="createdVideo"></VideoForm>
        </v-dialog>
        <SnackbarView :message="notification" :visible.sync="snackbar"></SnackbarView>
    </v-app-bar>
</template>

<script>
import SnackbarView from "./SnackbarView.vue";
import VideoForm from "./VideoForm.vue";
export default {
    props: {
        channel: {
            type: Object,
            required: true
        }
    },
    components: {
        SnackbarView,
        VideoForm
    },
    data() {
        return {
            dialogVideo: false,
            notification: "",
            snackbar: false,
        }
    },
    methods: {
        createdVideo(video) {
            this.dialogVideo = false;
            this.notification = `動画「${video.title}」を投稿しました`;
            this.snackbar = true;
            this.$emit("created:video", video);
        }
    }
}
</script>