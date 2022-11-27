<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Reject Upload File</span>
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
          <v-flex xs2>
            <date-picker
              v-model="fromDate"
              :smaller="toDate"
              label="Upload Date From"
              icon="event"
            />
          </v-flex>
          <v-flex xs2>
            <date-picker
              v-model="toDate"
              :greater="fromDate"
              label="Upload Date To"
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
              @click="exportReport"
            >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataRejectUploadFile"
              :search="search"
              :pagination.sync="pagination"
              :rows-per-page-items="[10]"
              hide-actions
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td>{{ props.item.no }}</td>
                  <td>{{ props.item.agreementId }}</td>
                  <td>{{ props.item.agreementNo }}</td>
                  <td>{{ props.item.batchId }}</td>
                  <td>{{ props.item.receiptNo }}</td>
                  <td>{{ props.item.chequeNo }}</td>
                  <td>{{ props.item.bankAcnum }}</td>
                  <td>{{ props.item.drawNon }}</td>
                  <td>{{ props.item.toWards }}</td>
                  <td>
                    {{
                      String(props.item.receiptAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>{{ props.item.receiptDate }}</td>
                  <td>{{ props.item.userId }}</td>
                  <td>{{ props.item.uploadDate }}</td>
                  <td class="fixedColumn">{{ props.item.reaSon }}</td>
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
              ></v-pagination>
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
      minPicker: null,
      search: '',
      loading: false,
      agreementId: null,
      fromDate: new Date(
        new Date().getTime() - 24 * 60 * 60 * 1000
      ).toLocaleDateString('en-US'),
      toDate: new Date(
        new Date().getTime() - 24 * 60 * 60 * 1000
      ).toLocaleDateString('en-US'),
      dataRejectUploadFile: [],
      showPagin: false,
      search: '',
      pagination: {},
      totalData: 0,
      page: 1,
      headers: [
        { text: 'No', sortable: false },
        { text: 'AGREEMENTID', sortable: false },
        { text: 'AGREEMENTNO', sortable: false },
        { text: 'BATCHID', sortable: false },
        { text: 'RECEIPTNO', sortable: false },
        { text: 'CHEQUE_NO', sortable: false },
        { text: 'BANKACNUM', sortable: false },
        { text: 'DRAWNON', sortable: false },
        { text: 'TOWARDS', sortable: false },
        { text: 'RECEIPT_AMT', sortable: false },
        { text: 'RECEIPT_DATE', sortable: false },
        { text: 'USERID', sortable: false },
        { text: 'UPLOAD_DATE', sortable: false },
        { text: 'REASON', sortable: false }
      ]
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
  watch: {},
  created() {
    // this.minPicker = this.parseDate(new Date(new Date().getTime() - 168*60*60*1000).toLocaleDateString('en-US'))
  },

  methods: {
    ...mapActions('rejectUploadFile', [
      'getRejectUploadFileData',
      'exportExcelFile'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    parseDate(date) {
      if (!date) return null;
      const [month, day, year] = date.split('/');
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    },
    async nextPages() {
      this.loading = true;
      var param = {
        agreementId: this.agreementId,
        fromDate: this.fromDate,
        toDate: this.toDate,
        page: this.page
      };

      const result = await this.getRejectUploadFileData(param);
      if (result.data != null) {
        this.dataRejectUploadFile = result.data.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async getData() {
      this.loading = true;
      var param = {
        agreementId: this.agreementId,
        fromDate: this.fromDate,
        toDate: this.toDate,
        page: 1
      };

      const result = await this.getRejectUploadFileData(param);
      if (result.data != null) {
        this.dataRejectUploadFile = result.data.data;
        this.totalData = result.data.totalPages;
        this.showPagin = true;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async exportReport() {
      if (this.dataRejectUploadFile.length == 0) {
        this.showWarningMsg('Please search data for export!');
        return;
      }
      this.loading = true;
      var param = {
        agreementId: this.agreementId,
        fromDate: this.fromDate,
        toDate: this.toDate
      };

      const result = await this.exportExcelFile(param);
      console.log(result);
      if (result.data != null) {
        var blob;
        blob = window.atob(result.data);
        let today = new Date().toISOString().slice(0, 10);
        var link = document.createElement('a');
        if (link.download !== undefined) {
          link.href = 'data:text/csv;charset=utf-8,' + encodeURI(blob);
          link.target = '_blank';
          link.download =
            'Reject Upload File Report ' + moment(today).format('DD/MM/YYYY');
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
.fixedColumn {
  min-width: 300px;
  max-width: 300px;
  word-wrap: break-word;
}
</style>
