<template>
  <div ref="studioScaffold" class="h-100">
    <div v-if="channel" class="d-flex h-100">
      <StudioMenu :channel="channel" class="h-100"></StudioMenu>
      <div class="flex-fill overflow-y-auto" :style="contentStyle">
        <slot name="content"></slot>
      </div>
    </div>
  </div>
</template>

<script>
import StudioMenu from "./StudioMenu.vue";
export default {
  props: {
    channel: {
      required: true,
      validator: v => typeof v === "object" || v === null
    }
  },
  components: {
    StudioMenu
  },
  data() {
    return {
      contentStyle: {}
    };
  },
  mounted() {
    const height = this.$refs.studioScaffold.clientHeight + "px";
    this.contentStyle = {
      height
    };
  }
};
</script>

<style>
.channel-menu {
  width: 240px;
  border-right: 1px solid lightgray;
}
</style>

