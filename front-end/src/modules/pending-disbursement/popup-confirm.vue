<template>
  <v-container
    class="home-app-wrapper"
    grid-list-md
    fluid
    text-xs-center
    style="height:auto"
  >
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-dialog v-model="show" max-width="450">
      <v-card>
        <v-card-title class="headline primary-title">
          Warning
        </v-card-title>
        <v-card-text>
          <h5>
            Would you like to close these agreements ({{ totalApp }}) in table
            for delete?
          </h5>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="teal vbutton" text @click="handleSave">
            OK
          </v-btn>
          <v-btn color="teal vbutton" text @click="handleClose">
            Cancel
          </v-btn>
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
  computed: {
    totalApp() {
      return this.param.length;
    }
  },
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
    ...mapActions('pendingDisbursement', ['deleteAgreementId']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async handleSave() {
      this.loading = true;
      var result = await this.deleteAgreementId(this.param);
      this.loading = false;
      this.$emit('save', result);
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
