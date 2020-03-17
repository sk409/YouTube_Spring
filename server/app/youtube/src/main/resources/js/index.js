import GuideScaffold from "./components/GuideScaffold.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import VideoCard from "./components/VideoCard.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";

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
      recommendedVideos: null
    };
  },
  mounted() {
    const recommendedVideosJSON = this.$refs.recommendedVideos.textContent;
    this.recommendedVideos = recommendedVideosJSON ?
      JSON.parse(recommendedVideosJSON) :
      [];

    // //
    // const path = this.$serverUrl("videos/video1.m3u8");
    // var video = document.getElementById("v");
    // if (video.canPlayType("application/vnd.apple.mpegurl")) {
    //   video.src = path;
    //   return;
    // }
    // if (Hls.isSupported()) {
    //   var hls = new Hls();
    //   hls.loadSource(path);
    //   hls.attachMedia(video);
    // }
    // return;
    // //
  }
});