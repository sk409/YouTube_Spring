import ajax from "./ajax.js";
import NavbarSearch from "./components/NavbarSearch.vue";
import VideoComment from "./components/VideoComment.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";
import "../sass/watch.scss";

new Vue({
    el: "#app",
    vuetify,
    components: {
        NavbarSearch,
        VideoComment
    },
    data: {
        highRating: null,
        lowRating: null,
        userRating: null,
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
        highRatingCount() {
            const userRating = this.userRating && this.userRating.ratingId === this.$constants.highRatingId ? 1 : 0;
            const highRatingCount = this.highRating ? this.highRating.count : 0;
            return userRating + highRatingCount;
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
            const lowRatingCount = this.lowRating ? this.lowRating.count : 0;
            return userRating + lowRatingCount;
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
        this.highRating = this.$refs.highRating.textContent ? JSON.parse(this.$refs.highRating.textContent) : null;
        this.lowRating = this.$refs.lowRating.textContent ? JSON.parse(this.$refs.lowRating.textContent) : null;
        this.userRating = this.$refs.userRating.textContent ? JSON.parse(this.$refs.userRating.textContent) : null;
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
        fetchNextComments() {
            const data = {
                videoId: this.video.id,
                limit: 10
            };
            if (this.video.comments.length !== 0) {
                data.oldBefore = this.video.comments[this.video.comments.length - 1];
            }
            ajax.get(this.$routes.videoComments.nextComments, data).then(response => {
                this.video.comments = this.video.comments.concat(response.data);
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
        }
    }
});