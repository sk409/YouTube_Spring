import ajax from "./ajax.js";
import Vue from "vue";
import vuetify from "./vuetify.js";
import {routes} from "./utils.js";
import "./common.js";

new Vue({
    el: "#app",
    vuetify,
    data() {
        return {
          username: "",
          usernameRules: [v => !!v || "ユーザIDを入力してください"],
          nickname: "",
          nicknameRules: [
            v => !!v || "ユーザ名を入力してください",
            v => v.length <= 255 || "ユーザ名を255文字以内で入力してください"
          ],
          email: "",
          emailRules: [
            v => !!v || "メールアドレスを入力してください",
            v => v.length <= 255 || "255文字以内で入力してください",
            v => {
              if (v.length === 0) {
                return "メールアドレスを入力してください";
              }
              const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
              return pattern.test(v) || "メールアドレスの形式が正しくありません";
            }
          ],
          loading: false,
          password: "",
          passwordRules: [
            v => 6 <= v.length || "パスワードを6文字以上で入力してください"
          ],
          passwordConfirmation: ""
        };
    },
    methods: {
      register() {
        if (!this.$refs.form.validate()) {
          return;
        }
        const data = {
          username: this.username,
          nickname: this.nickname,
          password: this.password,
          email: this.email
        };
        console.log(data);
        ajax.post(routes.register.base, data).then(response => {
          location.href = routes.root.base;
        });
      }
    }
})