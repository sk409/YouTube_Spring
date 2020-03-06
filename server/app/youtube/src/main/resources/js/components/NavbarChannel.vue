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
        <v-dialog width="60%" v-model="dialogVideo">
            <VideoForm :channel="channel"></VideoForm>
        </v-dialog>
    </v-app-bar>
</template>

<script>
import VideoForm from "./VideoForm.vue";
export default {
    props: {
        channel: {
            type: Object,
            required: true
        }
    },
    components: {
        VideoForm
    },
    data() {
        return {
            dialogVideo: false,
        }
    }
}
</script>