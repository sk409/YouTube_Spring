<template>
  <div>
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
      const scrollBottom =
        document.documentElement.scrollTop +
        document.documentElement.clientHeight;
      if (scrollBottom === document.documentElement.scrollHeight) {
        this.$emit("scroll:bottom");
      }
    }
  }
};
</script>