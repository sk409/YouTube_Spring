<template>
<div class="h-100">
    <div class="d-flex h-100">
        <ChannelMenu :channel="channel" class="h-100"></ChannelMenu>
        <slot name="content"></slot>
    </div>
</div>
</template>

<script>
import ChannelMenu from "./ChannelMenu.vue";
export default {
    props: {
        channel: {
            type: Object,
            required: true
        }
    },
    components: {
        ChannelMenu
    }
}
</script>

<style>
.channel-menu {
    width: 240px;
    border-right: 1px solid lightgray;
}
</style>

