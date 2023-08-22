<template>
  <v-container
    class="home-app-wrapper"
    grid-list-md
    fluid
    text-xs-center
    style="height:auto"
  >
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-dialog v-model="show" max-width="300">
      <v-card>
        <v-card-title class="headline primary-title">
          {{ title }}
        </v-card-title>
        <v-card-text>
          <v-flex>
            <v-layout align-center>
              <date-picker
                v-model="backDate"
                :label="`${titleDate}`"
                icon="event"
            /></v-layout>
          </v-flex>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="teal vbutton" text @click="handleSave">
            Process
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
import DatePicker from 'modules/common/datePicker';
import Loading from 'vue-loading-overlay';
export default {
  components: {
    Loading,
    DatePicker
  },
  props: {
    show: {
      type: Boolean,
      default: false
    },
    param: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      titleDate: '',
      backDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      title: null,
      loading: false,
      dialog: false,
      backDateType: null
    };
  },
  computed: {},
  watch: {
    param() {
      this.backDateType = this.param.type;
      this.title = this.param.title;
      if (this.param.type == 'BDWO') {
        this.titleDate = 'Write Off Date';
      } else {
        this.titleDate = 'Revert Write Off Date';
      }
    },
    show(value) {
      this.backDate = new Date(new Date().getTime()).toLocaleDateString(
        'en-US'
      );
      this.dialog = value;
    },

    dialog() {
      if (!this.dialog) {
        this.emitClose();
      }
    }
  },
  methods: {
    ...mapActions('backDateWo', ['processPendingBackDate']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async handleSave() {
      this.loading = true;
      var param = {
        type: this.backDateType,
        date: this.backDate
      };
      if (this.backDateType == 'BDWO') {
        param.backDateType = this.backDateType;
      } else {
        param.backDateType = this.backDateType;
      }
      var result = await this.processPendingBackDate(param);
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
