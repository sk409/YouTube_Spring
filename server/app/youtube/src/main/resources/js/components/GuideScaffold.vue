<template>
  <div ref="guideScaffold" class="d-flex h-100">
    <GuideMenu class="guide-menu" :style="menuStyle"></GuideMenu>
    <div class="content overflow-y-auto" :style="contentStyle" @scroll="scroll">
      <slot name="content" :scroll-bottom="scrollBottom"></slot>
    </div>
  </div>
</template>

<script>
import GuideMenu from "./GuideMenu.vue";
export default {
  components: {
    GuideMenu
  },
  data() {
    return {
      contentStyle: {},
      menuStyle: {},
      scrollBottom: false
    };
  },
  mounted() {
    const height = this.$refs.guideScaffold.clientHeight + "px";
    this.contentStyle = {
      height
    };
    this.menuStyle = {
      height
    };
  },
  methods: {
    scroll(e) {
      const diff = Math.abs(
        e.target.scrollHeight - e.target.clientHeight - e.target.scrollTop
      );
      this.scrollBottom = diff <= 0.5;
      if (this.scrollBottom) {
        this.$emit("scroll:bottom");
      }
    }
  }
};
</script>

<style>
.guide-menu {
  width: 20%;
}

.content {
  background: rgb(249, 249, 249);
  width: 80%;
}
</style>

