<template>
  <v-app-bar color="white" flat>
    <v-app-bar-nav-icon></v-app-bar-nav-icon>
    <v-spacer></v-spacer>
    <input type="text" placeholder="検索" />
    <v-spacer></v-spacer>
    <div class="d-flex justify-space-between">
      <v-btn icon @click="dialogChannel = true">
        <v-icon>mdi-view-grid-plus</v-icon>
      </v-btn>
      <v-btn icon @click="clickVideoPlus">
        <v-icon>mdi-video-plus</v-icon>
      </v-btn>
    </div>
    <v-dialog v-model="dialogChannel">
      <ChannelForm @created="createdChannel"></ChannelForm>
    </v-dialog>
    <SnackbarView :message="notification" :visible.sync="snackbar"></SnackbarView>
  </v-app-bar>
</template>

<script>
import ajax from "../ajax.js";
import ChannelForm from "./ChannelForm.vue";
import SnackbarView from "./SnackbarView.vue";
export default {
  components: {
    ChannelForm,
    SnackbarView
  },
  data() {
    return {
      dialogChannel: false,
      notification: "",
      snackbar: false
    };
  },
  methods: {
    clickVideoPlus() {
      ajax.get(this.$routes.channels.lastSelected).then(response => {
        location.href = this.$routes.channels.videos.upload(response.data.id);
      });
    },
    createdChannel(channel) {
      this.dialogChannel = false;
      this.notification = `チャンネル${channel.name}を作成しました`;
      this.snackbar = true;
    }
  }
};
</script>