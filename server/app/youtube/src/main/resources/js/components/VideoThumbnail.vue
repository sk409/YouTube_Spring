<template>
  <div ref="videoThumbnail" class="p-relative">
    <v-img ref="image" fit :src="$serverUrl(video.thumbnailPath)"></v-img>
    <div class="caption duration p-absolute">{{video.duration | timeColonSeconds}}</div>
  </div>
</template>

<script>
export default {
  props: {
    vertical: {
      type: Boolean,
      default: false
    },
    video: {
      type: Object,
      required: true
    }
  },
  mounted() {
    const videoThumbnail = this.$refs.videoThumbnail;
    const image = this.$refs.image;
    if (this.vertical) {
      const imageHeight = videoThumbnail.clientHeight;
      console.log("**");
      console.log(imageHeight);
      image.$el.style.height = imageHeight + "px";
      image.$el.style.width = (imageHeight * 16) / 9 + "px";
    } else {
      const imageWidth = videoThumbnail.clientWidth;
      image.$el.style.width = imageWidth;
      image.$el.style.height = (imageWidth * 9) / 16 + "px";
    }
  }
};
</script>

<style>
.duration {
  background: rgb(70, 69, 77);
  color: rgb(249, 249, 249);
  right: 4px;
  bottom: 4px;
}
</style>