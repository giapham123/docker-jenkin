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
          Push SAP
        </v-card-title>
        <v-card-text>
          <h5>
            <b>You only push SAP once a day.</b> <br />
            Please make sure all steps below are action before Push SAP:<br />
            - PD Disb<br />
            - Back date WO<br />
            - Upload manual entries<br />
          </h5>
        </v-card-text>
        <v-card-actions class="text-xs-center">
          <v-layout row>
            <v-flex justify-center>
              <v-btn color="teal vbutton" text @click="handleSave">
                PUSH SAP
              </v-btn>
              <v-btn color="teal vbutton" text @click="handleClose">
                Cancel
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
      type: Array,
      default: null
    }
  },
  data() {
    return {
      loading: false,
      dialog: false
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
    }
  },
  methods: {
    ...mapActions('viewSapEntries', ['putSap']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async handleSave() {
      this.loading = true;
      var rs = await this.putSap();
      this.loading = false;
      this.$emit('save', rs);
    },
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
