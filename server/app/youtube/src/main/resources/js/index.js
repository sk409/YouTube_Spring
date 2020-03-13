import GuideScaffold from "./components/GuideScaffold.vue";
import NavbarSearch from "./components/NavbarSearch.vue";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js"

new Vue({
    el: "#app",
    vuetify,
    components: {
        GuideScaffold,
        NavbarSearch
    },
    data() {
        return {
            subscriptionChannels: [],
        }
    },
    mounted() {
        const subscriptionChannelsJSON = this.$refs.subscriptionChannels.textContent;
        this.subscriptionChannels = subscriptionChannelsJSON ? JSON.parse(subscriptionChannelsJSON) : [];
    }
});