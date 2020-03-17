<template>
  <div ref="scrollView">
    <slot></slot>
  </div>
</template>

<script>
export default {
  created() {
    onscroll = this.scroll;
  },
  methods: {
    scroll() {
      const diff = Math.abs(
        document.documentElement.scrollTop +
          document.documentElement.clientHeight -
          document.documentElement.scrollHeight
      );
      if (diff <= 1) {
        this.$emit("scroll:bottom");
      }
    }
  }
};
</script>