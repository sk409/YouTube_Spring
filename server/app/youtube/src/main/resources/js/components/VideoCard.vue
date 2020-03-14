<template>
  <v-card flat @click="$transition($routes.watch.base(video.uniqueId))">
    <v-img :src="$serverUrl(video.thumbnailPath)" class="w-100"></v-img>
    <div class="d-flex">
      <v-avatar size="40">
        <v-img :src="$serverUrl(video.channel.user.profileImagePath)"></v-img>
      </v-avatar>
      <div class="flex-fill mt-1 ml-1">
        <div class="video-title">{{video.title | truncate(25)}}</div>
        <div class="mt-1">
          <div class="caption">{{video.channel.name}}</div>
          <div class="caption">{{video.views}}回視聴</div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
export default {
  props: {
    video: {
      type: Object,
      required: true
    }
  },
  methods: {}
};
</script>

<style>
.video-title {
  word-break: break-all;
}
</style>