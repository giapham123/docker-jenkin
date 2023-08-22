<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Monthly Disbursal Report</span>
      </v-card-title>
    </v-card>
    <v-card min-height="80vh">
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs2>
            <v-select
              v-model="lsProducts.selected"
              :items="lsProducts.items"
              label="Product"
            ></v-select>
          </v-flex>
          <v-flex xs2>
            <date-picker
              v-model="disbursalDate"
              label="Disbursal Date"
              icon="event"
            />
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
              @click="exportDataBankStatement"
            >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataMonthlyDisbursalReport"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:headers="props">
                <tr>
                  <th
                    v-for="header in props.headers"
                    :key="header.text"
                    :class="['word-wrap-example']"
                    style="font-weight: bold;color: black;"
                  >
                    {{ header.text }}
                  </th>
                </tr>
              </template>
              <template v-slot:items="props">
                <tr v-if="props.item.logDt != 'TOTAL'">
                  <td class="text-xs-center">{{ props.item.logDt }}</td>
                  <td class="text-xs-center">{{ props.item.countApp }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.includedInsAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.notInsAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right ">
                    {{
                      String(props.item.insAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.noAppActual }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.actualCashDis).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.actualCashDisInIns).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.pendingDisAmount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.accumDisAmountInIns).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                </tr>
                <tr v-else>
                  <td class="text-xs-center fixColumnBold">
                    {{ props.item.logDt }}
                  </td>
                  <td class="text-xs-center fixColumnBold">
                    {{ props.item.countApp }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                    {{
                      String(props.item.includedInsAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                    {{
                      String(props.item.notInsAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                    {{
                      String(props.item.insAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center fixColumnBold">
                    {{ props.item.noAppActual }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                    {{
                      String(props.item.actualCashDis).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                    {{
                      String(props.item.actualCashDisInIns).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                    {{
                      String(props.item.pendingDisAmount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                   0
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
          <v-layout>
            <v-flex>
              <chart-statistic-status
                v-if="isloaded"
                :data-chart="dataChartStatistic"
              ></chart-statistic-status>
              <div class="text-xs-center">
                <h4>Disbursed Amount</h4>
            </div></v-flex
            >
            <v-divider class="mx-0" inset vertical></v-divider>
            <!-- load sum system success and system fail -->
            <v-flex>
              <chart-statistic-status-line
                v-if="isloaded"
                :data-chart="dataChartStatisticLine"
              ></chart-statistic-status-line>
            <div class="text-xs-center"><h4>Disbursed Cases</h4></div></v-flex
            >
          </v-layout>
        </v-layout>
      </v-container>
    </v-card>
  </div>
</template>
<script>
import DatePicker from 'modules/common/datePicker';
import { mapActions } from 'vuex';
import Loading from 'vue-loading-overlay';
import chartStatisticStatus from './chart_statistic';
import chartStatisticStatusLine from './chart_statistic_line';
export default {
  components: {
    DatePicker,
    Loading,
    chartStatisticStatus,
    chartStatisticStatusLine
  },

  data() {
    return {
      isloaded: true,
      dataChartStatistic: {
        dateDBT: [],
        dataMK_NEW: [],
        dataCK1_NEW: [],
        dataCK2_NEW: [],
        labelChart: [],
        dateT: '',
        dateT1: ''
      },
      dataChartStatisticLine: {
        dateDBT: [],
        dataMK_NEW: [],
        dataCK1_NEW: [],
        dataCK2_NEW: [],
        labelChart: [],
        dateT: '',
        dateT1: ''
      },
      disbursalDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      dataMonthlyDisbursalReport: [],
      lsProducts: {
        selected: 'All Product',
        items: [
          'All Product',
          'SALPIL',
          'OCLPIL',
          'FALPIL',
          'MAEALPIL',
          'SELPIL',
          'TOPPIL',
          'TGDD',
          'FPT',
          'Cao Phong',
          'CDL',
          'Stock loan',
          'Auto Loan'
        ]
      },
      loading: false,
      headers: [
        { text: 'Date', sortable: false, align: 'center' },
        { text: 'NO. OF APP', sortable: false, align: 'center' },
        {
          text: 'Disbursed Amount included Insurance',
          sortable: false,
          align: 'center'
        },
        {
          text: 'Disbursed Amount excluded Insurance',
          sortable: false,
          align: 'center'
        },
        { text: 'Insurance amount', sortable: false, align: 'center' },
        { text: 'No. of App Actual', sortable: false, align: 'center' },
        {
          text: 'Actual cash disbursed (only)',
          sortable: false,
          align: 'center'
        },
        {
          text: 'Actual cash disbursed included insurance',
          sortable: false,
          align: 'center'
        },
        { text: 'Pending Disbursed Amount', sortable: false, align: 'center' },
        {
          text: 'Accumulated Disbursed Amount',
          sortable: false,
          align: 'center'
        }
      ]
    };
  },
  computed: {},
  watch: {
    dataMonthlyDisbursalReport() {}
  },
  created() {
    localStorage.removeItem('baseImageChart');
    localStorage.removeItem('baseImageChart1');
  },

  methods: {
    ...mapActions('monthlyDisbursalReport', [
      'getDataMonthlyDisbursalReport',
      'exportExcelFile'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async getData() {
      localStorage.removeItem('baseImageChart');
      localStorage.removeItem('baseImageChart1');
      this.loading = true;
      this.isloaded = false;
      var datePre1 = new Date(
        new Date(this.disbursalDate) - 24 * 60 * 60 * 1000
      ).toLocaleDateString('en-US');
      var param = {
        dateT1: datePre1,
        productType: this.lsProducts.selected,
        disbursalDate: this.disbursalDate
      };
      var result = await this.getDataMonthlyDisbursalReport(param);
      if (result.success) {
        this.dataMonthlyDisbursalReport = result.data.resultT;
        var totalApp = 0;
        var totalincludedInsAmt = 0;
        var totalnotInsAmt = 0;
        var totalinsAmt = 0;
        var totalnoAppActual = 0;
        var totalactualCashDis = 0;
        var totalactualCashDisInIns = 0;
        var totalpendingDisAmount = 0;
        var totalAccumDisAmountInIns = 0;
        var dataYearCur = [];
        var dataYearPre = [];
        var dataYearCurCount = [];
        var dataYearPreCount = [];
        for (let i = 0; i < this.dataMonthlyDisbursalReport.length; i++) {
          totalApp += parseInt(this.dataMonthlyDisbursalReport[i].countApp);
          totalincludedInsAmt += parseInt(
            this.dataMonthlyDisbursalReport[i].includedInsAmt
          );
          totalnotInsAmt += parseInt(
            this.dataMonthlyDisbursalReport[i].notInsAmt
          );
          totalinsAmt += parseInt(this.dataMonthlyDisbursalReport[i].insAmt);
          totalnoAppActual += parseInt(
            this.dataMonthlyDisbursalReport[i].noAppActual
          );
          totalactualCashDis += parseInt(
            this.dataMonthlyDisbursalReport[i].actualCashDis
          );
          totalactualCashDisInIns += parseInt(
            this.dataMonthlyDisbursalReport[i].actualCashDisInIns
          );
          totalpendingDisAmount += parseInt(
            this.dataMonthlyDisbursalReport[i].pendingDisAmount
          );
          totalAccumDisAmountInIns += parseInt(
            this.dataMonthlyDisbursalReport[i].accumDisAmountInIns
          );
        }

        this.dataMonthlyDisbursalReport = this.dataMonthlyDisbursalReport.concat(
          {
            countApp: totalApp,
            includedInsAmt: totalincludedInsAmt,
            notInsAmt: totalnotInsAmt,
            insAmt: totalinsAmt,
            noAppActual: totalnoAppActual,
            actualCashDis: totalactualCashDis,
            actualCashDisInIns: totalactualCashDisInIns,
            pendingDisAmount: totalpendingDisAmount,
            accumDisAmountInIns: totalAccumDisAmountInIns,
            logDt: 'TOTAL'
          }
        );
        for (let i = 0; i < result.data.resultChartCurYear.length; i++) {
          var a = parseInt(result.data.resultChartCurYear[i].disAmout);
          a = (a - (a % 1000000)) / 1000000;
          dataYearCur.push(a);
          dataYearCurCount.push(result.data.resultChartCurYear[i].countApp);
        }
        for (let i = 0; i < result.data.resultPreYear.length; i++) {
          var a = parseInt(result.data.resultPreYear[i].disAmout);
          a = (a - (a % 1000000)) / 1000000;
          dataYearPre.push(a);
          dataYearPreCount.push(result.data.resultPreYear[i].countApp);
        }
        this.dataChartStatistic.labelChart = [
          'Jan',
          'Feb',
          'Mar',
          'Apr',
          'May',
          'Jun',
          'Jul',
          'Aug',
          'Sep',
          'Oct',
          'Nov',
          'Dev'
        ];
        this.dataChartStatisticLine.labelChart = [
          'Jan',
          'Feb',
          'Mar',
          'Apr',
          'May',
          'Jun',
          'Jul',
          'Aug',
          'Sep',
          'Oct',
          'Nov',
          'Dev'
        ];
        var yearSplit = this.disbursalDate.split('/');
        this.dataChartStatistic.dateDBT = dataYearCur;
        this.dataChartStatistic.dataMK_NEW = dataYearPre;
        this.dataChartStatistic.dateT = yearSplit[2];
        this.dataChartStatistic.dateT1 = yearSplit[2] - 1;
        this.dataChartStatisticLine.dateDBT = dataYearCurCount;
        this.dataChartStatisticLine.dataMK_NEW = dataYearPreCount;

        this.dataChartStatisticLine.dateT = yearSplit[2];
        this.dataChartStatisticLine.dateT1 = yearSplit[2] - 1;
      } else {
        this.dataChartStatistic.dateDBT = [];
        this.dataChartStatistic.dataMK_NEW = [];
        this.dataChartStatistic.dateT = '';
        this.dataChartStatistic.dateT1 = '';
        this.dataChartStatisticLine.dateDBT = [];
        this.dataChartStatisticLine.dataMK_NEW = [];

        this.dataChartStatisticLine.dateT = '';
        this.dataChartStatisticLine.dateT1 = '';
        this.loading = false;
        this.dataMonthlyDisbursalReport = [];
        this.showWarningMsg(result.message);
        this.isloaded = true;
      }
      this.loading = false;
      this.isloaded = true;
    },
    async exportDataBankStatement() {
      if (this.dataMonthlyDisbursalReport.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      this.loading = true;
      var param = {
        productType: this.lsProducts.selected,
        disbursalDate: this.disbursalDate,
        base64Img: localStorage.getItem('baseImageChart'),
        base64Img1: localStorage.getItem('baseImageChart1')
      };
      const result = await this.exportExcelFile(param);
      if (result.data != null) {
        const helloWorldExcelContent = result.data;
        const anchor_href =
          'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,' +
          helloWorldExcelContent;
        const exportLinkElement = document.createElement('a');

        exportLinkElement.hidden = true;
        exportLinkElement.download = 'Monthly_Disbursal_Report.xlsx';
        exportLinkElement.href = anchor_href;
        exportLinkElement.text = 'downloading...';

        document.body.appendChild(exportLinkElement);
        exportLinkElement.click();
        exportLinkElement.remove();
        this.loading = false;
      } else {
        this.loading = false;
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

.fixColumn {
  min-width: 200px;
  max-width: 200px;
  word-wrap: break-word;
}

table thead th.word-wrap-example {
  width: 10px;
  word-wrap: break-word;
  white-space: normal;
}
.fixColumnBold {
  font-weight: bold;
}
</style>
