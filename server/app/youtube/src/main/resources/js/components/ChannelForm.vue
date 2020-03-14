<template>
  <v-card>
    <v-card-title class="success white--text">チャンネルを作成</v-card-title>
    <v-card-text>
      <v-form ref="form">
        <v-text-field color="success" v-model="name" label="チャンネル名"></v-text-field>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-btn color="success" :loading="loading" class="mx-auto" @click="create">作成</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import ajax from "../ajax.js";
export default {
  data() {
    return {
      loading: false,
      name: ""
    };
  },
  methods: {
    create() {
      const data = {
        name: this.name,
        uniqueId: this.$uuid()
      };
      this.loading = true;
      ajax.post(this.$routes.channels.base, data).then(response => {
        this.loading = false;
        this.$emit("created", response.data);
      });
    }
  }
};
</script>

<style>
</style>