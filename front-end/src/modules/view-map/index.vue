<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>View Manual Entries</span>
      </v-card-title>
    </v-card>
    <v-card min-height="80vh" style="padding-bottom: 20px;">
      <v-container fluid grid-list-md>
        <v-layout row wrap>
          <v-flex xs1>
            <v-text-field v-model="batchId" label="Batch Id"></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-text-field
              v-model="agreementId"
              label="Agreement Id"
            ></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-text-field v-model="sapGl" label="Sap GL"></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-text-field v-model="remarks" label="Remarks"></v-text-field>
          </v-flex>
          <v-flex xs3>
            <v-select
              v-model="trans.selected"
              :items="trans.items"
              label="Trans Type"
              item-value="transCd"
              item-text="transDesc"
            ></v-select>
          </v-flex>
          <v-flex xs2>
            <v-select
              v-model="channel.selected"
              :items="channel.items"
              label="Channel"
              item-value="channelCd"
              item-text="channelDesc"
            >
            </v-select>
          </v-flex>
          <v-flex xs1>
            <v-select
              v-model="sapYn.selected"
              :items="sapYn.items"
              label="SAP YN"
            ></v-select>
          </v-flex>
          <v-flex xs2>
            <v-layout align-center>
              <v-checkbox v-model="isUseTransDt"></v-checkbox>
              <date-picker
                :disabled-date="!isUseTransDt"
                :disabled-param="!isUseTransDt"
                v-model="transDt"
                label="Trans Date"
                icon="event"
            /></v-layout>
          </v-flex>
          <v-flex xs2 style="padding-left: 20px">
            <v-layout align-center>
              <v-checkbox v-model="isUseupdateDt"></v-checkbox>
              <date-picker
                :disabled-date="!isUseupdateDt"
                :disabled-param="!isUseupdateDt"
                v-model="updateDt"
                label="Update Date"
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
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataGenSAP"
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
                  <td class="text-xs-center">{{ props.item.batchId }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.transDesc }}
                  </td>
                  <td class="text-xs-left">{{ props.item.channelDesc }}</td>
                  <td class="text-xs-center">
                    {{ props.item.agreementId }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.debitAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.creditAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">
                    {{ props.item.transDt }}
                  </td>
                  <td class="text-xs-right">{{ props.item.sapGl }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.remarks }}
                  </td>
                  <td class="text-xs-center">{{ props.item.leaVoucherId }}</td>
                  <td class="text-xs-center fixColumn">
                    {{ props.item.updateDt }}
                  </td>
                  <td class="text-xs-center">
                    {{ props.item.sapYn }}
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
import _ from 'lodash';
export default {
  components: {
    DatePicker,
    Loading
  },

  data() {
    return {
      agreementId: null,
      batchId: null,
      sapGl: null,
      remarks: null,
      isUseTransDt: false,
      isUseupdateDt: true,
      transDt: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      updateDt: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      dataGenSAP: [],
      loading: false,
      trans: {
        items: [{ transCd: null, transDesc: 'All' }],
        selected: null
      },
      sapYn: {
        items: ['All', 'Y', 'N'],
        selected: 'All'
      },
      channel: {
        items: [{ channelCd: null, channelDesc: 'All' }],
        selected: null
      },
      headers: [
        { text: 'BatchId', sortable: false, align: 'center' },
        { text: 'Trans Type', sortable: false, align: 'center' },
        { text: 'Channel', sortable: false, align: 'center' },
        { text: 'AgreementId', sortable: false, align: 'center' },
        { text: 'Debit Amt', sortable: false, align: 'center' },
        { text: 'Credit Amt', sortable: false, align: 'center' },
        { text: 'Trans Date', sortable: false, align: 'center' },
        { text: 'SapGL', sortable: false, align: 'center' },
        { text: 'Remarks', sortable: false, align: 'center' },
        { text: 'Lea VoucherId', sortable: false, align: 'center' },
        { text: 'Update Date', sortable: false, align: 'center' },
        { text: 'SapYN', sortable: false, align: 'center' }
      ]
    };
  },
  computed: {},
  watch: {},
  created() {
    this.loadingData();
  },

  methods: {
    ...mapActions('sapUploadReconciling', ['getInitData']),
    ...mapActions('viewMap', ['getDataViewMap', 'exportFunc']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async loadingData() {
      var result = await this.getInitData();
      this.trans.items = this.trans.items.concat(result.data.trans);
      this.channel.items = this.channel.items.concat(result.data.channel);
    },
    async getData() {
      if (
        _.isEmpty(this.agreementId) &&
        _.isEmpty(this.batchId) &&
        _.isEmpty(this.sapGl) &&
        this.isUseTransDt == false &&
        this.isUseupdateDt == false
      ) {
        this.showWarningMsg(
          'Please choose (Agreemen tId, Batch Id, Sap GL, Trans Date, Update Date) for search.'
        );
        return;
      }

      this.loading = true;
      var param = {
        agreementId: this.agreementId,
        batchId: this.batchId,
        sapGl: this.sapGl,
        remarks: this.remarks,
        sapYn: this.sapYn.selected,
        channelCd:
          this.channel.selected == null
            ? null
            : this.channel.items.find(
                element => element.channelDesc == this.channel.selected
              ).channelCd,
        transType:
          this.trans.selected == null
            ? null
            : this.trans.items.find(
                element => element.transDesc == this.trans.selected
              ).transType
      };
      if (this.isUseTransDt) {
        param.transDt = this.transDt;
      } else {
        param.transDt = null;
      }
      if (this.isUseupdateDt) {
        param.updateDt = this.updateDt;
      } else {
        param.updateDt = null;
      }
      var result = await this.getDataViewMap(param);
      if (result.success) {
        this.dataGenSAP = result.data;
      }
      this.loading = false;
    },

    exportDataGenSap() {
      this.exportData(
        this.dataGenSAP,
        MainConstant.headerDataGenSap,
        'View_manual_Entries '
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
