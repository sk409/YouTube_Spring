import _ from "lodash";
import ChannelScaffold from "./components/ChannelScaffold.vue";
import NavbarChannel from "./components/NavbarChannel.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";
import "../sass/channels.videos.upload.scss";

new Vue({
  el: "#app",
  vuetify,
  components: {
    ChannelScaffold,
    NavbarChannel
  },
  data: {
    tabs: "tab-upload",
    videos: []
  },
  mounted() {
    const videos = JSON.parse(this.$refs.videos.textContent);
    const videoCommentCounts = _.groupBy(
      JSON.parse(this.$refs.videoCommentCounts.textContent),
      "videoId"
    );
    const videoRatingCounts = _.groupBy(
      JSON.parse(this.$refs.videoRatingCounts.textContent),
      "videoId"
    );
    videos.forEach(video => {
      const videoCommentCount = videoCommentCounts[video.id];
      video.commentCount = videoCommentCount ? videoCommentCount[0].count : 0;
      const videoRatingCount = videoRatingCounts[video.id];
      if (!videoRatingCount) {
        return;
      }
      let sum = 0;
      const highRatingCount = videoRatingCount[this.$constans.highRatingId];
      if (highRatingCount) {
        sum += highRatingCount.count;
      }
      const lowRatingCount = videoRatingCount[this.$constans.lowRatingId];
      if (lowRatingCount) {
        sum += lowRatingCount.count;
      }
      if (sum !== 0) {
        video.highRatingRate = highRatingCount.count / sum;
      }
    });
    this.videos = videos;
  },
  methods: {
    createdVideo() {
      location.href = location.href;
    }
  }
});
