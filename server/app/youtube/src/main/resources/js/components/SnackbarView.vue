<template>
  <v-snackbar v-model="snackbar" :timeout="timeout" top class="white--text">
    <span>{{message}}</span>
    <v-btn icon @click="snackbar=false">
      <v-icon class="white--text">mdi-close</v-icon>
    </v-btn>
  </v-snackbar>
</template>


<script>
export default {
  props: {
    message: {
      type: String,
      default: ""
    },
    timeout: {
      type: Number,
      default: 2000
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      snackbar: false
    };
  },
  watch: {
    snackbar() {
      if (this.snackbar === this.visible) {
        return;
      }
      this.$emit("update:visible", this.snackbar);
    },
    visible: {
      immediate: true,
      handler() {
        this.snackbar = this.visible;
      }
    }
  }
};
</script>