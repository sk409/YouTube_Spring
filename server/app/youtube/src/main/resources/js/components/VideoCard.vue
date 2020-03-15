<template>
  <v-card flat :ripple="false" @click="$transition($routes.watch.base(video.uniqueId))">
    <VideoThumbnail :video="video"></VideoThumbnail>
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
import VideoThumbnail from "./VideoThumbnail.vue";
export default {
  props: {
    video: {
      type: Object,
      required: true
    }
  },
  components: {
    VideoThumbnail
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

.video-title {
  word-break: break-all;
}
</style>