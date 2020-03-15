<template>
  <div v-if="channel">
    <div>
      <div class="subtitle-1">{{channel.name}}</div>
      <div class="caption"></div>
    </div>
    <div>
      <div
        v-for="scrollMenuItem in scrollMenuItems"
        :key="scrollMenuItem.title"
        :class="scrollMenuItemClass(scrollMenuItem)"
        class="pa-3 scroll-menu-item"
      >
        <v-icon style="color:inherit;">{{scrollMenuItem.icon}}</v-icon>
        <span class="ml-4">{{scrollMenuItem.title}}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    channel: {
      required: true,
      validator: v => typeof v === "object" || v === null
    }
  },
  computed: {
    scrollMenuItems() {
      if (!this.channel) {
        return [];
      }
      return [
        {
          icon: "mdi-view-dashboard",
          title: "ダッシュボード",
          path: ""
        },
        {
          icon: "mdi-library-video",
          title: "動画",
          path: this.$routes.channels.videos.upload(this.channel.id)
        }
      ];
    }
  },
  methods: {
    scrollMenuItemClass(scrollMenuItem) {
      if (location.pathname === scrollMenuItem.path) {
        return ["scroll-menu-item-active"];
      }
      return ["grey--text"];
    }
  }
};
</script>

<style lang="scss">
.scroll-menu-item-active {
  border-left: 3px solid $primary;
}
.scroll-menu-item:hover,
.scroll-menu-item-active {
  background: rgb(244, 244, 244);
  color: $primary;
}
</style>