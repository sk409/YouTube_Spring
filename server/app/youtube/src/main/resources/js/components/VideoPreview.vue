<template>
  <div>
    <video ref="video" class="w-100"></video>
    <div class="grey lighten-3">
      <div>
        <div>動画リンク</div>
        <div class="link">{{link}}</div>
      </div>
      <div class="mt-3">
        <div>ファイル名</div>
        <div>{{filename}}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    file: {
      required: true,
      validator: v => typeof v === "object" || v === null
    },
    link: {
      type: String,
      default: ""
    }
  },
  computed: {
    filename() {
      return this.file ? this.file.name : "";
    }
  },
  watch: {
    file: {
      immediate: true,
      handler() {
        this.$nextTick(() => {
          const video = this.$refs.video;
          video.src = URL.createObjectURL(this.file);
          video.load();
        });
      }
    }
  }
};
</script>