<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Collector Compare</span>
      </v-card-title>
    </v-card>
    <v-card min-height="80vh" style="padding-bottom: 20px;">
      <v-container fluid grid-list-md>
        <v-layout row wrap>
          <v-flex xs2>
            <v-text-field
              v-model="agreementId"
              label="Agreement Id"
            ></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-text-field
              v-model="agreementNo"
              label="Agreement No"
            ></v-text-field>
          </v-flex>
          <v-flex xs3>
            <v-select
              v-model="collector.selected"
              :items="collector.items"
              label="Collector"
            ></v-select>
          </v-flex>
          <v-flex xs3>
            <v-select
              v-model="channel.selected"
              :items="channel.items"
              label="Channel"
            ></v-select>
          </v-flex>
          <v-flex xs3>
            <v-select
              v-model="rsMgs.selected"
              :items="rsMgs.items"
              label="Result Message"
            ></v-select>
          </v-flex>
          <v-flex xs2>
            <v-layout align-center>
              <v-checkbox v-model="isUsetxnDt"></v-checkbox>
              <date-picker
                :disabled-date="!isUsetxnDt"
                :disabled-param="!isUsetxnDt"
                v-model="txnDate"
                label="TXN Date"
                icon="event"
              />
            </v-layout>
          </v-flex>
          <v-flex xs2>
            <v-layout align-center>
              <v-checkbox v-model="isUseupdateDt"></v-checkbox>
              <date-picker
                :disabled-date="!isUseupdateDt"
                :disabled-param="!isUseupdateDt"
                v-model="compareDate"
                label="Compare Date"
                icon="event"
              />
            </v-layout>
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
              @click="exportDataGenSap"
            >Export</v-btn
            >
          </v-flex>
          <v-layout class="text-lg-right" row wrap>
            <v-flex v-if="!isProcess" xs7 offset-xs5 offset-md2 offset-lg5>
              <v-btn
                small
                color="rgb(0, 105, 92)"
                class="white--text"
                @click="genSap"
              >Collector Compare
              </v-btn></v-flex
            >
            <v-flex v-else xs7 offset-xs5 offset-md2 offset-lg5>
              <v-btn disabled small color="rgb(0, 105, 92)" class="white--text"
              >In Progressing
              </v-btn></v-flex
            >
          </v-layout>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataCollectorCompare"
              :rows-per-page-items="[10]"
              item-key="agreementNo"
              class="elevation-1 full_box_table"
            >
              <template v-slot:headers="props">
                <tr>
                  <th
                    v-for="header in props.headers"
                    :key="header.text"
                    style="font-weight: bold;color: black;"
                  >
                    {{ header.text }}
                  </th>
                </tr>
              </template>
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-center">{{ props.item.agreementNo }}</td>
                  <td class="text-xs-left">{{ props.item.txnDt }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.txnAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.collector }}</td>
                  <td class="text-xs-center ">
                    {{ props.item.channel }}
                  </td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.resultMsg }}
                  </td>
                  <td class="text-xs-center fixColumn">
                    {{ props.item.processDt }}
                  </td>
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
      agreementId: '',
      agreementNo: '',
      collector: {
        selected: 'All',
        items: [
          'All',
          'MOMO',
          'VIETTEL',
          'SHOPEE',
          'VNPAY',
          'EPAY',
          'VNPT',
          'SMARTNET',
          'PAYOO',
          'ZALO',
          'VIMO',
          'VNPOST',
          'ONEPAY'
        ]
      },
      channel: {
        items: ['All', 'F1', 'IMPORT'],
        selected: 'All'
      },
      rsMgs: {
        selected: 'All',
        items: [
          'All',
          'MATCH',
          'IN F1 - NOT IN STATEMENT',
          'NOT IN F1 - IN STATEMENT',
          'F1 DUPLICATE',
          'STATEMENT DUPLICATE',
          'DIFFERENT AMOUNT'
        ]
      },
      isUseupdateDt: true,
      isUsetxnDt: false,
      compareDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      txnDate: new Date(
        new Date().getTime() - 20 * 60 * 60 * 1000
      ).toLocaleDateString('en-US'),
      dataCollectorCompare: [],
      loading: false,
      headers: [
        { text: 'AGREEMENTID', sortable: false, align: 'center' },
        { text: 'AGREEMENTNO', sortable: false, align: 'center' },
        { text: 'TXN_DATE', sortable: false, align: 'center' },
        { text: 'TXN_AMT', sortable: false, align: 'center' },
        { text: 'COLLECTOR', sortable: false, align: 'center' },
        { text: 'CHANNEL', sortable: false, align: 'center' },
        { text: 'RESULT_MSG', sortable: false, align: 'center' },
        { text: 'COMPARE_DATE', sortable: false, align: 'center' }
      ],
      interval: null,
      isProcess: false
    };
  },
  computed: {},
  watch: {
    isUseupdateDt() {
      if (this.isUseupdateDt == true) {
        this.isUsetxnDt = false;
      } else {
        this.isUsetxnDt = true;
      }
    },
    isUsetxnDt() {
      if (this.isUsetxnDt) {
        this.isUseupdateDt = false;
      } else {
        this.isUseupdateDt = true;
      }
    }
  },
  created() {
    this.isProcessAction();
  },

  methods: {
    ...mapActions('collCompare', [
      'collectorCompare',
      'getCollectorCompare',
      'exportFunc',
      'getIsProcess'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),

    async isProcessAction() {
      this.interval = setInterval(async () => {
        var rs = await this.getIsProcess();
        this.isProcess = rs.data;
      }, 1000);
    },
    async genSap() {
      this.loading = true;
      var param = {
        compareDt: this.compareDate
      };
      var rs = await this.collectorCompare(param);
      if (rs.success) {
        this.getData();
        this.showSuccessMsg(rs.message);
      } else {
        this.showErrorMsg(rs.message);
      }
      this.loading = false;
    },
    async getData() {
      if (this.isProcess == false) {
        clearInterval(this.interval);
      }
      this.loading = true;

      var param = {
        agreementNo: this.agreementNo,
        collector: this.collector.selected,
        channel: this.channel.selected,
        resultMsg: this.rsMgs.selected,
        agreementId: this.agreementId
      };
      if (this.isUseupdateDt) {
        param.processDt = this.compareDate;
      } else {
        param.processDt = null;
      }
      if (this.isUsetxnDt) {
        param.txnDt = this.txnDate;
      } else {
        param.txnDt = null;
      }
      var result = await this.getCollectorCompare(param);
      if (result.success) {
        this.dataCollectorCompare = result.data;
      }
      this.loading = false;
    },

    exportDataGenSap() {
      this.exportData(
        this.dataCollectorCompare,
        MainConstant.headerDataCollCompare,
        'CollectorCompare '
      );
    },

    async exportData(dataExport, header, fileName) {
      const today = new Date();
      const yyyy = today.getFullYear();
      let mm = today.getMonth() + 1; // Months start at 0!
      let dd = today.getDate();

      if (dd < 10) dd = '0' + dd;
      if (mm < 10) mm = '0' + mm;

      const hourFileName = mm + dd + yyyy;
      var exporter;
      if (dataExport.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      await this.exportFunc();
      exporter = new Exporter(fileName + hourFileName);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = dataExport;
      exporter.addSheet(header, data, 'sheet_1');
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
.changeColorRow {
  background-color: rgb(215, 215, 44);
}

.fixColumn {
  min-width: 200px;
  max-width: 200px;
  word-wrap: break-word;
}
</style>
