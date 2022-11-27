<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Cas Repayment Schedule</span>
      </v-card-title>
    </v-card>
    <v-card>
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs2>
            <v-text-field v-model="agreementId" label="AgreementID"></v-text-field>
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-btn small color="rgb(0, 105, 92)" class="white--text" @click="getData">Search</v-btn>
            <v-btn small color="rgb(0, 105, 92)" class="white--text" @click="exportReport">Export</v-btn>
          </v-flex>
        </v-layout>
        <v-divider></v-divider>
        <v-layout>
          <v-flex xs4>
            <v-layout>
              <v-card-text style="font-weight: bold;">Receivable_till_date: </v-card-text>
              <v-card-text>{{receivableTill}}</v-card-text>
            </v-layout>
          </v-flex>
          <v-flex xs4>
            <v-layout>
              <v-card-text style="font-weight: bold;">Not_Overdue_Principal:</v-card-text>
              <v-card-text>{{notOverdue}}</v-card-text>
            </v-layout>
          </v-flex>
          <v-flex xs4>
            <v-layout>
              <v-card-text style="font-weight: bold;">Early payment penalty:</v-card-text>
              <v-card-text>{{rsEarlyPayment}}</v-card-text>
            </v-layout>
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex xs4>
            <v-layout>
              <v-card-text style="font-weight: bold;">Rec_amount:</v-card-text>
              <v-card-text>{{recAmount}}</v-card-text>
            </v-layout>
          </v-flex>
          <v-flex xs4>
            <v-layout>
              <v-card-text style="font-weight: bold;">Waive off Covid:</v-card-text>
              <v-card-text>{{waiveOffCovid}}</v-card-text>
            </v-layout>
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex xs4>
            <v-layout>
              <v-card-text style="font-weight: bold;">Termination Amount:</v-card-text>
              <v-card-text>{{rsTerminalAmount}}</v-card-text>
            </v-layout>
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table :headers="headers" :items="casRepaymentScheduleData" :rows-per-page-items="[10,20]"
              class="elevation-1 full_box_table">
              <template v-slot:items="props">
                <tr>
                  <td>{{ props.item.agreementId }}</td>
                  <td>{{ props.item.instlNum }}</td>
                  <td>{{ props.item.dueDt }}</td>
                  <td>{{ String(props.item.instlAmt).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  <td>{{ String(props.item.prinComp).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  <td>{{ String(props.item.intComp).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  <td>{{ props.item.effRate }}</td>
                  <td>{{ String(props.item.balPrin).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  <td>{{ props.item.days }}</td>
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
import { Exporter } from '@chidoan/excel-utils';
import MainConstant from './constant_main';
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
      casRepaymentScheduleData: [],
      receivableTill:null,
      notOverdue:null,
      rsEarlyPayment:null,
      recAmount:null,
      waiveOffCovid:null,
      rsTerminalAmount:null,
      headers: [
        { text: 'AGREEMENT ID', value: 'no', sortable: false },
        { text: 'INSTLNUM', value: 'agreementNo', sortable: false },
        { text: 'DUEDATE', value: 'agreementId', sortable: false },
        { text: 'INSTLAMT', value: 'outDate', sortable: false },
        { text: 'PRINCOMP', value: 'amtFin', sortable: false },
        { text: 'INTCOMP', value: 'principleBf', sortable: false },
        { text: 'EFFRATE', value: 'interestBf', sortable: false },
        { text: 'BALPRIN', value: 'lppBf', sortable: false },
        { text: 'DAYS', value: 'principleAt', sortable: false }
      ]
    };
  },
  computed: {
  },

  created() {
  },

  methods: {
    ...mapActions('casRepaymentSchedule', ['getDataCasRepayment','exportData']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),

    async getData() {
      if(this.agreementId == null || this.agreementId.trim() == null || this.agreementId.trim() == ""){
        this.showErrorMsg("Please input agreementId for search")
        return
      }
      this.loading = true;
      var param = {
        agreementId: this.agreementId
      };

      const result = await this.getDataCasRepayment(param);
      if (result.data != null) {
        this.casRepaymentScheduleData = result.data.data;
        this.receivableTill = String(result.data.receivableTill).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
        this.notOverdue =String(result.data.notOverdue).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
        this.rsEarlyPayment =String(result.data.rsEarlyPayment).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
        this.recAmount = String(result.data.recAmount).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
        this.waiveOffCovid = String(result.data.waiveOffCovid).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
        this.rsTerminalAmount =String(result.data.rsTerminalAmount).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
      } else {
        this.showWarningMsg(result.message);
        this.loading = false;
      }
      this.loading = false;
    },
    async exportReport() {
      if (this.casRepaymentScheduleData.length == 0) {
        this.showWarningMsg('Please search data for export!');
        return;
      }
      this.loading = true;
      var param = {
        agreementId: this.agreementId
      };

      const result = await this.exportData(param);
      if (result.data != null) {
        var blob;
        blob = window.atob(result.data);
        let today = new Date().toISOString().slice(0, 10);
        var link = document.createElement('a');
        if (link.download !== undefined) {
          link.href = 'data:text/csv;charset=utf-8,' + encodeURI(blob);
          link.target = '_blank';
          link.download =
            'Termination SIM Report His ' + moment(today).format('DD/MM/YYYY');
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
