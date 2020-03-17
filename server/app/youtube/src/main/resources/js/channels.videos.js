import ajax from "./ajax.js";
import ChannelHeader from "./components/ChannelHeader.vue";
import GuideScaffold from "./components/GuideScaffold.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import VideoThumbnail from "./components/VideoThumbnail.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";

new Vue({
    el: "#app",
    vuetify,
    components: {
        ChannelHeader,
        GuideScaffold,
        NavbarSearch,
        VideoThumbnail
    },
    data() {
        return {
            channel: null,
            userSubscriber: null,
            videos: []
        }
    },
    computed: {
        moreNextVideos() {
            return (
                (this.newVideoPage + 1) * newVideoDisplayCount < this.newVideos.length
            );
        },
        newVideoStyle() {
            return {
                transform: `translateX(${-this.newVideoPage *
          this.newVideoSlideWidth *
          newVideoDisplayCount}px)`
            };
        },
        showedPopularVideos() {
            return this.showingAllPopularVideos ?
                this.popularVideos :
                this.popularVideos.slice(0, newVideoDisplayCount);
        },
        sortMenuItems() {
            if (!this.channel) {
                return [];
            }
            return [{
                    title: "人気の動画",
                    route: this.$routes.channels.videos.base(this.channel.uniqueId, "popular")
                },
                {
                    title: "追加日（古い順）",
                    route: this.$routes.channels.videos.base(this.channel.uniqueId, "old")
                },
                {
                    title: "追加日(新しい順)",
                    route: this.$routes.channels.videos.base(this.channel.uniqueId)
                }
            ];
        }
    },
    mounted() {
        const channelJSON = this.$refs.channelJSON.textContent;
        this.channel = channelJSON ? JSON.parse(channelJSON) : null;
        const videosJSON = this.$refs.videosJSON.textContent;
        this.videos = videosJSON ? JSON.parse(videosJSON) : [];
        const userSubscriberJSON = this.$refs.userSubscriberJSON.textContent;
        this.userSubscriber = userSubscriberJSON ? JSON.parse(userSubscriberJSON) : null;
    },
    methods: {
        fetchVideos() {
            this.fetchNewVideos();
        },
        fetchNewVideos() {
            const data = {
                channelId: this.channel.id,
                limit: 30
            };
            if (this.videos.length !== 0) {
                data.oldBeforeId = this.videos[this.videos.length - 1].id;
            }
            ajax
                .get(this.$routes.channels.videos.new(this.channel.id), data)
                .then(response => {
                    this.videos = this.videos.concat(response.data);
                });
        },
        fetchVideosOld() {},
        fetchVideosPopular() {}
    }
});