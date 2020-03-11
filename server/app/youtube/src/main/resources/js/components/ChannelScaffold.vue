<template>
  <div v-if="channel" class="h-100">
    <div class="d-flex h-100">
      <ChannelMenu :channel="channel" class="h-100"></ChannelMenu>
      <div class="flex-fill">
        <slot name="content"></slot>
      </div>
    </div>
  </div>
</template>

<script>
import ChannelMenu from "./ChannelMenu.vue";
export default {
  props: {
    channel: {
      required: true,
      validator: v => typeof v === "object" || v === null
    }
  },
  components: {
    ChannelMenu
  }
};
</script>

<style>
.channel-menu {
  width: 240px;
  border-right: 1px solid lightgray;
}
</style>

