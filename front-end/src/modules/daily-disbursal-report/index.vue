<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Daily Disbursal Report</span>
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
          <v-flex width="100%">
            <v-data-table
              :headers="headers"
              :items="dataDailyDisbursalReport"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:headers="props">
                <tr>
                  <th
                    v-for="header in props.headers"
                    :key="header.text"
                    :class="['word-wrap-example']"
                    style="font-weight: bold;color: black"
                  >
                    {{ header.text }}
                  </th>
                </tr>
              </template>
              <template v-slot:items="props">
                <tr v-if="props.item.product != 'TOTAL'">
                  <td class="text-xs-center">{{ props.item.logDt }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.product }}
                  </td>
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
                  <td class="text-xs-center">{{ props.item.accumApp }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.accumAmt).replace(
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
                  <td class="text-xs-left fixColumn fixColumnBold">
                    {{ props.item.product }}
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
                    {{ props.item.accumApp }}
                  </td>
                  <td class="text-xs-right fixColumnBold">
                    {{
                      String(props.item.accumAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <v-layout>
        <v-sheet class="v-sheet--offset mx-auto">
            <!-- load sum system success and system fail -->
            <chart-statistic-status
              v-if="isloaded"
              :data-chart="dataChartStatistic"
            ></chart-statistic-status>
          </v-sheet></v-layout>
      </v-container>
    </v-card>
  </div>
</template>
<script>
import DatePicker from 'modules/common/datePicker';
import { mapActions } from 'vuex';
import Loading from 'vue-loading-overlay';
import chartStatisticStatus from './chart_statistic';
export default {
  components: {
    DatePicker,
    Loading,
    chartStatisticStatus
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
      disbursalDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      dataDailyDisbursalReport: [],
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
        { text: 'Date', sortable: false, align: 'center', width: '10%' },
        { text: 'Product', sortable: false, align: 'center' },
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
        { text: 'Accumulated No. of App', sortable: false, align: 'center' },
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
    dataDailyDisbursalReport() {}
  },
  created() {
    localStorage.removeItem('baseImageChart');
    localStorage.removeItem('baseImageChart1');
  },

  methods: {
    ...mapActions('dailyDisbursalReport', [
      'getDataDailyDisbursalReport',
      'exportExcelFile'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async getData() {
      localStorage.removeItem('baseImageChart');
      // localStorage.removeItem('baseImageChart1');
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
      var result = await this.getDataDailyDisbursalReport(param);
      if (result.success) {
        this.dataDailyDisbursalReport = result.data.resultT;
        var totalApp = 0;
        var totalincludedInsAmt = 0;
        var totalnotInsAmt = 0;
        var totalinsAmt = 0;
        var totalaccumApp = 0;
        var totalaccumAmt = 0;
        var lsDataDisT = [];
        var lsDataDisT1 = [];
        var labelChart = [];
        for (let i = 0; i < this.dataDailyDisbursalReport.length; i++) {
          totalApp = totalApp + this.dataDailyDisbursalReport[i].countApp;
          totalincludedInsAmt += parseInt(
            this.dataDailyDisbursalReport[i].includedInsAmt
          );
          totalnotInsAmt += parseInt(
            this.dataDailyDisbursalReport[i].notInsAmt
          );
          totalinsAmt += parseInt(this.dataDailyDisbursalReport[i].insAmt);
          totalaccumApp += parseInt(this.dataDailyDisbursalReport[i].accumApp);
          totalaccumAmt += parseInt(this.dataDailyDisbursalReport[i].accumAmt);
          var a = parseInt(this.dataDailyDisbursalReport[i].includedInsAmt);
          a = (a - (a % 1000000)) / 1000000;
          lsDataDisT.push(a);
          labelChart.push(this.dataDailyDisbursalReport[i].product);
        }

        this.dataDailyDisbursalReport = this.dataDailyDisbursalReport.concat({
          countApp: totalApp,
          includedInsAmt: totalincludedInsAmt,
          notInsAmt: totalnotInsAmt,
          insAmt: totalinsAmt,
          accumApp: totalaccumApp,
          accumAmt: totalaccumAmt,
          logDt: '',
          product: 'TOTAL'
        });
        for (let i = 0; i < result.data.resultT1.length; i++) {
          var a = parseInt(result.data.resultT1[i].includedInsAmt);
          a = (a - (a % 1000000)) / 1000000;
          lsDataDisT1.push(a);
        }
        this.dataChartStatistic.labelChart = labelChart;
        this.dataChartStatistic.dateDBT = lsDataDisT;
        this.dataChartStatistic.dataMK_NEW = lsDataDisT1;
        this.dataChartStatistic.dateT = this.disbursalDate;
        this.dataChartStatistic.dateT1 = datePre1;
      } else {
        this.dataChartStatistic.labelChart = [];
        this.dataChartStatistic.dateDBT = [];
        this.dataChartStatistic.dataMK_NEW = [];
        this.dataChartStatistic.dateT = '';
        this.dataChartStatistic.dateT1 = '';
        this.loading = false;
        this.isloaded = true;
        this.dataDailyDisbursalReport = [];
        this.showWarningMsg(result.message);
      }
      this.loading = false;
      this.isloaded = true;
    },
    async exportDataBankStatement() {
      if (this.dataDailyDisbursalReport.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      this.loading = true;
      var param = {
        productType: this.lsProducts.selected,
        disbursalDate: this.disbursalDate,
        base64Img: localStorage.getItem('baseImageChart')
      };
      const result = await this.exportExcelFile(param);
      if (result.data != null) {
        const helloWorldExcelContent = result.data;
        const anchor_href =
          'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,' +
          helloWorldExcelContent;
        const exportLinkElement = document.createElement('a');

        exportLinkElement.hidden = true;
        exportLinkElement.download = 'Daily_Disbursal_Report.xlsx';
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

.fixColumnBold {
  font-weight: bold;
}

table thead th.word-wrap-example {
  width: 10px;
  word-wrap: break-word;
  white-space: normal;
}
</style>
