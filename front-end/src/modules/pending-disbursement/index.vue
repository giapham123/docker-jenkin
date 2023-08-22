<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Pending Disburment agreementIDs </span>
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
              @click="insertAgreementBtn"
            >Insert</v-btn
            >
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="deleteAgreementId"
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
          <v-layout class="text-lg-right" row wrap>
            <v-flex v-if="!isProcess" xs7 offset-xs5 offset-md2 offset-lg5>
              <v-btn
                small
                color="rgb(0, 105, 92)"
                class="white--text"
                @click="processBtn"
              >Process
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
              v-model="selected"
              :headers="headers"
              :items="dataPendingDisbursement"
              :rows-per-page-items="[10]"
              select-all
              item-key="agreementId"
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
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-center">{{ props.item.uptDt }}</td>
                  <td class="text-xs-center">{{ props.item.runYn }}</td>
                  <td class="text-xs-left">{{ props.item.errorMsg }}</td>
                  <td class="text-xs-center">{{ props.item.userLogin }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <popupConfirm
          :show="showPupopConfirm"
          :param="paramForDelete"
          @close="handleCloseConfirm"
          @save="deleteAgreementIdAfterSave"
        />
        <popupInsert
          :show="showPupopInsert"
          @close="handleCloseInsert"
          @save="insertAgreementId"
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
import moment from 'moment';
import popupConfirm from './popup-confirm.vue';
import popupInsert from './popup-insert.vue';
export default {
  components: {
    DatePicker,
    popupInsert,
    popupConfirm,
    Loading
  },

  data() {
    return {
      paramForDelete: [],
      showPupopConfirm: false,
      showPupopInsert: false,
      agreementId: null,
      dataPendingDisbursement: [],
      fileName: '',
      fileUrl: '',
      loading: false,
      selected: [],
      headers: [
        { text: 'AgreementId', sortable: false, align: 'center' },
        { text: 'Proccess Date', sortable: false, align: 'center' },
        { text: 'Proccess YN', sortable: false, align: 'center' },
        { text: 'Error Detail', sortable: false, align: 'center' },
        { text: 'Maker', sortable: false, align: 'center' }
      ],
      interval: null,
      isProcess: false
    };
  },
  computed: {},
  watch: {},
  created() {
    this.isProcessAction();
  },

  methods: {
    ...mapActions('pendingDisbursement', [
      'uploadFile',
      'getDataPendingDisbursement',
      'processPending',
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
    async processBtn() {
      this.loading = true;
      var rs = await this.processPending();
      if (rs.success) {
        this.getData();
        this.showSuccessMsg(rs.message);
      } else {
        this.showErrorMsg(rs.message);
      }
      this.loading = false;
    },
    deleteAgreementId() {
      this.paramForDelete = this.selected;
      this.showPupopConfirm = true;
    },
    handleCloseConfirm() {
      this.showPupopConfirm = false;
    },
    deleteAgreementIdAfterSave(item) {
      if (item.success) {
        this.selected = [];
        this.getData();
        this.showPupopConfirm = false;
        this.showSuccessMsg(item.message);
        this.exportData(
          item.data,
          MainConstant.headersPendingDisbur,
          'File agreementId process is delete '
        );
      } else {
        this.selected = [];
        this.getData();
        this.showPupopConfirm = false;
        if (item.data.length == this.selected.length) {
          this.showWarningMsg('All items have process Y');
          return;
        }
        this.showWarningMsg(item.message);
        if (item.data.length != 0) {
          this.exportData(
            item.data,
            MainConstant.headersPendingDisbur,
            'File agreementId process is delete '
          );
        }
      }
    },
    insertAgreementBtn() {
      this.showPupopInsert = true;
    },
    async insertAgreementId(item) {
      if (item.success) {
        this.getData();
        this.showSuccessMsg(item.message);
        this.showPupopInsert = false;
      } else {
        this.showWarningMsg(item.message);
        this.showPupopInsert = true;
      }
    },
    async handleCloseInsert() {
      this.showPupopInsert = false;
    },
    async getData() {
      if (this.isProcess == false) {
        clearInterval(this.interval);
      }
      this.loading = true;
      var param = {
        agreementId: this.agreementId == null ? '' : this.agreementId
      };
      var result = await this.getDataPendingDisbursement(param);
      if (result.success) {
        this.dataPendingDisbursement = result.data;
      } else {
        this.dataPendingDisbursement = [];
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
        formData.append('file', this.fileUrl);
        var result = await this.uploadFile(formData);
        if (result.success) {
          this.$refs.fileExcel.value = null;
          this.fileUrl = null;
          this.fileName = null;
          this.getData();
          this.agreementId = null;
          this.showSuccessMsg(result.message);
        } else {
          this.$refs.fileExcel.value = null;
          this.fileUrl = null;
          this.fileName = null;
          this.showWarningMsg(result.message);
          if (result.data != null) {
            this.exportData(
              result.data,
              MainConstant.headerErrorImport,
              'Data Failed '
            );
          }
        }
      } catch (err) {
        this.showErrorMsg(err);
      }
      this.loading = false;
    },

    exportDataSap() {
      this.exportData(
        this.dataPendingDisbursement,
        MainConstant.headersPendingDisbur,
        'Data Pending Disbursement '
      );
    },
    toggleAll() {
      if (this.selected.length) this.selected = [];
      else this.selected = this.dataPendingDisbursement.slice();
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
