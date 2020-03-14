<template>
  <div class="d-flex">
    <div>
      <v-avatar size="42">
        <v-img :src="$serverUrl(comment.user.profileImagePath)"></v-img>
      </v-avatar>
    </div>
    <div class="flex-fill ml-3">
      <div>{{comment.user.nickname}}</div>
      <pre>{{comment.text}}</pre>
      <div class="d-flex mt-3">
        <div :style="ratingStyle(highRating)">
          <v-btn icon @click="clickHighRating">
            <v-icon :style="ratingStyle(highRating)">mdi-thumb-up</v-icon>
          </v-btn>
          <span>{{highRatingCount}}</span>
        </div>
        <div class="ml-2" :style="ratingStyle(lowRating)">
          <v-btn icon @click="clickLowRating">
            <v-icon :style="ratingStyle(lowRating)">mdi-thumb-down</v-icon>
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
        @created="createdReply"
      ></VideoCommentForm>
      <div v-if="showingReplies">
        <div>
          <v-btn text class="d-flex aling-center success--text" @click="showingReplies=false">
            <v-icon class="success--text">mdi-menu-up</v-icon>
            <span>{{comment.childCount}}件の返信を非表示</span>
          </v-btn>
        </div>
        <VideoComment v-for="reply in replies" :key="reply.id" :comment="reply"></VideoComment>
        <v-btn
          v-if="replies.length < comment.childCount"
          text
          class="d-flex align-center success--text"
          @click="fetchNextReplies"
        >
          <v-icon style="color:inherit;">mdi-subdirectory-arrow-right</v-icon>他の返信を表示
        </v-btn>
      </div>
      <div v-else-if="comment.childCount !== 0">
        <v-btn text class="d-flex aling-center success--text" @click="showReplies">
          <v-icon class="success--text">mdi-menu-down</v-icon>
          {{comment.childCount}}件の返信を表示
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import ajax from "../ajax.js";
import VideoComment from "./VideoComment.vue";
import VideoCommentForm from "./VideoCommentForm.vue";
export default {
  name: "VideoComment",
  props: {
    comment: {
      type: Object,
      required: true
    }
  },
  components: {
    VideoComment,
    VideoCommentForm
  },
  data() {
    return {
      replies: [],
      showingReplies: false,
      videoCommentForm: false
    };
  },
  computed: {
    highRating() {
      return (
        !!this.comment.userRating &&
        this.comment.userRating.ratingId === this.$constants.highRatingId
      );
    },
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
    lowRating() {
      return (
        !!this.comment.userRating &&
        this.comment.userRating.ratingId === this.$constants.lowRatingId
      );
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
  },
  methods: {
    clickRating(ratingId) {
      if (!this.comment.userRating) {
        const data = {
          videoCommentId: this.comment.id,
          ratingId
        };
        ajax.post(this.$routes.videoCommentRating.base, data).then(response => {
          this.comment.userRating = response.data;
        });
      } else if (this.comment.userRating.ratingId === ratingId) {
        ajax.delete(
          this.$routes.videoCommentRating.destroy(this.comment.userRating.id)
        );
        this.comment.userRating = null;
      } else {
        const data = {
          videoCommentId: this.comment.id,
          ratingId
        };
        ajax
          .put(
            this.$routes.videoCommentRating.update(this.comment.userRating.id),
            data
          )
          .then(response => {
            this.comment.userRating = response.data;
          });
      }
    },
    clickHighRating() {
      this.clickRating(this.$constants.highRatingId);
    },
    clickLowRating() {
      this.clickRating(this.$constants.lowRatingId);
    },
    createdReply(reply) {
      this.$emit("created:reply", reply);
    },
    fetchNextReplies() {
      const data = {
        videoCommentId: this.comment.id
      };
      if (this.replies.length === 0) {
        data.limit = 10;
      } else {
        data.newAfterVideoCommentId = this.replies[this.replies.length - 1].id;
      }
      ajax.get(this.$routes.videoComments.replies, data).then(response => {
        this.replies = this.replies.concat(response.data);
      });
    },
    ratingStyle(applying) {
      if (applying) {
        return {
          color: this.$vuetify.theme.currentTheme.success
        };
      }
      return {};
    },
    showReplies() {
      this.showingReplies = true;
      if (this.replies.length === 0) {
        this.fetchNextReplies();
      }
    }
  }
};
</script>

<style>
pre {
  font-family: "Roboto", sans-serif;
}
</style>