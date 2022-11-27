<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Waive Off Amount</span>
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
              @click="uploadBtn"
            >Upload File</v-btn
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
              :items="dataWaveOffAmountHis"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.no }}</td>
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-right">
                    {{
                      String(props.item.waveOffAmount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="justify-center layout px-0">
                    <v-icon small class="mr-2" @click="editItem(props.item)">
                      edit
                    </v-icon>
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex xs2>
            <v-text-field
              v-model="agreementIdUpdate"
              label="Agreement ID"
              disabled
            ></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-text-field
              v-model="waveOffAmount"
              :hint="`${suggestPrice.toString()}`"
              label="Waive Off Amount"
              persistent-hint
            ></v-text-field>
          </v-flex>
          <v-flex xs2>
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="updateBtn"
            >Update</v-btn
            >
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
const format = num =>
  String(num).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
export default {
  components: {
    DatePicker,
    Loading
  },

  data() {
    return {
      agreementIdUpdate: null,
      waveOffAmount: null,
      title: 'Image Upload',
      dialog: false,
      fileName: '',
      fileUrl: '',
      showMessageRequied: false,
      loading: false,
      agreementId: null,
      termDt: new Date().toLocaleDateString('en-US'),
      dataWaveOffAmountHis: [],
      headers: [
        { text: 'No', sortable: false, align: 'center' },
        { text: 'Agreement ID', sortable: false, align: 'center' },
        { text: 'Waive Off Amount', sortable: false, align: 'center' },
        { text: 'Actions', sortable: false, align: 'center' }
      ]
    };
  },
  computed: {
    suggestPrice() {
      if (this.waveOffAmount != null) {
        return String(this.waveOffAmount).replace(
          /(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g,
          '$1,'
        );
      }
      return 0;
    }
  },
  watch: {
    dataWaveOffAmountHis() {
      for (let i = 0; i < this.dataWaveOffAmountHis.length; i++) {
        this.dataWaveOffAmountHis[i].no = i + 1;
      }
    },
    agreementId() {
      if (this.agreementId != null || this.agreementId != '') {
        this.showMessageRequied = false;
      }
    }
  },
  created() {
    this.uploadFileFinish();
  },

  methods: {
    ...mapActions('waveoffamount', [
      'uploadWaveOffAmount',
      'getWaveOffAmountData',
      'updateWaveOffAmount',
      'uploadFileStatus',
      'exportData'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    changeFormat() {
      this.waveOffAmount = this.waveOffAmount
        .toString()
        .replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ',');
    },
    async uploadFileFinish() {
      var stopLoading = false;
      this.loading = true;
      while (!stopLoading) {
        var rs = await this.uploadFileStatus();
        stopLoading = rs.data;
      }
      if (stopLoading) {
        this.loading = false;
      }
    },
    editItem(item) {
      this.agreementIdUpdate = item.agreementId;
      this.waveOffAmount = item.waveOffAmount;
    },
    async getData() {
      this.loading = true;
      var param = {
        agreementId: this.agreementId
      };

      const result = await this.getWaveOffAmountData(param);
      if (result.data != null) {
        this.dataWaveOffAmountHis = result.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async exportReport() {
      var filename = Date.now();
      var exporter;
      if (this.dataWaveOffAmountHis.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      await this.exportData();
      exporter = new Exporter('Waive Off Amount ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = this.dataWaveOffAmountHis;
      exporter.addSheet(MainConstant.headersReport, data, 'sheet_1');
      const result = exporter.exportExcel();
      if (result.success) {
        console.log('Export file is successful');
      } else {
        console.error(result.message);
      }
      this.loading = false;
    },
    async uploadBtn() {
      if (this.fileUrl == '') {
        this.showWarningMsg('Please select file!');
        return;
      }
      this.loading = true;
      var formData = new FormData();
      formData.append('file', this.fileUrl);
      var result = await this.uploadWaveOffAmount(formData);
      if (result.success) {
        this.getData();
        this.fileUrl = '';
        this.fileName = '';
        this.showSuccessMsg(result.message);
        this.$refs.fileExcel.value = null;
      } else {
        this.showErrorMsg(result.message);
        this.$refs.fileExcel.value = null;
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
    async updateBtn() {
      this.loading = true;
      var formatAmount = this.waveOffAmount.replace(/,/gi, '');
      var param = {
        agreementId: this.agreementIdUpdate,
        waveOffAmount: formatAmount
      };
      var result = await this.updateWaveOffAmount(param);
      if (result.success) {
        this.getData();
        this.waveOffAmount = null;
        this.agreementIdUpdate = null;
        this.showSuccessMsg(result.message);
      }
      this.loading = false;
    }
  }
};
</script>
