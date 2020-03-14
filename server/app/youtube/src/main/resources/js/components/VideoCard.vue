<template>
  <v-card flat :ripple="false" @click="$transition($routes.watch.base(video.uniqueId))">
    <div class="p-relative">
      <v-img :src="$serverUrl(video.thumbnailPath)" class="w-100"></v-img>
      <div class="caption duration p-absolute">{{video.duration | timeColonSeconds}}</div>
    </div>
    <div class="d-flex">
      <v-avatar size="40">
        <v-img :src="$serverUrl(video.channel.user.profileImagePath)"></v-img>
      </v-avatar>
      <div class="flex-fill mt-1 ml-1">
        <div class="video-title">{{video.title | truncate(25)}}</div>
        <div class="mt-1">
          <div
            class="channel-name"
            @click.prevent.stop="$transition($routes.channels.show(video.channel.uniqueId))"
          >{{video.channel.name}}</div>
          <div class="views-and-date">{{video.views}}回視聴・{{video.createdAt | dateAgo}}</div>
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
.channel-name,
.views-and-date {
  font-size: 16px;
}

.channel-name:hover {
  text-decoration: underline;
  font-weight: bold;
}

.duration {
  background: rgb(70, 69, 77);
  color: rgb(249, 249, 249);
  right: 4px;
  bottom: 4px;
}

.video-title {
  word-break: break-all;
}
</style>