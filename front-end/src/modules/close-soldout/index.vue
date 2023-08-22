<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Close Soldout</span>
      </v-card-title>
    </v-card>
    <v-card>
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs2>
            <v-text-field
              v-model="agreementId"
              :disabled="workingTime"
              label="AgreementID"
            ></v-text-field>
            <div
              v-show="showMessageRequied"
              style="font-size: 12px !important; color: red; margin-top: -15px"
            >
              Agreement ID Required
            </div>
          </v-flex>
          <v-flex xs5>
            <v-text-field
              v-model="fileName"
              :disabled="workingTime"
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
              :disabled="workingTime"
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="getData"
              >Search</v-btn
            >
            <v-btn
              :disabled="workingTime"
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="closeAppBtn"
              >Close</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataCloseSoldout"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-center">{{ props.item.agreementNo }}</td>
                  <td class="text-xs-center">{{ props.item.cusName }}</td>
                  <td class="text-xs-center">{{ props.item.status }}</td>
                  <td class="text-xs-center">{{ props.item.npaStage }}</td>
                  <td class="text-xs-center">{{ props.item.product }}</td>
                  <td class="text-xs-center">{{ props.item.closureDt }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <popupConfirm
          :show="showPupopConfirm"
          :param="paramForClose"
          @close="handleCloseConfirm"
          @save="closeApp"
        />
      </v-container>
    </v-card>
    <v-dialog v-model="dialogNotify" hide-overlay persistent width="300">
      <v-card color="#00695c" dark>
        <v-card-text>
          Please come back at 07:00 to 22:30!
          <v-progress-linear
            indeterminate
            color="white"
            class="mb-0"
          ></v-progress-linear>
        </v-card-text>
      </v-card>
    </v-dialog>
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
      workingTime: false,
      dialogNotify: false,
      showMessageRequied: false,
      paramForClose: [],
      showPupopConfirm: false,
      dataCloseSoldout: [],
      dialog: false,
      fileName: '',
      fileUrl: '',
      loading: false,
      agreementId: null,
      headers: [
        { text: 'Agreement ID', sortable: false, align: 'center' },
        { text: 'Agreement No', sortable: false, align: 'center' },
        { text: 'Cus Name', sortable: false, align: 'center' },
        { text: 'Status', sortable: false, align: 'center' },
        { text: 'NPA_STAGEID', sortable: false, align: 'center' },
        { text: 'Product', sortable: false, align: 'center' },
        { text: 'Closure Date', sortable: false, align: 'center' }
      ]
    };
  },
  computed: {},
  watch: {
    agreementId() {
      if (this.agreementId != null || this.agreementId != '') {
        this.showMessageRequied = false;
      }
    }
  },
  created() {
    this.workingTimeFunc();
  },

  methods: {
    ...mapActions('closeSoldout', [
      'loadingDetailsData',
      'closeApp',
      'getCloseSoldout'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    workingTimeFunc() {
      var now = moment(new Date()).format('HH:mm');
      if (now.toString() >= '07:00' && now.toString() <= '22:30') {
        this.workingTime = false;
        this.dialogNotify = false;
      } else {
        this.workingTime = true;
        this.dialogNotify = true;
      }
    },
    editItem(item) {
      this.agreementIdUpdate = item.agreementId;
      this.waveOffAmount = item.waveOffAmount;
    },
    async getData() {
      this.workingTimeFunc();
      if (this.agreementId == null || this.agreementId == '') {
        this.showMessageRequied = true;
      } else {
        this.loading = true;
        var param = [];
        param.push({ agreementId: this.agreementId });
        var result = await this.getCloseSoldout(param);
        if (result.success) {
          this.dataCloseSoldout = result.data;
        } else {
          this.showWarningMsg(result.message);
        }
        this.loading = false;
      }
    },

    async closeAppBtn() {
      this.workingTimeFunc();
      if (this.dataCloseSoldout.length <= 0) {
        this.showErrorMsg('Have no data for close!');
        return;
      }
      this.paramForClose = this.dataCloseSoldout;
      this.showPupopConfirm = true;
    },
    async loadingData() {
      this.loading = true;
      var formData = new FormData();
      formData.append('file', this.fileUrl);
      var result = await this.loadingDetailsData(formData);
      if (result.data.ErrorData != null) {
        this.exportData(result.data.ErrorData);
        this.$refs.fileExcel.value = null;
      } else if (result.data.checkRcr != null) {
        this.showWarningMsg(result.data.checkRcr);
        this.$refs.fileExcel.value = null;
      } else {
        this.dataCloseSoldout = result.data.LoadingData;
        this.$refs.fileExcel.value = null;
      }
      this.loading = false;
    },
    pickFile() {
      this.workingTimeFunc();
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
          this.loadingData();
        });
      } else {
        this.fileName = '';
      }
    },
    handleCloseConfirm() {
      this.showPupopConfirm = false;
    },
    async closeApp(item) {
      if (item.success) {
        this.fileUrl = '';
        this.fileName = '';
        this.dataCloseSoldout = [];
        this.showSuccessMsg(item.message);
      } else {
        this.exportData(item.data);
        this.showWarningMsg(item.message);
      }
      this.showPupopConfirm = false;
    },
    exportData(dataError) {
      var filename = Date.now();
      var exporter;
      if (dataError.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      exporter = new Exporter('Data Failed ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = dataError;
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
<style scoped>
.changeColorRow {
  background-color: rgb(215, 215, 44);
}
</style>
