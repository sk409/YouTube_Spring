<template>
  <div class="h-100">
    <div class="d-flex h-100">
      <GuideMenu :channels="channels" class="guide-menu"></GuideMenu>
      <div class="flex-fill">
        <slot name="content"></slot>
      </div>
    </div>
  </div>
</template>

<script>
import GuideMenu from "./GuideMenu.vue";
export default {
  props: {
    channels: {
      default: () => [],
      type: Array
    }
  },
  components: {
    GuideMenu
  }
};
</script>

<style>
.guide-menu {
  width: 240px;
  border-right: 1px solid lightgray;
}
</style>

