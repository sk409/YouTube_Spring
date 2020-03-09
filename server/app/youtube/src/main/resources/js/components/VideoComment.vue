<template>
  <div class="d-flex">
    <div></div>
    <div>
      <div>{{comment.user.nickname}}</div>
      <pre>{{comment.text}}</pre>
      <div class="d-flex justify-space-between mt-3">
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
        <v-btn text class="ml-2">返信</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    comment: {
      validator: v => typeof v === "object" || v === null,
      required: true
    }
  }
};
</script>