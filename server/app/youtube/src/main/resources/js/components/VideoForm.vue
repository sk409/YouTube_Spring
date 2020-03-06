<template>
  <div>
    <v-card>
      <v-card-title>動画をアップロード</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <div v-if="!video" class="d-flex align-center justify-center dropbox">
          <div>
            <div class="subtitle-1">アップロードするファイルをドラッグアンドドロップします</div>
            <div class="text-center mt-4">
              <input ref="fileInput" hidden type="file" @change.prevent="changeVideo" />
              <v-btn color="success" @click="clickFileSelectionButton">ファイルを選択</v-btn>
            </div>
          </div>
        </div>
        <div v-else class="pt-3">
          <v-stepper v-model="step" color="success" flat>
            <v-stepper-header>
              <v-stepper-step :complete="1 < step" step="1">詳細</v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step :complete="2 < step" step="2">動画の要素</v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step step="3">公開設定</v-stepper-step>
            </v-stepper-header>
            <v-stepper-items>
              <v-stepper-content step="1">
                <div class="step-title">詳細</div>
                <v-tooltip :value="titleError" bottom>
                  <template #activator="{}">
                    <div
                      :class="metadataInputContainerClass(titleError)"
                      class="mt-3 pa-2 metadata-input-container"
                    >
                      <span class="input-caption">タイトル(必須)</span>
                      <div class="metadata-input" contenteditable tabindex="0" @input="inputTitle"></div>
                      <div class="d-flex">
                        <span class="input-length ml-auto mr-1">{{title.length}}/{{titleMaxLength}}</span>
                      </div>
                    </div>
                  </template>
                  <span>{{titleError}}</span>
                </v-tooltip>
                <v-tooltip :value="overviewError" bottom>
                  <template #activator="{}">
                    <div
                      :class="metadataInputContainerClass(overviewError)"
                      class="mt-3 pa-2 metadata-input-container"
                    >
                      <span class="input-caption">説明</span>
                      <div
                        class="metadata-input"
                        contenteditable
                        tabindex="1"
                        @input="inputOverview"
                      ></div>
                      <div class="d-flex">
                        <span
                          class="input-length ml-auto mr-1"
                        >{{overview.length}}/{{overviewMaxLength}}</span>
                      </div>
                    </div>
                  </template>
                  <span>{{overviewError}}</span>
                </v-tooltip>
              </v-stepper-content>
              <v-stepper-content step="2">
                <div class="step-title">動画の要素</div>
                <div class="d-flex grey lighten-3 mt-4 pa-3">
                  <div>
                    <div>終了画面の追加</div>
                    <div class="caption">動画の最後で関連コンテンツをプロモーションする</div>
                  </div>
                  <v-btn color="success" text class="ml-auto">追加</v-btn>
                </div>
                <div class="d-flex grey lighten-3 mt-4 pa-3">
                  <div>
                    <div>カードの追加</div>
                    <div class="caption">動画の途中で関連コンテンツをプロモーションする</div>
                  </div>
                  <v-btn color="success" text class="ml-auto">追加</v-btn>
                </div>
              </v-stepper-content>
              <v-stepper-content step="3">
                <div class="step-title">公開設定</div>
              </v-stepper-content>
            </v-stepper-items>
          </v-stepper>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn v-show="2<=step" text class="ml-auto" @click="step-=1">戻る</v-btn>
        <v-btn
          v-show="step<=2"
          color="success"
          :class="{'ml-auto': step===1}"
          :disabled="hasErrors"
          @click="goNextStep"
        >次へ</v-btn>
        <v-btn color="success" v-show="step===3" @click="createVideo">公開</v-btn>
      </v-card-actions>
    </v-card>
    <SnackbarView :message="notification" :visible.sync="snackbar"></SnackbarView>
  </div>
</template>

<script>
import ajax from "../ajax.js";
import SnackbarView from "./SnackbarView.vue";
export default {
  props: {
    channel: {
      type: Object,
      required: true
    }
  },
  components: {
    SnackbarView
  },
  data() {
    return {
      notification: "",
      overview: "",
      overviewError: null,
      overviewMaxLength: 5000,
      snackbar: false,
      step: 1,
      title: "",
      titleError: null,
      titleMaxLength: 100,
      video: null
    };
  },
  computed: {
    hasErrors() {
      return this.overviewError !== null || this.titleError !== null;
    }
  },
  methods: {
    changeVideo(e) {
      const files = e.target.files;
      const file = files[0];
      if (!file.type.startsWith("video")) {
        this.notification = "動画以外はアップロードできません";
        this.snackbar = true;
        return;
      }
      this.video = {};
      this.video.file = file;
      this.video.title = file.name;
      this.video.uniqueId = this.$uuid();
    },
    checkErrors() {
      this.checkOverviewError();
      this.checkTitleError();
    },
    checkOverviewError() {
      if (this.overviewMaxLength < this.overview.length) {
        this.overviewError = "説明が長すぎます";
      } else {
        this.overviewError = null;
      }
    },
    checkTitleError() {
      if (this.titleMaxLength < this.title.length) {
        this.titleError = "タイトルが長すぎます";
      } else if (this.title.length === 0) {
        this.titleError = "動画にタイトルを付けてください";
      } else {
        this.titleError = null;
      }
    },
    clickFileSelectionButton() {
      this.$refs.fileInput.click();
    },
    createVideo() {
      const data = {
        title: this.title,
        overview: this.overview,
        uniqueId: this.video.uniqueId,
        video: this.video.file
      };
      const config = {
          headers: {
              "Content-Type": "multipart/form-data"
          }
      };
      console.log(data);
      ajax.post(this.$routes.channels.videos.base(this.channel.id), data, config).then(response => {
          console.log(response);
      });
    },
    goNextStep() {
      this.checkErrors();
      if (this.hasErrors) {
        return;
      }
      this.step += 1;
    },
    inputOverview(e) {
      this.overview = e.target.textContent;
      this.checkOverviewError();
    },
    inputTitle(e) {
      this.title = e.target.textContent;
      this.checkTitleError();
    },
    metadataInputContainerClass(error) {
      return error ? ["metadata-input-container-error"] : [];
    }
  }
};
</script>

<style lang="scss">
.dropbox {
  height: 80vh;
}

.input-length {
  display: none;
}

.step-title {
  font-size: 24px;
}

.metadata-input {
  outline: none;
}

.metadata-input-container {
  border: 1px solid lightgrey;
}

.metadata-input-container:focus-within {
  border: 1px solid $success;
}

.metadata-input-container:focus-within .metadata-input {
  border: none;
}

.metadata-input-container:focus-within .input-caption {
  color: $success;
}

.metadata-input-container:focus-within .input-length {
  display: inline;
}

.metadata-input-container-error {
  border: 1px solid $error;
}

.metadata-input-container-error .input-caption {
  color: $error;
}

.metadata-input-container-error:focus-within {
  border: 1px solid $error;
}

.metadata-input-container-error:focus-within .input-caption {
  color: $error;
}
</style>
