<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Collector transactions</span>
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
          <v-flex xs3>
            <v-select
              v-model="channel.selected"
              :items="channel.items"
              label="Channel"
            ></v-select>
          </v-flex>
          <v-flex xs2>
            <v-layout align-center>
              <v-checkbox v-model="isUseTransDt"></v-checkbox>
              <date-picker
                :disabled-date="!isUseTransDt"
                :disabled-param="!isUseTransDt"
                v-model="transDt"
                label="Transactions Date"
                icon="event"
            /></v-layout>
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
              @click="exportdataCollTrans"
            >Export</v-btn
            >
          </v-flex>
          <v-layout class="text-lg-right" row wrap>
            <v-flex v-if="!isTransAction" xs7 offset-xs5 offset-md2 offset-lg5>
              <v-btn
                small
                color="rgb(0, 105, 92)"
                class="white--text"
                @click="getTransactionsbtn"
              >Get Transactions
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
              :items="dataCollTrans"
              :rows-per-page-items="[10]"
              item-key="leaVoucherId"
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
                  <td class="text-xs-center fixColumn">
                    {{ props.item.transDt }}
                  </td>
                  <td class="text-xs-center">
                    {{ props.item.agreementId }}
                  </td>
                  <td class="text-xs-left">{{ props.item.cusNm }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.amount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>

                  <td class="text-xs-left">{{ props.item.channel }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <popupConfirm
          :param="messageErrorTrans"
          :show="showPupopConfirm"
          @close="handleCloseConfirm"
        />
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
import popupConfirm from './popup-confirm.vue';
import moment from 'moment';
export default {
  components: {
    DatePicker,
    Loading,
    popupConfirm
  },

  data() {
    return {
      messageErrorTrans: null,
      showPupopConfirm: false,
      isTransAction: false,
      agreementId: null,
      isUseTransDt: false,
      transDt: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      dataCollTrans: [],
      loading: false,
      channel: {
        items: [
          'All',
          'EPAY',
          'MOMOAPP',
          'MOMOMYFINACE',
          'ONEPAY',
          'PAYOO',
          'SHOPEEPAYAPP',
          'SHOPEEPAYCOUNTER',
          'SMARTNET',
          'VIETTELAPP',
          'VIETTELCOUNTER',
          'VIMO',
          'VNPAYBILLING',
          'VNPAYGATEWAY',
          'VNPOST',
          'VNPTPAY',
          'ZALOPAY'
        ],
        selected: 'All'
      },
      headers: [
        { text: 'Transactions Date', sortable: false, align: 'center' },
        { text: 'AgreementId', sortable: false, align: 'center' },
        { text: 'Customer Name', sortable: false, align: 'center' },
        { text: 'Amount', sortable: false, align: 'center' },
        { text: 'Channel', sortable: false, align: 'center' }
      ],
      interval: null
    };
  },
  computed: {},
  watch: {},
  beforeDestroy() {
    clearInterval(this.interval);
  },
  created() {
    this.isTransActions();
  },
  methods: {
    ...mapActions('collTrans', [
      'getCollTransData',
      'exportColl',
      'getTransactions',
      'getIsTrans'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),

    async isTransActions() {
      this.interval = setInterval(async () => {
        var rs = await this.getIsTrans();
        this.isTransAction = rs.data;
      }, 1000);
    },
    async getTransactionsbtn() {
      this.loading = true;
      try {
        var result = await this.getTransactions();
        if (result.success) {
          this.getData();
          this.showSuccessMsg(result.message);
        } else {
          this.showPupopConfirm = true;
          this.messageErrorTrans = result.message;
          // this.showErrorMsg(result.message);
        }
      } catch (err) {
        this.showErrorMsg('Wrong Format Data In Excel.');
      }
      this.loading = false;
    },
    showPopupConfirm() {
      this.showPupopConfirm = true;
    },
    handleCloseConfirm() {
      this.showPupopConfirm = false;
    },
    async getData() {
      if (this.isTransAction == false) {
        clearInterval(this.interval);
      }
      if (
        _.isEmpty(this.agreementId) &&
        this.channel.selected == 'All' &&
        this.isUseTransDt == false
      ) {
        this.showWarningMsg(
          'Please choose (Agreemen tId, Channel, Trans Date) for search'
        );
        return;
      }
      this.loading = true;

      var param = {
        agreementId: this.agreementId,
        channel: this.channel.selected
      };
      if (this.isUseTransDt) {
        param.transDt = this.transDt;
      } else {
        param.transDt = null;
      }
      var result = await this.getCollTransData(param);
      if (result.success) {
        this.dataCollTrans = result.data;
      }
      this.loading = false;
    },

    exportdataCollTrans() {
      this.exportData(
        this.dataCollTrans,
        MainConstant.headerDataCollTrans,
        'Collector_transactions '
      );
    },

    async exportData(dataExport, header, fileName) {
      this.loading = true;
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
        this.loading = false;
        return;
      }
      await this.exportColl();
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
