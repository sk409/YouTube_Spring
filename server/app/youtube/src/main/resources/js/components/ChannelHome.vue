<template>
  <div class="d-flex px-2 pt-3">
    <div class="left">
      <div class="subtitle-1 mb-4">人気のアップロード動画</div>
      <div
        v-for="popularVideo in popularVideos"
        :key="popularVideo.id"
        class="d-flex mb-2"
        @click="$transition($routes.watch(video.uniqueId))"
      >
        <VideoThumbnail vertical :video="popularVideo" class="video-thumbnail"></VideoThumbnail>
        <div class="ml-3">
          <div class="video-title word-break-all">{{popularVideo.title | truncate(50)}}</div>
          <div class="caption">{{popularVideo.views}}回視聴・{{popularVideo.createdAt | dateAgo}}</div>
          <div class="mt-4 video-overview word-break-all">{{popularVideo.overview | truncate(100)}}</div>
        </div>
      </div>
    </div>
    <div class="right">
      <div>おすすめチャンネル</div>
    </div>
  </div>
</template>

<script>
import VideoThumbnail from "./VideoThumbnail.vue";
export default {
  props: {
    channel: {
      type: Object,
      required: true
    },
    popularVideos: {
      default: () => [],
      type: Array
    }
  },
  components: {
    VideoThumbnail
  },
  methods: {}
};
</script>

<style>
.left {
  width: 70%;
}

.right {
  width: 30%;
}

.video-overview {
  color: rgb(100, 100, 100);
}

.video-title {
  font-size: 24;
  font-weight: 500;
}

.video-thumbnail {
  height: 160px;
}
</style>