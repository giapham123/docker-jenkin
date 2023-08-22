<template>
  <v-container
    class="home-app-wrapper"
    grid-list-md
    fluid
    text-xs-center
    style="height:auto"
  >
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-dialog v-model="show" max-width="250">
      <v-card>
        <v-card-title class="headline primary-title">
          Insert Agreement Id
        </v-card-title>
        <v-card-text>
          <v-flex>
            <v-text-field
              v-model="agreementId"
              label="Agreement Id"
            ></v-text-field>
          </v-flex>
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
    }
  },
  data() {
    return {
      agreementId: null,
      loading: false,
      dialog: false
    };
  },
  computed: {},
  watch: {
    show(value) {
      this.agreementId = null;
      this.dialog = value;
    },

    dialog() {
      if (!this.dialog) {
        this.emitClose();
      }
    }
  },
  methods: {
    ...mapActions('pendingDisbursement', ['insertSigAgre']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async handleSave() {
      this.loading = true;
      var param = {
        agreementId: this.agreementId
      };
      var result = await this.insertSigAgre(param);
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
