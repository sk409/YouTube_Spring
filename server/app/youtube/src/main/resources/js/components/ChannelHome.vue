<template>
  <div class="d-flex px-4 pt-3">
    <div class="left">
      <div class="subtitle-1 mb-4">人気のアップロード動画</div>
      <div
        v-for="popularVideo in showedPopularVideos"
        :key="popularVideo.id"
        class="d-flex mb-2"
        @click="$transition($routes.watch.base(popularVideo.uniqueId))"
      >
        <VideoThumbnail vertical :video="popularVideo" class="video-thumbnail-popular"></VideoThumbnail>
        <div class="ml-3">
          <div class="video-title-popular word-break-all">{{popularVideo.title | truncate(50)}}</div>
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
      <div ref="newVideosContainer" class="mt-3 p-relative">
        <div class="d-flex overflow-x-hidden w-100">
          <div
            ref="newVideos"
            v-for="(newVideo, index) in newVideos"
            :key="newVideo.id"
            :style="newVideoStyle"
            class="video-new"
            @click="$transition($routes.watch.base(newVideo.uniqueId))"
          >
            <VideoThumbnail
              ref="newVideoThumbnails"
              :resizing.sync="resizingNewVideoThumbnailArray[index]"
              :video="newVideo"
              class="video-thumbnail-new"
            ></VideoThumbnail>
            <div class="video-title-new">{{newVideo.title}}</div>
            <div class="caption">{{newVideo.views}}回視聴・{{newVideo.createdAt | dateAgo}}</div>
          </div>
        </div>
        <div v-show="newVideoPage !== 0" class="previous-new-video-button p-absolute">
          <v-btn fab small @click="showPreviousNewVideos">
            <v-icon>mdi-menu-left</v-icon>
          </v-btn>
        </div>
        <div v-show="moreNextVideos" class="next-new-video-button p-absolute">
          <v-btn fab small @click="showNextNewVideos">
            <v-icon>mdi-menu-right</v-icon>
          </v-btn>
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

const newVideoDisplayCount = 4;

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
      newVideoPage: 0,
      newVideoSlideWidth: 0,
      resizingNewVideoThumbnailArray: [],
      showingAllPopularVideos: false
    };
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
      return this.showingAllPopularVideos
        ? this.popularVideos
        : this.popularVideos.slice(0, newVideoDisplayCount);
    }
  },
  mounted() {
    this.setupNewVideos();
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
};
</script>

<style>
.left {
  width: 85%;
}

.next-new-video-button,
.previous-new-video-button {
  top: 25%;
}

.next-new-video-button {
  right: 0;
  transform: translateX(30%);
}

.previous-new-video-button {
  left: 0;
  transform: translateX(-30%);
}

.right {
  width: 15%;
}

.video-new {
  transition: all 0.5s;
  width: 25%;
}

.video-overview {
  color: rgb(100, 100, 100);
}

.video-title-new {
  font-size: 20;
  font-weight: 400;
}

.video-title-popular {
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