<template>
  <div class="pl-3 overflow-y-auto">
    <div
      v-for="menuItem in menuItems"
      :key="menuItem.title"
      :style="menuItemStyle(menuItem)"
      class="py-2"
    >
      <v-icon :style="menuItemIconStyle(menuItem)" class="icon">{{menuItem.icon}}</v-icon>
      <span class="ml-3">{{menuItem.title}}</span>
    </div>
    <v-divider></v-divider>
    <div class="mt-3 subtitle-1">登録チャンネル</div>
    <div v-for="channel in channels" :key="channel.id" class="py-2">
      <v-avatar class="icon">
        <v-img :src="$serverUrl(channel.profileImagePath)"></v-img>
      </v-avatar>
      <span>{{channel.name}}</span>
    </div>
    <div v-if="fetchingSubscriptionChannels" class="py-2 text-center">
      <v-progress-circular indeterminate></v-progress-circular>
    </div>
    <div v-else-if="remainingChannelCount !== 0" @click="fetchSubscriptionChannels(null)">
      <v-icon class="icon">mdi-menu-down</v-icon>
      <span class="ml-3">他の{{remainingChannelCount}}件を表示</span>
    </div>
  </div>
</template>

<script>
import ajax from "../ajax.js";
export default {
  data() {
    return {
      channels: [],
      fetchingSubscriptionChannels: false,
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
      ],
      subscriptionCount: 0
    };
  },
  computed: {
    remainingChannelCount() {
      return Math.max(0, this.subscriptionCount - this.channels.length);
    }
  },
  created() {
    this.fetchSubscriptionChannels(5);
    this.fetchSubscriptionCount();
  },
  methods: {
    fetchSubscriptionChannels(limit) {
      const data = {};
      if (this.channels.length != 0) {
        data.excludedChannelIds = this.channels.map(channel => channel.id);
      }
      if (limit) {
        data.limit = limit;
      }
      this.fetchingSubscriptionChannels = true;
      ajax.get(this.$routes.channels.subscription, data).then(response => {
        this.channels = this.channels.concat(response.data);
        this.fetchingSubscriptionChannels = false;
      });
    },
    fetchSubscriptionCount() {
      ajax.get(this.$routes.users.subscriptionCount).then(response => {
        this.subscriptionCount = response.data;
      });
    },
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

<style>
.icon {
  width: 40px;
}
</style>