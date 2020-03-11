<template>
  <div class="d-flex">
    <div></div>
    <div class="flex-fill">
      <div>{{comment.user.nickname}}</div>
      <pre>{{comment.text}}</pre>
      <div class="d-flex mt-3">
        <div>
          <v-btn icon>
            <v-icon>mdi-thumb-up</v-icon>
          </v-btn>
          <span>426</span>
        </div>
        <div class="ml-2">
          <v-btn icon>
            <v-icon>mdi-thumb-down</v-icon>
          </v-btn>
          <span>32</span>
        </div>
        <v-btn text class="ml-2" @click="videoCommentForm=true">返信</v-btn>
      </div>
      <VideoCommentForm
        v-show="videoCommentForm"
        :parent-id="comment.id"
        :video-id="comment.videoId"
        class="w-100"
        @cancel="videoCommentForm=false"
      ></VideoCommentForm>
    </div>
  </div>
</template>

<script>
import VideoCommentForm from "./VideoCommentForm.vue";
export default {
  props: {
    comment: {
      type: Object,
      required: true
    }
  },
  components: {
    VideoCommentForm
  },
  data() {
    return {
      videoCommentForm: false
    };
  }
};
</script>

<style>
pre {
  font-family: "Roboto", sans-serif;
}
</style>