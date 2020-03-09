<template>
  <div>
    <v-text-field v-model="comment" color="grey" type="text" placeholder="公開のコメントを入力"></v-text-field>
    <div class="d-flex">
      <v-btn text class="ml-auto">キャンセル</v-btn>
      <v-btn
        color="success"
        :disabled="comment.length === 0"
        :loading="loading"
        @click="create"
      >コメント</v-btn>
    </div>
  </div>
</template>

<script>
import ajax from "../ajax.js";
export default {
  props: {
    videoId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      comment: "",
      loading: false
    };
  },
  methods: {
    create() {
      const data = {
        text: this.comment,
        videoId: this.videoId
      };
      this.loading = true;
      ajax.post(this.$routes.videoComments.base, data).then(response => {
        this.comment = "";
        this.loading = false;
        this.$emit("created", response.data);
      });
    }
  }
};
</script>