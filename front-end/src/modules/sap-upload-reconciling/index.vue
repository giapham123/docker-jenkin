<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Upload the reconciling result</span>
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
          <v-flex xs3>
            <v-select
              v-model="trans.selected"
              :items="trans.items"
              label="Trans Type"
              item-value="transCd"
              item-text="transDesc"
            ></v-select>
          </v-flex>
          <v-flex xs3>
            <v-select
              v-model="channel.selected"
              :items="channel.items"
              label="Channel"
              item-value="channelCd"
              item-text="channelDesc"
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
              <v-checkbox v-model="isUseUploadDt"></v-checkbox>
              <date-picker
                :disabled-date="!isUseUploadDt"
                :disabled-param="!isUseUploadDt"
                v-model="uploadDt"
                label="Upload Date"
                icon="event"
              />
            </v-layout>
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
              @click="importFileSapReconciling"
            >Upload</v-btn
            >
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="deleteDataSap"
            >Delete</v-btn
            >
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="exportDataSap"
            >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              v-model="selected"
              :headers="headers1"
              :items="dataReconcilingImport"
              :rows-per-page-items="[10]"
              select-all
              item-key="leaVoucherId"
              class="elevation-1 full_box_table"
            >
              <template v-slot:headers="props">
                <tr>
                  <th>
                    <v-checkbox
                      :input-value="props.all"
                      :indeterminate="props.indeterminate"
                      primary
                      hide-details
                      @click.stop="toggleAll"
                    ></v-checkbox>
                  </th>
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
                <tr
                  :active="props.selected"
                  @click="props.selected = !props.selected"
                >
                  <td>
                    <v-checkbox
                      :input-value="props.selected"
                      primary
                      hide-details
                    ></v-checkbox>
                  </td>
                  <td class="text-xs-center">{{ props.item.batchId }}</td>
                  <td class="text-xs-center">{{ props.item.txnId }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.transDesc }}
                  </td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.channelDesc }}
                  </td>
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-center">
                    {{
                      String(props.item.amount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">{{ props.item.transDt }}</td>
                  <td class="text-xs-right fixColumn">
                    {{ props.item.uploadDt }}
                  </td>
                  <td class="text-xs-center">{{ props.item.uploadBy }}</td>
                  <td class="text-xs-center">{{ props.item.genSapYN }}</td>
                  <td class="text-xs-right">{{ props.item.genSapDt }}</td>
                  <td class="text-xs-center">{{ props.item.genSapBy }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex>
            <b>Record Error</b>
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="exportRecordError"
            >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers2"
              :items="dataReconcilingImportTmp"
              :rows-per-page-items="[10]"
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
                  <td class="text-xs-center">{{ props.item.txnId }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.transDesc }}
                  </td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.channelDesc }}
                  </td>
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.amount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-right">{{ props.item.transDt }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.errorDesc }}
                  </td>
                  <td class="text-xs-right fixColumn">
                    {{ props.item.uploadDt }}
                  </td>
                  <td class="text-xs-center">{{ props.item.uploadBy }}</td>
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
      agreementId: null,
      batchId: null,
      isUseTransDt: false,
      isUseUploadDt: true,
      transDt: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      uploadDt: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      dataReconcilingImport: [],
      dataReconcilingImportTmp: [],
      fileName: '',
      fileUrl: '',
      loading: false,
      trans: {
        items: [{ transCd: null, transDesc: 'All' }],
        selected: null
      },
      channel: {
        items: [{ channelCd: null, channelDesc: 'All' }],
        selected: null
      },
      selected: [],
      headers1: [
        { text: 'BatchId', sortable: false, align: 'center' },
        { text: 'TxnId', sortable: false, align: 'center' },
        { text: 'Trans Type', sortable: false, align: 'center' },
        { text: 'Channel', sortable: false, align: 'center' },
        { text: 'AgreementId', sortable: false, align: 'center' },
        { text: 'Amount', sortable: false, align: 'center' },
        { text: 'Trans Date', sortable: false, align: 'center' },
        { text: 'Upload Date', sortable: false, align: 'center' },
        { text: 'Upload By', sortable: false, align: 'center' },
        { text: 'Gen SAP Y/N', sortable: false, align: 'center' },
        { text: 'Gen SAP Date', sortable: false, align: 'center' },
        { text: 'Gen SAP By', sortable: false, align: 'center' }
      ],
      headers2: [
        { text: 'BatchId', sortable: false, align: 'center' },
        { text: 'TxnId', sortable: false, align: 'center' },
        { text: 'Trans Type', sortable: false, align: 'center' },
        { text: 'Channel', sortable: false, align: 'center' },
        { text: 'AgreementId', sortable: false, align: 'center' },
        { text: 'Amount', sortable: false, align: 'center' },
        { text: 'Trans Date', sortable: false, align: 'center' },
        { text: 'Error Desc', sortable: false, align: 'center' },
        { text: 'Upload Date', sortable: false, align: 'center' },
        { text: 'Upload By', sortable: false, align: 'center' }
      ]
    };
  },
  computed: {},
  watch: {},
  created() {
    this.loadingData();
  },

  methods: {
    ...mapActions('sapUploadReconciling', [
      'uploadFile',
      'getInitData',
      'getDataUploadReconciling',
      'deteleDataImportSap',
      'exportFunc',
      'exportErr'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async loadingData() {
      var result = await this.getInitData();
      this.channel.items = this.channel.items.concat(result.data.channel);
      this.trans.items = this.trans.items.concat(result.data.trans);
    },
    async getData() {
      this.loading = true;

      var param = {
        agreementId: this.agreementId,
        batchId: this.batchId,
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
      if (this.isUseUploadDt) {
        param.uploadDt = this.uploadDt;
      } else {
        param.uploadDt = null;
      }
      var result = await this.getDataUploadReconciling(param);
      if (result.success) {
        this.dataReconcilingImport = result.data.uploadReconcilingImport;
        this.dataReconcilingImportTmp = result.data.uploadReconcilingImportTmp;
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
    async importFileSapReconciling() {
      if (this.fileName == null || this.fileName == '') {
        this.showErrorMsg('Please attach file for import!');
        return;
      }
      this.loading = true;
      try {
        var formData = new FormData();
        console.log(this.fileUrl);
        formData.append('file', this.fileUrl);
        var result = await this.uploadFile(formData);
        if (result.success) {
          this.$refs.fileExcel.value = null;
          this.fileUrl = null;
          this.fileName = null;
          this.dataReconcilingImportTmp = [];
          this.agreementId = null;
          this.batchId = null;
          this.isUseTransDt = false;
          this.isUseUploadDt = true;
          this.trans.selected = null;
          this.channel.selected = null;
          this.transDt = new Date(new Date().getTime()).toLocaleDateString(
            'en-US'
          );
          this.uploadDt = new Date(new Date().getTime()).toLocaleDateString(
            'en-US'
          );
          this.dataReconcilingImport = [];
          this.getData();
          this.showSuccessMsg(result.message);
        } else {
          this.$refs.fileExcel.value = null;
          this.fileUrl = null;
          this.fileName = null;
          this.showWarningMsg(result.message);
          if (result.data != null) {
            this.exportData(
              result.data,
              MainConstant.headersReport,
              'Data Failed '
            );
          }
        }
      } catch (err) {
        this.showErrorMsg(err);
      }
      this.loading = false;
    },
    async deleteDataSap() {
      this.loading = true;
      var dataHaveSapYNIsNull = '';
      for (let i = 0; i < this.selected.length; i++) {
        if (this.selected[i].genSapYN != null) {
          dataHaveSapYNIsNull +=
            this.selected[i].txnId + ':' + this.selected[i].agreementId + '; ';
        }
      }
      if (dataHaveSapYNIsNull != '') {
        this.showErrorMsg('Has been generate SAP');
        this.loading = false;
        return;
      } else {
        var result = await this.deteleDataImportSap(this.selected);
        if (result.success) {
          this.showSuccessMsg(result.message);
          this.getData();
        }
        this.loading = false;
      }
    },
    async exportDataSap() {
      await this.exportFunc();
      this.exportData(
        this.dataReconcilingImport,
        MainConstant.headerUploadReconciling,
        'Uploadreconcilingresult '
      );
    },
    async exportRecordError() {
      await this.exportErr();
      this.exportData(
        this.dataReconcilingImportTmp,
        MainConstant.headerRecordError,
        'Uploadreconcilingresult_error '
      );
    },
    toggleAll() {
      if (this.selected.length) this.selected = [];
      else this.selected = this.dataReconcilingImport.slice();
    },
    exportData(dataExport, header, fileName) {
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
      // await this.exportFunc();
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
