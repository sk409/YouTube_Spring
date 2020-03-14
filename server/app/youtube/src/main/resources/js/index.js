import GuideScaffold from "./components/GuideScaffold.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import VideoCard from "./components/VideoCard.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js"

new Vue({
    el: "#app",
    vuetify,
    components: {
        GuideScaffold,
        NavbarSearch,
        VideoCard
    },
    data() {
        return {
            recommendedVideos: null,
            subscriptionChannels: null,
            user: null
        }
    },
    mounted() {
        const recommendedVideosJSON = this.$refs.recommendedVideos.textContent;
        this.recommendedVideos = recommendedVideosJSON ? JSON.parse(recommendedVideosJSON) : [];
        const subscriptionChannelsJSON = this.$refs.subscriptionChannels.textContent;
        this.subscriptionChannels = subscriptionChannelsJSON ? JSON.parse(subscriptionChannelsJSON) : [];
        const userJSON = this.$refs.user.textContent;
        this.user = userJSON ? JSON.parse(userJSON) : null;
    }
});