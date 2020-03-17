<template>
  <div class="d-grid grid-columns-5">
    <div v-for="video in videos" :key="video.id">
      <VideoThumbnail :video="video" class="mx-2 my-3"></VideoThumbnail>
      <div>{{video.title | truncate(30)}}</div>
      <div class="caption mt-3">{{video.views}}回視聴・{{video.createdAt | dateAgo}}</div>
    </div>
  </div>
</template>

<script>
import ajax from "../ajax.js";
import VideoThumbnail from "./VideoThumbnail.vue";
export default {
  props: {
    channel: {
      type: Object,
      required: true
    },
    fetching: {
      default: false,
      type: Boolean
    }
  },
  components: {
    VideoThumbnail
  },
  data() {
    return {
      videos: []
    };
  },
  created() {
    this.fetchVideos();
  },
  watch: {
    fetching() {
      if (!this.fetching) {
        return;
      }
      this.fetchVideos();
    }
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
      ajax.get(this.$routes.videos.newChannel, data).then(response => {
        this.videos = this.videos.concat(response.data);
      });
    },
    fetchVideosOld() {},
    fetchVideosPopular() {}
  }
};
</script>