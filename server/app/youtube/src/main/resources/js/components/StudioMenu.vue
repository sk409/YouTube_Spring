<template>
  <div v-if="channel">
    <div class="text-center">
      <v-avatar size="128">
        <v-img :src="$serverUrl(channel.profileImagePath)"></v-img>
      </v-avatar>
      <div class="subtitle-1">{{channel.name}}</div>
      <div class="caption">{{channel.user.username}}</div>
    </div>
    <div>
      <div
        v-for="scrollMenuItem in scrollMenuItems"
        :key="scrollMenuItem.title"
        :class="scrollMenuItemClass(scrollMenuItem)"
        class="cursor-pointer pa-3 scroll-menu-item"
        @click="$transition(scrollMenuItem.route)"
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
          route: ""
        },
        {
          icon: "mdi-library-video",
          title: "動画",
          route: this.$routes.studio.channels.videos.upload(this.channel.uniqueId)
        },
        {
          icon: "mdi-playlist-play",
          title: "再生リスト",
          route: this.$routes.studio.channels.playlists(this.channel.uniqueId)
        }
      ];
    }
  },
  methods: {
    scrollMenuItemClass(scrollMenuItem) {
      if (location.pathname === scrollMenuItem.route) {
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