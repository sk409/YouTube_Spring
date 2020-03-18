<template>
  <v-card>
    <v-card-text>
      <div class="pt-2">
        <v-form ref="form">
          <v-text-field v-model="name" color="success" :rules="nameRules" label="再生リストの名前"></v-text-field>
          <v-textarea v-model="overview" color="success" :rules="overviewRules" label="再生リストの説明"></v-textarea>
        </v-form>
      </div>
    </v-card-text>
    <v-card-actions>
      <div class="d-flex">
        <v-btn text @click="$emit('cancel')">キャンセル</v-btn>
        <v-btn color="success" text @click="create">作成</v-btn>
      </div>
    </v-card-actions>
  </v-card>
</template>

<script>
import ajax from "../ajax.js";
export default {
  props: {
    channelId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      name: "",
      nameRules: [
        v => !!v || "名前を入力してください",
        v => v.length <= 150 || "名前を150文字以内で入力してください"
      ],
      overview: "",
      overviewRules: [
        v => !!v || "再生リストの説明を入力してください",
        v =>
          v.length <= 5000 || "再生リストの説明を5000文字以内で入力してください"
      ]
    };
  },
  methods: {
    create() {
      const data = {
        name: this.name,
        overview: this.overview,
        channelId: this.channelId
      };
      console.log(data);
      ajax.post(this.$routes.playlists.base, data).then(response => {
        this.$emit("created", response.data);
      });
    }
  }
};
</script>