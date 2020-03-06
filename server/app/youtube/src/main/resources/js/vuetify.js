import Vue from "vue";
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
Vue.use(Vuetify)
const opts = {
  theme: {
    themes: {
      light: {
        primary: '#ea3524',
        secondary: '#b0bec5',
        accent: '#8c9eff',
        error: '#b02619',
        success: "#2761cd"
      },
    },
  },
};
const vuetify = new Vuetify(opts);
export default vuetify;