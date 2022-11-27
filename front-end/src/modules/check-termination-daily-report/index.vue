<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Check Termination Daily Report</span>
      </v-card-title>
    </v-card>
    <v-card>
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs2>
            <v-text-field
              v-model="agreementId"
              label="AgreementID"
            ></v-text-field>
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="getData"
              >Search</v-btn
            >
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="exportReport"
              >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataTerminationDailyReport"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.no }}</td>
                  <td class="text-xs-center">{{ props.item.terDate }}</td>
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-center">{{ props.item.maker }}</td>
                  <td class="text-xs-center">{{ props.item.checker }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
      </v-container>
    </v-card>
  </div>
</template>
<script>
import DatePicker from 'modules/common/datePicker';
import { mapActions } from 'vuex';
import Loading from 'vue-loading-overlay';
import MainConstant from './constant_main';
import { Exporter } from '@chidoan/excel-utils';
import moment from 'moment';
export default {
  components: {
    DatePicker,
    Loading
  },

  data() {
    return {
      loading: false,
      agreementId: null,
      dataTerminationDailyReport: [],
      headers: [
        { text: 'No', sortable: false, align: 'center' },
        { text: 'TERMINATION DATE', sortable: false, align: 'center' },
        { text: 'AGREEMENT ID', sortable: false, align: 'center' },
        { text: 'MAKER', sortable: false, align: 'center' },
        { text: 'CHECKER', sortable: false, align: 'center' }
      ]
    };
  },
  watch: {
    dataTerminationDailyReport() {
      for (let i = 0; i < this.dataTerminationDailyReport.length; i++) {
        this.dataTerminationDailyReport[i].no = i + 1;
      }
    },
    cbReceiptFromTo() {
      if (this.cbReceiptFromTo) {
        this.disabledDateFromTo = false;
      } else {
        this.disabledDateFromTo = true;
      }
    }
  },
  created() {},

  methods: {
    ...mapActions('checkTerDailyReport', [
      'getTerminationDailyReport',
      'exportData'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async getData() {
      this.loading = true;
      var param = {
        agreementId: this.agreementId
      };
      const result = await this.getTerminationDailyReport(param);
      if (result.data != null) {
        this.dataTerminationDailyReport = result.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async exportReport() {
      var filename = Date.now();
      var exporter;
      if (this.dataTerminationDailyReport.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      await this.exportData();
      exporter = new Exporter('Check Termination Daily Report ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = this.dataTerminationDailyReport;
      exporter.addSheet(MainConstant.headersReport, data, 'sheet_1');
      const result = exporter.exportExcel();
      if (result.success) {
        console.log('Export file is successful');
      } else {
        console.error(result.message);
      }
      this.loading = false;
    }
  }
};
</script>
<style scoped>
.fixedColumn {
  min-width: 300px;
  max-width: 300px;
  word-wrap: break-word;
}
</style>
