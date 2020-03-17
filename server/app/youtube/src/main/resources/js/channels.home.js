import ChannelHeader from "./components/ChannelHeader.vue";
import GuideScaffold from "./components/GuideScaffold.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import VideoThumbnail from "./components/VideoThumbnail.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";
import "../sass/channels.home.scss";

const newVideoDisplayCount = 4;

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
            newVideoPage: 0,
            newVideos: [],
            newVideoSlideWidth: 0,
            popularVideos: [],
            resizingNewVideoThumbnailArray: [],
            showingAllPopularVideos: false,
            userSubscriber: null
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
        }
    },
    mounted() {
        const channelJSON = this.$refs.channelJSON.textContent;
        this.channel = channelJSON ? JSON.parse(channelJSON) : null;
        const newVideosJSON = this.$refs.newVideosJSON.textContent;
        this.newVideos = newVideosJSON ? JSON.parse(newVideosJSON) : [];
        const popularVideosJSON = this.$refs.popularVideosJSON.textContent;
        this.popularVideos = popularVideosJSON ? JSON.parse(popularVideosJSON) : [];
        const userSubscriberJSON = this.$refs.userSubscriberJSON.textContent;
        this.userSubscriber = userSubscriberJSON ? JSON.parse(userSubscriberJSON) : null;
        this.$nextTick(() => {
            this.setupNewVideos();
        });
    },
    methods: {
        setupNewVideos() {
            this.$refs.newVideos.forEach(newVideo => {
                this.resizingNewVideoThumbnailArray.push(false);
            });
            const containerWidth = this.$refs.newVideosContainer.clientWidth;
            const thumbnailWidth = containerWidth * 0.24;
            const marginRight = containerWidth * 0.01;
            this.newVideoSlideWidth = thumbnailWidth + marginRight;
            this.$refs.newVideos.forEach(newVideo => {
                newVideo.style.width = thumbnailWidth + "px";
                newVideo.style.marginRight = marginRight + "px";
            });
            this.$refs.newVideoThumbnails.forEach((newVideoThumbnail, index) => {
                newVideoThumbnail.$el.style.width = thumbnailWidth + "px";
                this.$set(this.resizingNewVideoThumbnailArray, index, true);
            });
        },
        showNextNewVideos() {
            this.newVideoPage += 1;
        },
        showPreviousNewVideos() {
            this.newVideoPage -= 1;
        }
    }
});