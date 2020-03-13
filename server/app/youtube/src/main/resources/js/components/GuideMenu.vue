<template>
  <div class="pl-3">
    <div
      v-for="menuItem in menuItems"
      :key="menuItem.title"
      :style="menuItemStyle(menuItem)"
      class="py-4"
    >
      <v-icon :style="menuItemIconStyle(menuItem)">{{menuItem.icon}}</v-icon>
      <span class="ml-3">{{menuItem.title}}</span>
    </div>
    <v-divider></v-divider>
    <div class="mt-3 subtitle-1">登録チャンネル</div>
    <div v-for="channel in channels" :key="channel.id">{{channel.name}}</div>
  </div>
</template>

<script>
export default {
  props: {
    channels: {
      default: () => [],
      type: Array
    }
  },
  data() {
    return {
      menuItems: [
        {
          title: "ホーム",
          icon: "mdi-home",
          route: "/"
        },
        {
          title: "急上昇",
          icon: "mdi-fire"
        },
        {
          title: "登録チャンネル",
          icon: "mdi-youtube-subscription"
        },
        {
          title: "ライブラリ",
          icon: "mdi-youtube-tv"
        },
        {
          title: "履歴",
          icon: "mdi-av-timer"
        },
        {
          title: "自分の動画",
          icon: "mdi-youtube"
        },
        {
          title: "後で見る",
          icon: "mdi-timer"
        },
        {
          title: "高く評価した動画",
          icon: "mdi-thumb-up"
        }
      ]
    };
  },
  methods: {
    isMenuItemActive(menuItem) {
      return location.pathname.endsWith(menuItem.route);
    },
    menuItemStyle(menuItem) {
      if (this.isMenuItemActive(menuItem)) {
        return {
          background: "rgb(244, 244, 244)"
        };
      }
      return {};
    },
    menuItemIconStyle(menuItem) {
      if (this.isMenuItemActive(menuItem)) {
        return {
          color: this.$vuetify.theme.currentTheme.primary
        };
      }
      return {};
    }
  }
};
</script>