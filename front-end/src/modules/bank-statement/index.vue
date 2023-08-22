<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Bank Statement</span>
      </v-card-title>
    </v-card>
    <v-card>
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs2>
            <v-text-field
              v-model="acct"
              label="MAFC Acct"
            ></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-select
              :items="bank.items"
              v-model="bank.selected"
              label="Bank"
            ></v-select>
          </v-flex>
          <v-flex xs2>
            <date-picker
              v-model="fromDate"
              :smaller="toDate"
              label="Report Date From"
              icon="event"
            />
          </v-flex>
          <v-flex xs2>
            <date-picker
              v-model="toDate"
              :greater="fromDate"
              label="Report Date To"
              icon="event"
            />
          </v-flex>
          <v-flex xs2>
            <v-text-field
              v-model="sapAcct"
              label="SAP"
            ></v-text-field>
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="syncData"
              ><i class="material-icons">sync</i></v-btn
            >
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
              :items="dataBankStatement"
              :rows-per-page-items="[10]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td>{{ props.item.no }}</td>
                  <td>{{ props.item.bankName }}</td>
                  <td>{{ props.item.fileName }}</td>
                  <td>{{ props.item.acct }}</td>
                  <td>{{ props.item.balance }}</td>
                  <td>{{ props.item.statmentDtInFileName }}</td>
                  <td>{{ props.item.statmentDt }}</td>
                  <td>{{ props.item.sapAcct }}</td>
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
      bank:{
        selected:null,
        items:["All"]
      },
      acct:null,
      sapAcct:null,
      fromDate: new Date(
        new Date().getTime() - 24 * 60 * 60 * 1000
      ).toLocaleDateString('en-US'),
      toDate: new Date(
      ).toLocaleDateString('en-US'),
      loading: false,
      dataBankStatement: [],
      headers: [
        { text: 'No', sortable: false },
        { text: 'BANK NAME', sortable: false },
        { text: 'FILE NAME', sortable: false },
        { text: 'MAFC ACCT', sortable: false },
        { text: 'BALANCE', sortable: false },
        { text: 'STATEMENT DATE', sortable: false },
        { text: 'REPORT DATE', sortable: false },
        { text: 'SAP ACCT', sortable: false }
      ]
    };
  },
  computed: {
  },
  watch: {
    dataBankStatement(){
      for(let i = 0; i< this.dataBankStatement.length; i++){
        this.dataBankStatement[i].no = i+1
      }
    }
  },
  created() {
    this.loadingData()
  },

  methods: {
    ...mapActions('bankStatement', [
      'getDataBank',
      'getDataBankStatement',
      "SyncFTP"
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async syncData(){
      this.loading = true
      var result = await this.SyncFTP()
      if(result.success){
        this.loadingData()
        this.showSuccessMsg("Sync data FTP success")
        this.loading = false
      }else{
        this.showErrorMsg("Sync data FTP fail")
        this.loading = false
      }
    },
    async loadingData(){
      var result = await this.getDataBank()
      this.bank.items = this.bank.items.concat(result.data)
    },
    async getData() {
      this.loading = true;
      var param = {
        acct: this.acct,
        bankName: this.bank.selected,
        sapAcct: this.sapAcct,
        fromDt: this.fromDate,
        toDt: this.toDate
      };

      const result = await this.getDataBankStatement(param);
      if (result.data != null) {
        this.dataBankStatement = result.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async exportReport() {
      var filename = Date.now();
      var exporter;
      if (this.dataBankStatement.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      exporter = new Exporter('Bank Statement ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = this.dataBankStatement;
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
