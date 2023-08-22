<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Schedule Before Interest Advance Booking</span>
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
          <!-- <v-flex xs5>
            <v-layout align-center>
              <date-picker
                v-model="fromDate"
                :smaller="toDate"
                label="From"
                icon="event"
                :disabledDate="disableDt"
                :disabledParam="disableDt"
              />
              <date-picker
                v-model="toDate"
                :greater="fromDate"
                label="To"
                icon="event"
                :disabledDate="disableDt"
                :disabledParam="disableDt"
              />
            </v-layout>
          </v-flex> -->
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
              @click="exportData"
            >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="datInterestAdvanceBooking"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.appId }}</td>
                  <td class="text-xs-center">{{ props.item.startTenure }}</td>
                  <td class="text-xs-center">{{ props.item.endTenure }}</td>
                  <td class="text-xs-center">
                    {{
                      String(props.item.firstIntcompChange).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.insertDt }}</td>
                  <td class="text-xs-center">{{ props.item.updateDt }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="RepaymentHeaders"
              :items="dataRepayment"
              :search="search"
              :pagination.sync="pagination"
              :rows-per-page-items="[10]"
              hide-actions
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.proposalId }}</td>
                  <td class="text-xs-center">{{ props.item.instlNum }}</td>
                  <td class="text-xs-center">{{ props.item.dueDate }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.instlAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.printComp).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.intComp).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.effRate }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.balPrin).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.days }}</td>
                </tr>
              </template>
            </v-data-table>
            <div class="text-xs-center pt-2">
              <v-pagination
                v-show="showPagin"
                v-model="page"
                :length="pages"
                :total-visible="7"
                @input="nextPages"
              >
              </v-pagination>
            </div>
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
      disableDt: false,
      datInterestAdvanceBooking: [],
      dataRepayment: [],
      loading: false,
      agreementId: null,
      headers: [
        { text: 'APP_ID_C', sortable: false, align: 'center' },
        { text: 'START_TENURE', sortable: false, align: 'center' },
        { text: 'END_TENURE', sortable: false, align: 'center' },
        { text: 'FIRST_INTCOMP_ADDON', sortable: false, align: 'center' },
        { text: 'INSERTDATE', sortable: false, align: 'center' },
        { text: 'UPDATE_DATE', sortable: false, align: 'center' }
      ],
      RepaymentHeaders: [
        { text: 'APP_ID_C', sortable: false, align: 'center' },
        { text: 'INSTLNUM', sortable: false, align: 'center' },
        { text: 'DUEDATE', sortable: false, align: 'center' },
        { text: 'INSTLAMT', sortable: false, align: 'center' },
        { text: 'PRINCOMP', sortable: false, align: 'center' },
        { text: 'INTCOMP', sortable: false, align: 'center' },
        { text: 'EFFRATE', sortable: false, align: 'center' },
        { text: 'BALPRIN', sortable: false, align: 'center' },
        { text: 'DAYS', sortable: false, align: 'center' }
      ],
      fromDate: new Date().toLocaleDateString('en-US'),
      toDate: new Date().toLocaleDateString('en-US'),
      showPagin: false,
      search: '',
      pagination: {},
      totalData: 0,
      page: 1
    };
  },
  computed: {
    pages() {
      if (
        this.pagination.rowsPerPage == null ||
        this.pagination.totalItems == null
      )
        return 0;

      return Math.ceil(this.totalData / this.pagination.rowsPerPage);
    }
  },
  watch: {
    agreementId() {
      if (this.agreementId.length != 0) {
        this.disableDt = true;
      } else {
        this.disableDt = false;
      }
    }
  },
  created() {},

  methods: {
    ...mapActions('scheduleBfIntAdvanceBooking', [
      'getReduceInterest',
      'getRepayment',
      'exportExcelFile'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async nextPages() {
      this.loading = true;
      var fromDt = null;
      var toDt = null;
      if (this.agreementId == null || this.agreementId == '') {
        fromDt = this.fromDate;
        toDt = this.toDate;
      } else {
        fromDt = null;
        toDt = null;
      }
      var param = {
        agreementId: this.agreementId,
        fromDt: fromDt,
        toDt: toDt,
        page: this.page
      };
      var resultRepayment = await this.getRepayment(param);
      if (resultRepayment.success) {
        this.dataRepayment = resultRepayment.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async getData() {
      if (this.agreementId == null) {
        this.showErrorMsg('Please Input AgreementId');
        return;
      }
      if (this.agreementId.trim() == '') {
        this.showErrorMsg('Please Input AgreementId');
        return;
      }
      if (new Date(this.fromDate).getDate() == 1) {
        var lastDayOfMonth = new Date(
          new Date(this.fromDate).getFullYear(),
          new Date(this.fromDate).getMonth() + 1,
          0
        );
        let totalDayInMonth = Math.ceil(
          (new Date(lastDayOfMonth).getTime() -
            new Date(this.fromDate).getTime()) /
            (1000 * 3600 * 24)
        );
        let totalDayInTwosDate = Math.ceil(
          (new Date(this.toDate).getTime() -
            new Date(this.fromDate).getTime()) /
            (1000 * 3600 * 24)
        );
        if (totalDayInTwosDate > totalDayInMonth) {
          this.showErrorMsg('Please select in 1 month!');
          return;
        }
      } else {
        let totalDayInTwosDate = Math.ceil(
          (new Date(this.toDate).getTime() -
            new Date(this.fromDate).getTime()) /
            (1000 * 3600 * 24)
        );
        if (totalDayInTwosDate >= 31) {
          this.showErrorMsg('Please select in 1 month!');
          return;
        }
      }

      this.loading = true;
      this.page = 1;
      var fromDt = null;
      var toDt = null;
      if (this.agreementId == null || this.agreementId == '') {
        fromDt = this.fromDate;
        toDt = this.toDate;
      } else {
        fromDt = null;
        toDt = null;
      }

      var param = {
        agreementId: this.agreementId,
        fromDt: fromDt,
        toDt: toDt,
        page: 1
      };
      var result = await this.getReduceInterest(param);
      var resultRepayment = await this.getRepayment(param);
      if (result.success) {
        this.datInterestAdvanceBooking = result.data.resultData;
        this.totalData = result.data.totalPage;
        this.dataRepayment = resultRepayment.data;
        this.showPagin = true;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    exportReduceInterest() {
      var filename = Date.now();
      var exporter;
      if (this.datInterestAdvanceBooking.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      exporter = new Exporter('Reduce Interest ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = this.datInterestAdvanceBooking;
      exporter.addSheet(MainConstant.headersReport, data, 'sheet_1');
      const result = exporter.exportExcel();
      if (result.success) {
        console.log('Export file is successful');
      } else {
        console.error(result.message);
      }
      this.loading = false;
    },
    exportData() {
      this.exportReduceInterest();
      this.exportRepayment();
    },
    async exportRepayment() {
      var fromDt = null;
      var toDt = null;
      if (this.agreementId == null || this.agreementId == '') {
        fromDt = this.fromDate;
        toDt = this.toDate;
      } else {
        fromDt = null;
        toDt = null;
      }
      var param = {
        agreementId: this.agreementId,
        fromDt: fromDt,
        toDt: toDt
      };
      const result = await this.exportExcelFile(param);
      if (result.data != null) {
        var blob;
        blob = window.atob(result.data);
        let today = new Date().toISOString().slice(0, 10);
        var link = document.createElement('a');
        if (link.download !== undefined) {
          link.href = 'data:text/csv;charset=utf-8,' + encodeURI(blob);
          link.target = '_blank';
          link.download = 'Repayment ' + moment(today).format('DD/MM/YYYY');
          +'.csv';
          link.click();
          this.loading = false;
        }
      } else {
        this.showWarningMsg(result.message);
      }
    }
  }
};
</script>
<style scoped>
.changeColorRow {
  background-color: rgb(215, 215, 44);
}
</style>
