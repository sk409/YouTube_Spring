<template>
  <div ref="guideScaffold" class="d-flex h-100">
    <GuideMenu class="guide-menu" :style="menuStyle"></GuideMenu>
    <div class="main">
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
      menuStyle: {}
    };
  },
  mounted() {
    const menuHeight = this.$refs.guideScaffold.clientHeight + "px";
    this.menuStyle = {
      height: menuHeight
    };
  }
};
</script>

<style>
.guide-menu {
  width: 20%;
}

.main {
  background: rgb(249, 249, 249);
  width: 80%;
}
</style>

