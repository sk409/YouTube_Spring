import ajax from "./ajax.js";
import Vue from "vue";
import vuetify from "./vuetify.js";
import "./common.js";

new Vue({
    el: "#app",
    vuetify,
    data() {
        return {
          loading: false,
          notification: "",
          password: "",
          passwordRules: [
            v => 6 <= v.length || "パスワードを6文字以上で入力してください"
          ],
          snackbar: false,
          username: "",
          usernameRules: [v => !!v || "ユーザIDを入力してください"],
        };
      },
    methods: {
      login() {
        if (!this.$refs.form.validate()) {
          return;
        }
        const data = {
          username: this.username,
          password: this.password
        };
        ajax.post("/login", data, {headers: {"Content-Type": "application/x-www-form-urlencoded"}}).then(response => {
          location.href = this.$routes.root.base;
        })
      }
    }
});