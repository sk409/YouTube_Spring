<template>
  <div class="d-flex px-2 pt-3">
    <div class="left">
      <div class="subtitle-1 mb-4">人気のアップロード動画</div>
      <div
        v-for="popularVideo in showedPopularVideos"
        :key="popularVideo.id"
        class="d-flex mb-2"
        @click="$transition($routes.watch(popularVideo.uniqueId))"
      >
        <VideoThumbnail vertical :video="popularVideo" class="video-thumbnail-popular"></VideoThumbnail>
        <div class="ml-3">
          <div class="video-title word-break-all">{{popularVideo.title | truncate(50)}}</div>
          <div class="caption">{{popularVideo.views}}回視聴・{{popularVideo.createdAt | dateAgo}}</div>
          <div class="mt-4 video-overview word-break-all">{{popularVideo.overview | truncate(100)}}</div>
        </div>
      </div>
      <div
        v-if="!showingAllPopularVideos"
        class="caption cursor-pointer mt-4"
        @click="showingAllPopularVideos=true"
      >もっと見る</div>
      <v-divider class="my-5"></v-divider>
      <div class="subtitle-1">アップロード動画</div>
      <div ref="newVideosContainer" class="d-flex overflow-x-hidden">
        <div
          ref="newVideos"
          v-for="(newVideo, index) in newVideos"
          :key="newVideo.id"
          class="video-new"
        >
          <VideoThumbnail
            ref="newVideoThumbnails"
            :resizing.sync="resizingNewVideoThumbnailArray[index]"
            :video="newVideo"
            class="video-thumbnail-new"
          ></VideoThumbnail>
        </div>
      </div>
      <v-divider class="my-5"></v-divider>
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
    newVideos: {
      default: () => [],
      type: Array
    },
    popularVideos: {
      default: () => [],
      type: Array
    }
  },
  components: {
    VideoThumbnail
  },
  data() {
    return {
      resizingNewVideoThumbnailArray: [
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false
      ],
      showingAllPopularVideos: false
    };
  },
  computed: {
    showedPopularVideos() {
      return this.showingAllPopularVideos
        ? this.popularVideos
        : this.popularVideos.slice(0, 4);
    }
  },
  mounted() {
    this.setupNewVideos();
  },
  methods: {
    setupNewVideos() {
      const containerWidth = this.$refs.newVideosContainer.clientWidth;
      const thumbnailWidth = containerWidth * 0.23;
      const marginRight = containerWidth * 0.02;
      this.$refs.newVideos.forEach(newVideo => {
        newVideo.style.width = thumbnailWidth + "px";
        newVideo.style.marginRight = marginRight + "px";
      });
      this.$refs.newVideoThumbnails.forEach((newVideoThumbnail, index) => {
        newVideoThumbnail.$el.style.width = thumbnailWidth + "px";
        this.$set(this.resizingNewVideoThumbnailArray, index, true);
      });
    }
  }
};
</script>

<style>
.left {
  width: 85%;
}

.right {
  width: 15%;
}

.video-new {
  width: 25%;
}

.video-overview {
  color: rgb(100, 100, 100);
}

.video-title {
  font-size: 24;
  font-weight: 500;
}

.video-thumbnail-new {
  width: 98%;
}

.video-thumbnail-popular {
  height: 160px;
}
</style>