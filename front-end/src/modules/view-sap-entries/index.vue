<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>View SAP Entries</span>
      </v-card-title>
    </v-card>
    <v-card min-height="80vh" style="padding-bottom: 20px;">
      <v-container fluid grid-list-md>
        <v-layout row wrap>
          <v-flex xs2>
            <v-text-field v-model="sapGl" label="Sap GL"></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-text-field v-model="remarks" label="Remarks"></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-select
              v-model="sapYn.selected"
              :items="sapYn.items"
              label="Sap YN"
            >
            </v-select>
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
          <v-flex xs2>
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
              @click="exportdataSapEntries"
            >Export</v-btn
            >
          </v-flex>
          <v-layout class="text-lg-right" row wrap>
            <v-flex v-if="!isProcess" xs7 offset-xs5 offset-md2 offset-lg5>
              <v-btn
                small
                color="rgb(0, 105, 92)"
                class="white--text"
                @click="showPopupConfirm"
              >Push SAP
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
              :items="dataSapEntries"
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
                  <td class="text-xs-center">{{ props.item.glAcct }}</td>
                  <td class="text-xs-center">{{ props.item.acctNm }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.debit).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.credit).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.remarks }}
                  </td>
                  <td class="text-xs-center">
                    {{ props.item.roDept }}
                  </td>
                  <td class="text-xs-center">{{ props.item.budgetCd }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.descVn }}
                  </td>
                  <td class="text-xs-center">{{ props.item.groupGl }}</td>
                  <td class="text-xs-center fixColumn">
                    {{ props.item.txnDt }}
                  </td>
                  <td class="text-xs-center fixColumn">
                    {{ props.item.runDt }}
                  </td>
                  <td class="text-xs-center fixColumn">
                    {{ props.item.sapYn }}
                  </td>
                  <td class="text-xs-center fixColumn">
                    {{ props.item.usrLg }}
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <popupConfirm
          :show="showPupopConfirm"
          @close="handleCloseConfirm"
          @save="actionPushSap"
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
import _ from 'lodash';
export default {
  components: {
    popupConfirm,
    DatePicker,
    Loading
  },

  data() {
    return {
      paramForDelete: [],
      showPupopConfirm: false,
      agreementId: null,
      batchId: null,
      sapGl: null,
      remarks: null,
      isUseTransDt: false,
      isUseupdateDt: true,
      transDt: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      updateDt: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      dataSapEntries: [],
      loading: false,
      trans: {
        items: [{ transCd: null, transDesc: 'All' }],
        selected: null
      },
      sapYn: {
        items: ['All', 'Y', 'N'],
        selected: 'All'
      },
      headers: [
        { text: 'SAPGL', sortable: false, align: 'center' },
        { text: 'ACCTNAME', sortable: false, align: 'center' },
        { text: 'DEBIT', sortable: false, align: 'center' },
        { text: 'CREDIT', sortable: false, align: 'center' },
        { text: 'REMARKS', sortable: false, align: 'center' },
        { text: 'RO_DEPT', sortable: false, align: 'center' },
        { text: 'BUDGET_CODE', sortable: false, align: 'center' },
        { text: 'DESC_VIETNAMESE', sortable: false, align: 'center' },
        { text: 'GROUPGL', sortable: false, align: 'center' },
        { text: 'TRANS DATE', sortable: false, align: 'center' },
        { text: 'UPDATE DATE', sortable: false, align: 'center' },
        { text: 'SAP YN', sortable: false, align: 'center' },
        { text: 'USER PUSH SAP', sortable: false, align: 'center' }
      ],
      interval: null,
      isProcess: false
    };
  },
  computed: {},
  watch: {},
  created() {
    this.loadingData();
    this.isProcessAction();
  },

  methods: {
    ...mapActions('sapUploadReconciling', ['getInitData']),
    ...mapActions('viewSapEntries', [
      'getDataViewSapEntries',
      'exportFunc',
      'getIsProcess',
      'putSap'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),

    // async putSapBtn(){
    //   this.loading = true;
    //   var rs = await this.putSap();
    //   if (rs.success) {
    //     this.getData();
    //     this.showSuccessMsg(rs.message);
    //   } else {
    //     this.showErrorMsg(rs.message);
    //   }
    //   this.loading = false;
    // },

    async isProcessAction() {
      this.interval = setInterval(async () => {
        var rs = await this.getIsProcess();
        this.isProcess = rs.data;
      }, 1000);
    },

    async loadingData() {
      var result = await this.getInitData();
      this.trans.items = this.trans.items.concat(result.data.trans);
    },
    async getData() {
      if (this.isProcess == false) {
        clearInterval(this.interval);
      }
      this.loading = true;
      var param = {
        agreementId: this.agreementId,
        batchId: this.batchId,
        sapGl: this.sapGl,
        sapYn: this.sapYn.selected,
        remarks: this.remarks,
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
      var result = await this.getDataViewSapEntries(param);
      if (result.success) {
        this.dataSapEntries = result.data;
      }
      this.loading = false;
    },

    exportdataSapEntries() {
      this.exportData(
        this.dataSapEntries,
        MainConstant.headerDataSapEntries,
        'View_SAP_Entries '
      );
    },
    showPopupConfirm() {
      this.showPupopConfirm = true;
    },
    handleCloseConfirm() {
      this.showPupopConfirm = false;
    },
    actionPushSap(item) {
      if (item.success) {
        this.showPupopConfirm = false;
        this.getData();
        this.showSuccessMsg(item.message);
      } else {
        this.showPupopConfirm = false;
        this.getData();
        this.showErrorMsg(item.message);
      }
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
