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
          <span>{{highRatingCount}}</span>
        </div>
        <div class="ml-2">
          <v-btn icon>
            <v-icon>mdi-thumb-down</v-icon>
          </v-btn>
          <span>{{lowRatingCount}}</span>
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
  },
  computed: {
    highRatingCount() {
      if (!this.comment) {
        return 0;
      }
      const highRatingCount = this.comment.highRatingCount;
      if (!this.comment.userRating) {
        return highRatingCount;
      }
      const add =
        this.comment.userRating.ratingId === this.$constants.highRatingId
          ? 1
          : 0;
      return highRatingCount + add;
    },
    lowRatingCount() {
      if (!this.comment) {
        return 0;
      }
      const lowRatingCount = this.comment.lowRatingCount;
      if (!this.comment.userRating) {
        return lowRatingCount;
      }
      const add =
        this.comment.userRating.ratingId === this.$constants.lowRatingId
          ? 1
          : 0;
      return lowRatingCount + add;
    }
  }
};
</script>

<style>
pre {
  font-family: "Roboto", sans-serif;
}
</style>