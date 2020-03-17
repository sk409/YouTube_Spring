<template>
  <div>
    <div></div>
    <div class="px-10 white">
      <div class="d-flex">
        <v-avatar>
          <v-img :src="$serverUrl(channel.profileImagePath)"></v-img>
        </v-avatar>
        <div class="ml-5">
          <h3>{{channel.name}}</h3>
          <div class="grey--text">
            チャンネル登録者
            <span class="ml-1">{{channel.subscriberCount}}人</span>
          </div>
        </div>
        <div class="ml-auto">
          <v-btn v-if="!userSubscriber" color="primary" class="ml-auto" @click="subscribe">チャンネル登録</v-btn>
          <v-btn v-else class="ml-auto" @click="dialogChannelUnsubscribeForm=true">登録済み</v-btn>
        </div>
      </div>
      <div class="d-flex justify-center mt-3">
        <div
          v-for="tabItem in tabItems"
          :key="tabItem.title"
          :class="tabItemClass(tabItem)"
          class="cursor-pointer px-5 py-3"
          @click="$transition(tabItem.route)"
        >{{tabItem.title}}</div>
      </div>
    </div>
    <v-dialog v-model="dialogChannelUnsubscribeForm" width="420">
      <channel-unsubscribe-form
        v-if="channel && userSubscriber"
        :channelname="channel.name"
        :subscriber-id="userSubscriber.id"
        @unsubscribed="unsubscribed"
      ></channel-unsubscribe-form>
    </v-dialog>
    <snackbar-view :message="notification" :visible.sync="snackbar"></snackbar-view>
  </div>
</template>

<script>
import ajax from "../ajax.js";
import ChannelUnsubscribeForm from "./ChannelUnsubscribeForm.vue";
import SnackbarView from "./SnackbarView.vue";
export default {
  props: {
    channel: {
      type: Object,
      required: true
    },
    userSubscriber: {
      type: Object,
      required: true
    }
  },
  components: {
    ChannelUnsubscribeForm,
    SnackbarView
  },
  data() {
    return {
      dialogChannelUnsubscribeForm: false,
      notification: "",
      snackbar: false
    };
  },
  computed: {
    tabItems() {
      return [
        {
          title: "ホーム",
          route: this.$routes.channels.home(this.channel.uniqueId)
        },
        {
          title: "動画",
          route: this.$routes.channels.videos.base(this.channel.uniqueId)
        }
      ];
    }
  },
  methods: {
    subscribe() {
      const data = {
        channelId: this.channel.id
      };
      this.channel.subscriberCount += 1;
      ajax.post(this.$routes.subscribers.base, data).then(response => {
        this.notification = "登録チャンネルに追加しました";
        this.snackbar = true;
        this.userSubscriber = response.data;
      });
    },
    tabItemClass(tabItem) {
      if (location.pathname === tabItem.route) {
        return ["tab-active"];
      }
      return [];
    },
    unsubscribed() {
      this.channel.subscriberCount -= 1;
      this.notification = "登録チャンネルから削除しました";
      this.snackbar = true;
      this.dialogChannelUnsubscribeForm = false;
      this.userSubscriber = null;
    }
  }
};
</script>

<style>
.tab-active {
  border-bottom: 2px solid #aaaaaa;
}
</style>