<template>
  <div class="h-100">
    <div class="d-flex h-100">
      <GuideMenu
        :channels="channels"
        :subscription-count="subscriptionCount"
        class="guide-menu"
        @update:channels="updateChannels"
      ></GuideMenu>
      <div class="flex-fill main">
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
    },
    subscriptionCount: {
      default: 0,
      type: Number
    }
  },
  components: {
    GuideMenu
  },
  methods: {
    updateChannels(channels) {
      this.$emit("update:channels", channels);
    }
  }
};
</script>

<style>
.guide-menu {
  width: 240px;
}

.main {
  background: rgb(249, 249, 249);
}
</style>

