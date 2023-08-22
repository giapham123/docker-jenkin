<template>
  <v-container
    class="home-app-wrapper"
    grid-list-md
    fluid
    text-xs-center
    style="height:auto"
  >
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-dialog v-model="show" max-width="480">
      <v-card>
        <v-card-title class="headline primary-title">
          Error
        </v-card-title>
        <v-card-text>
          {{ message }}
        </v-card-text>
        <v-card-actions class="text-xs-center">
          <v-layout row>
            <v-flex justify-center>
              <v-btn color="teal vbutton" text @click="handleClose">
                Close
            </v-btn></v-flex
          ></v-layout
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
import { mapActions } from 'vuex';

import Loading from 'vue-loading-overlay';
export default {
  components: {
    Loading
  },
  props: {
    show: {
      type: Boolean,
      default: false
    },
    param: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      loading: false,
      dialog: false,
      message: null
    };
  },
  computed: {},
  watch: {
    show(value) {
      this.dialog = value;
    },

    dialog() {
      if (!this.dialog) {
        this.emitClose();
      }
    },
    param() {
      this.message = this.param;
    }
  },
  methods: {
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),

    emitClose() {
      this.$emit('close', false);
    },
    handleClose() {
      this.dialog = false;
    }
  }
};
</script>
<style>
.vbutton {
  background-color: rgb(0, 105, 92) !important;
  color: #ffffff !important;
}

.full_text_table {
  white-space: nowrap;
}
</style>
