<template>
  <div ref="guideScaffold" class="d-flex h-100">
    <GuideMenu class="guide-menu" :style="menuStyle"></GuideMenu>
    <div class="content overflow-y-auto" :style="contentStyle">
      <slot name="content"></slot>
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
      menuStyle: {}
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

