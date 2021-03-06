import ajax from "./ajax.js";
import ChannelUnsubscribeForm from "./components/ChannelUnsubscribeForm.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import ScrollView from "./components/ScrollView.vue";
import SnackbarView from "./components/SnackbarView.vue";
import VideoComment from "./components/VideoComment.vue";
import VideoCommentForm from "./components/VideoCommentForm.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";
import "../sass/watch.scss";

new Vue({
    el: "#app",
    vuetify,
    components: {
        ChannelUnsubscribeForm,
        NavbarSearch,
        ScrollView,
        SnackbarView,
        VideoComment,
        VideoCommentForm
    },
    data: {
        dialogChannelUnsubscribeForm: false,
        fetchingNextComment: false,
        notification: "",
        snackbar: false,
        userRating: null,
        userSubscriber: null,
        video: null
    },
    computed: {
        barHighRatingStyle() {
            const highRatingCount = this.highRatingCount;
            const lowRatingCount = this.lowRatingCount;
            if (highRatingCount === 0 && lowRatingCount === 0) {
                return {
                    width: "50%"
                };
            }
            const percentage = Math.round(highRatingCount / (highRatingCount + lowRatingCount) * 100);
            return {
                width: percentage + "%"
            };
        },
        fetchedAllComments() {
            return this.video.commentCount === this.video.comments.length;
        },
        highRatingCount() {
            const userRating = this.userRating && this.userRating.ratingId === this.$constants.highRatingId ? 1 : 0;
            return userRating + this.video.highRatingCount;
        },
        highRatingStyle() {
            if (!this.userRating) {
                return {};
            }
            return this.userRating.ratingId === this.$constants.highRatingId ? {
                color: this.$vuetify.theme.currentTheme.success
            } : {};
        },
        lowRatingCount() {
            const userRating = this.userRating && this.userRating.ratingId === this.$constants.lowRatingId ? 1 : 0;
            return userRating + this.video.lowRatingCount;
        },
        lowRatingStyle() {
            if (!this.userRating) {
                return {};
            }
            return this.userRating.ratingId === this.$constants.lowRatingId ? {
                color: this.$vuetify.theme.currentTheme.success
            } : {};
        }
    },
    mounted() {
        const video = JSON.parse(this.$refs.video.textContent);
        video.comments = [];
        this.video = video;
        this.userRating = this.$refs.userRating.textContent ? JSON.parse(this.$refs.userRating.textContent) : null;
        this.userSubscriber = this.$refs.userSubscriber.textContent ? JSON.parse(this.$refs.userSubscriber.textContent) : null;
        this.fetchNextComments();
    },
    methods: {
        clickRating(ratingId) {
            if (this.userRating && this.userRating.ratingId !== ratingId) {
                const data = {
                    videoId: this.video.id
                };
                ajax.delete(this.$routes.videoRating.base, data).then(() => {
                    this.giveRating(ratingId);
                });
            } else {
                this.giveRating(ratingId);
            }
        },
        clickHighRating() {
            this.clickRating(this.$constants.highRatingId);
        },
        clickLowRating() {
            this.clickRating(this.$constants.lowRatingId);
        },
        createdVideoComment() {
            this.notification = "コメントを公開しました。";
            this.snackbar = true;
        },
        fetchNextComments() {
            if (this.fetchedAllComments || this.fetchingNextComment) {
                return;
            }
            this.fetchingNextComment = true;
            const data = {
                videoId: this.video.id,
                exclude: this.video.comments.map(videoComment => videoComment.id),
                limit: 5
            };
            ajax.get(this.$routes.videoComments.nextComments, data).then(response => {
                this.video.comments = this.video.comments.concat(response.data);
                this.fetchingNextComment = false;
            });
        },
        giveRating(ratingId) {
            if (this.userRating && this.userRating.ratingId === ratingId) {
                const data = {
                    videoId: this.video.id
                };
                this.userRating = null;
                ajax.delete(this.$routes.videoRating.base, data);
            } else {
                const data = {
                    videoId: this.video.id,
                    ratingId: ratingId
                };
                ajax.post(this.$routes.videoRating.base, data).then(response => {
                    this.userRating = response.data;
                });
            }
        },
        subscribe() {
            const data = {
                channelId: this.video.channel.id
            };
            this.video.channel.subscriberCount += 1;
            ajax.post(this.$routes.subscribers.base, data).then(response => {
                this.notification = "登録チャンネルに追加しました";
                this.snackbar = true;
                this.userSubscriber = response.data;
            });
        },
        unsubscribed() {
            this.video.channel.subscriberCount -= 1;
            this.notification = "登録チャンネルから削除しました";
            this.snackbar = true;
            this.dialogChannelUnsubscribeForm = false;
            this.userSubscriber = null;
        }
    }
});