<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Upload Bank Statement</span>
      </v-card-title>
    </v-card>
    <v-card min-height="80vh">
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs2>
            <v-text-field v-model="appId" label="AppId"></v-text-field>
            <div
              v-show="showMessageRequied"
              style="font-size: 12px !important; color: red; margin-top: -15px"
            >
              Agreement ID Required
            </div>
          </v-flex>
          <v-flex xs2>
            <v-text-field v-model="txnNo" label="TXN No"></v-text-field>
          </v-flex>
          <v-flex xs2>
            <date-picker
              v-model="fromDate"
              :smaller="toDate"
              label="Date From"
              icon="event"
            />
          </v-flex>
          <v-flex xs2>
            <date-picker
              v-model="toDate"
              :greater="fromDate"
              label="Date To"
              icon="event"
            />
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex xs5>
            <v-text-field
              v-model="fileName"
              label="Select file"
              prepend-icon="attach_file"
              @click="pickFile"
            ></v-text-field>
            <input
              ref="fileExcel"
              type="file"
              style="display: none"
              accept="*"
              @change="onFilePicked"
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
              @click="importFileBankStatement"
            >Import</v-btn
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
              :items="dataUploadBankStatement"
              :search="search"
              :pagination.sync="pagination"
              :rows-per-page-items="[10]"
              hide-actions
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
                  <td class="text-xs-center">{{ props.item.appId }}</td>
                  <td class="text-xs-center">{{ props.item.statementDate }}</td>
                  <td class="text-xs-left">{{ props.item.detail }}</td>
                  <td class="text-xs-center">{{ props.item.txnNo }}</td>
                  <td class="text-xs-center">{{ props.item.descAcc }}</td>
                  <td class="text-xs-center">
                    {{
                      String(props.item.debitAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
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
        <popupConfirm
          :show="showPupopConfirm"
          :param="fileName"
          @close="handleCloseConfirm"
          @save="uploadBankStatement"
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
      fromDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      toDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      showMessageRequied: false,
      paramForClose: [],
      showPupopConfirm: false,
      dataUploadBankStatement: [],
      fileName: '',
      fileUrl: '',
      loading: false,
      appId: null,
      txnNo: null,
      headers: [
        { text: 'AppId', sortable: false, align: 'center' },
        { text: 'Date', sortable: false, align: 'center' },
        { text: 'Detail', sortable: false, align: 'center' },
        { text: 'Txn no', sortable: false, align: 'center' },
        { text: 'Desc acc', sortable: false, align: 'center' },
        { text: 'Debit amount', sortable: false, align: 'center' }
      ],
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
    appId() {
      if (this.appId != null || this.appId != '') {
        this.showMessageRequied = false;
      }
    }
  },
  created() {},

  methods: {
    ...mapActions('uploadBankStatement', [
      'uploadBankStatementFile',
      'getDataBankStatement',
      'exportExcel'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    importFileBankStatement() {
      if (this.fileName == null || this.fileName == '') {
        this.showErrorMsg('Please attach file for import!');
        return;
      }
      this.showPupopConfirm = true;
    },
    async nextPages() {
      this.loading = true;
      var param = {
        appId: this.appId,
        txnNo: this.txnNo,
        fromDt: this.fromDate,
        toDt: this.toDate,
        page: this.page
      };
      var result = await this.getDataBankStatement(param);
      if (result.success) {
        this.dataUploadBankStatement = result.data.data;
        this.showPagin = true;
      } else {
        this.dataUploadBankStatement = [];
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async getData() {
      this.loading = true;
      this.page = 1;
      var param = {
        appId: this.appId,
        txnNo: this.txnNo,
        fromDt: this.fromDate,
        toDt: this.toDate,
        page: 1
      };
      var result = await this.getDataBankStatement(param);
      if (result.success) {
        this.dataUploadBankStatement = result.data.data;
        this.totalData = result.data.totalPages;
        this.showPagin = true;
      } else {
        this.dataUploadBankStatement = [];
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },

    pickFile() {
      this.$refs.fileExcel.click();
    },

    onFilePicked(e) {
      const files = e.target.files;
      if (files[0] !== undefined) {
        this.fileName = files[0].name;
        if (this.fileName.lastIndexOf('.') <= 0) {
          return;
        }
        const fr = new FileReader();
        fr.readAsDataURL(files[0]);
        fr.addEventListener('load', () => {
          this.fileUrl = files[0];
        });
      } else {
        this.fileName = '';
      }
    },
    handleCloseConfirm() {
      this.$refs.fileExcel.value = null;
      this.fileUrl = null;
      this.fileName = null;
      this.showPupopConfirm = false;
    },
    async uploadBankStatement() {
      this.loading = true;
      var formData = new FormData();
      formData.append('file', this.fileUrl);
      var result = await this.uploadBankStatementFile(formData);
      if (result.success) {
        this.$refs.fileExcel.value = null;
        this.fileUrl = null;
        this.fileName = null;
        this.dataUploadBankStatement = [];
        this.showSuccessMsg(result.message);
      } else {
        this.$refs.fileExcel.value = null;
        this.fileUrl = null;
        this.fileName = null;
        this.showPupopConfirm = false;
        this.showWarningMsg(result.message);
        if (result.data != null) {
          this.exportData(
            result.data,
            MainConstant.headersReport,
            'Data Failed '
          );
        }
      }
      this.showPupopConfirm = false;
      this.loading = false;
    },
    async exportDataBankStatement() {
      this.loading = true;
      var param = {
        appId: this.appId,
        txnNo: this.txnNo,
        fromDt: this.fromDate,
        toDt: this.toDate
      };
      var result = await this.exportExcel(param);
      if (result.data != null) {
        var blob;
        blob = window.atob(result.data);
        let today = new Date().toISOString().slice(0, 10);
        var link = document.createElement('a');
        if (link.download !== undefined) {
          link.href = 'data:text/csv;charset=utf-8,' + encodeURI(blob);
          link.target = '_blank';
          link.download =
            'Data Bank Statement ' + moment(today).format('DD/MM/YYYY');
          +'.csv';
          link.click();
          this.loading = false;
        }
      } else {
        this.showWarningMsg(result.message);
        this.loading = false;
      }
      this.loading = false;
    },
    exportData(dataExport, header, fileName) {
      var hourFileName = Date.now();
      var exporter;
      if (dataExport.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
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
</style>
