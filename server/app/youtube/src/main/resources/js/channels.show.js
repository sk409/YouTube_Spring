import ajax from "./ajax.js";
import ChannelHome from "./components/ChannelHome.vue";
import ChannelUnsubscribeForm from "./components/ChannelUnsubscribeForm.vue";
import ChannelVideos from "./components/ChannelVideos.vue";
import GuideScaffold from "./components/GuideScaffold.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import SnackbarView from "./components/SnackbarView.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";

new Vue({
    el: "#app",
    vuetify,
    components: {
        ChannelHome,
        ChannelUnsubscribeForm,
        ChannelVideos,
        GuideScaffold,
        NavbarSearch,
        SnackbarView
    },
    data() {
        return {
            channel: null,
            dialogChannelUnsubscribeForm: false,
            newVideos: [],
            notification: "",
            popularVideos: [],
            snackbar: false,
            tab: "",
            tabs: ["ホーム", "動画", "再生リスト", "コミュニティ", "チャンネル", "概要"],
            userSubscriber: null
        }
    },
    mounted() {
        const channelJSON = this.$refs.channel.textContent;
        this.channel = channelJSON ? JSON.parse(channelJSON) : null;
        const newVideosJSON = this.$refs.newVideos.textContent;
        this.newVideos = newVideosJSON ? JSON.parse(newVideosJSON) : [];
        //console.log(this.newVideos);
        const popularVideosJSON = this.$refs.popularVideos.textContent;
        this.popularVideos = popularVideosJSON ? JSON.parse(popularVideosJSON) : [];
        const userSubscriberJSON = this.$refs.userSubscriber.textContent;
        this.userSubscriber = userSubscriberJSON ? JSON.parse(userSubscriberJSON) : null;
    },
    methods: {
        subscribe() {
            const data = {
                channelId: this.channel.id
            };
            this.channel.subscriberCount += 1;
            ajax.post(this.$routes.subscribers.base, data).then(response => {
                this.notification = "登録チャンネルに追加しました";
                this.snackbar = true;
                this.userSubscriber = response.data;
            });
        },
        unsubscribed() {
            this.channel.subscriberCount -= 1;
            this.notification = "登録チャンネルから削除しました";
            this.snackbar = true;
            this.dialogChannelUnsubscribeForm = false;
            this.userSubscriber = null;
        }
    }
});