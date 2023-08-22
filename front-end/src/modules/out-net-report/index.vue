<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>OUT NET Report</span>
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

          <v-layout align-center style="margin-left:40px">
            <v-checkbox
              v-model="cbReceiptFromTo"
              hide-details
              class="shrink mr-2"
              style="margin-top: -10px"
            ></v-checkbox>
            <date-picker
              v-model="fromDate"
              :smaller="toDate"
              :disabled-date="disabledDateFromTo"
              label="Receipt From"
              icon="event"
            />
            <date-picker
              v-model="toDate"
              :greater="fromDate"
              :disabled-date="disabledDateFromTo"
              label="Receipt To"
              icon="event"
            />
          </v-layout>
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
              :items="dataOutNetReport"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.no }}</td>
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-center">{{ props.item.agreementNo }}</td>
                  <td class="fixedColumn">{{ props.item.customerName }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.collectionFee).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.emiAtm).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.lastEmi).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.lastDuedate }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.receiptAtm).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.receiptDate }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.netReceiveableAtReceiptDate).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.netReceiveableAtReportDate).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.forceClosureChargeAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.reportDate }}</td>
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
      cbReceiptFromTo: false,
      disabledDateFromTo: true,
      search: '',
      loading: false,
      agreementId: null,
      fromDate: new Date(
        new Date().getTime() - 24 * 60 * 60 * 1000
      ).toLocaleDateString('en-US'),
      toDate: new Date().toLocaleDateString('en-US'),
      dataOutNetReport: [],
      headers: [
        { text: 'No', sortable: false },
        { text: 'AGREEMENTID', sortable: false },
        { text: 'AGREEMENTNO', sortable: false },
        { text: 'CUSTOMERNAME', sortable: false },
        { text: 'COLLECTION_FEE', sortable: false },
        { text: 'EMI_AMT', sortable: false },
        { text: 'LAST_EMI', sortable: false },
        { text: 'LAST_DUEDATE', sortable: false },
        { text: 'RECEIPT_AMT', sortable: false },
        { text: 'RECEIPT_DATE', sortable: false },
        { text: 'NET_RECEIVEABLE_AT_RECEIPTDATE', sortable: false },
        { text: 'NET_RECEIVEABLE_AT_REPORTTDATE', sortable: false },
        { text: 'FORE_CLOSURE_CHARGE_AMT', sortable: false },
        { text: 'REPORT_DATE', sortable: false }
      ]
    };
  },
  watch: {
    dataOutNetReport() {
      for (let i = 0; i < this.dataOutNetReport.length; i++) {
        this.dataOutNetReport[i].no = i + 1;
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
    ...mapActions('outNetReport', ['getOutNetReportData', 'exportData']),
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
      if (!this.disabledDateFromTo) {
        param.receiptFrom = this.fromDate;
        param.receiptTo = this.toDate;
      } else {
        param.receiptFrom = null;
        param.receiptTo = null;
      }
      const result = await this.getOutNetReportData(param);
      if (result.data != null) {
        this.dataOutNetReport = result.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async exportReport() {
      var filename = Date.now();
      var exporter;
      if (this.dataOutNetReport.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      await this.exportData();
      exporter = new Exporter('OUT NET Report ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = this.dataOutNetReport;
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
