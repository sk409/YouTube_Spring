<template>
  <v-card>
    <v-card-title>{{channelname}}のチャンネル登録を解除しますか?</v-card-title>
    <v-divider></v-divider>
    <v-card-actions>
      <v-btn text class="ml-auto">キャンセル</v-btn>
      <v-btn color="success" :loading="loading" text @click="unsubscribe">登録解除</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import ajax from "../ajax.js";
export default {
  props: {
    channelname: {
      type: String,
      required: true
    },
    subscriberId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      loading: false
    };
  },
  methods: {
    unsubscribe() {
      this.loading = true;
      ajax
        .delete(this.$routes.subscribers.destroy(this.subscriberId))
        .then(response => {
          this.loading = false;
          this.$emit("unsubscribed", response.data);
        });
    }
  }
};
</script>