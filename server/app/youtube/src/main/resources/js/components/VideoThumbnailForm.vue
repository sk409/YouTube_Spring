<template>
  <div ref="videoThumbnailForm">
    <div class="subtitle-1">サムネイル</div>
    <div class="caption">動画の内容がわかる画像を選択するかアップロードします。視聴者の目を引くサムネイルにしましょう。</div>
    <div class="d-flex justify-space-between mt-2">
      <div ref="uploader" class="d-flex align-center text-center thumbnail uploader">サムネイルをアップロード</div>
      <video
        v-for="oneBaseIndex in sampleCount"
        :key="oneBaseIndex"
        ref="videos"
        :class="{'selected-video': oneBaseIndex-1===selectedVideoIndex}"
        class="sample-video thumbnail"
        @click="clickVideo(oneBaseIndex-1)"
      ></video>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    file: {
      required: true,
      validator: v => typeof v === "object" || v === null
    }
  },
  data() {
    return {
      sampleCount: 3,
      selectedVideoIndex: 1
    };
  },
  watch: {
    file: {
      immediate: true,
      handler() {
        if (this.$refs.videos) {
          this.setThumbnails();
        } else {
          this.$nextTick(() => {
            this.setThumbnails();
          });
        }
      }
    }
  },
  methods: {
    clickVideo(index) {
      this.selectedVideoIndex = index;
      this.selectVideo();
    },
    setThumbnails() {
      const videos = this.$refs.videos;
      videos.forEach((video, index) => {
        video.src = URL.createObjectURL(this.file);
        video.load();
        video.onloadeddata = () => {
          video.currentTime = (video.duration * index) / videos.length;
          if (index === this.selectedVideoIndex) {
              video.onseeked = () => {
                  this.selectVideo();
              };
          }
        };
      });
    },
    selectVideo() {
      const video = this.$refs.videos[this.selectedVideoIndex];
      const canvas = document.createElement("canvas");
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      const context = canvas.getContext("2d");
      context.drawImage(video, 0, 0);
      canvas.toBlob(blob => {
          this.$emit("selected", new File([blob], "thumbnail.png"));
      });
    }
  }
};
</script>

<style>
.thumbnail {
  width: 24%;
}

.sample-video {
  box-sizing: content-box;
}

.selected-video {
  border: 2px solid black;
}

.uploader {
  border: 1px dashed lightgrey;
}
</style>