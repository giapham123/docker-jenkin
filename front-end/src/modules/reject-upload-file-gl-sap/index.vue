<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Reject Upload File GL SAP</span>
      </v-card-title>
    </v-card>
    <v-card>
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs4>
            <v-text-field v-model="remarks" label="Remarks"></v-text-field>
          </v-flex>
          <v-flex xs2>
            <date-picker
              v-model="uploadDate"
              label="Upload Date"
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
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td>{{ props.item.no }}</td>
                  <td>{{ props.item.glAcct }}</td>
                  <td>{{ props.item.acctName }}</td>
                  <td>
                    {{
                      String(props.item.debit).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>
                    {{
                      String(props.item.credit).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>{{ props.item.remarks }}</td>
                  <td>{{ props.item.roDept }}</td>
                  <td>{{ props.item.descriptionvietnames }}</td>
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
export default {
  components: {
    DatePicker,
    Loading
  },

  data() {
    return {
      search: '',
      loading: false,
      remarks: null,
      uploadDate: new Date(
        new Date().getTime() - 24 * 60 * 60 * 1000
      ).toLocaleDateString('en-US'),
      dataRejectUploadFile: [],
      headers: [
        { text: 'No', sortable: false },
        { text: 'GLACCT', sortable: false },
        { text: 'ACCTNAME', sortable: false },
        { text: 'DEBIT', sortable: false },
        { text: 'CREDIT', sortable: false },
        { text: 'REMARKS', sortable: false },
        { text: 'RO DEPT', sortable: false },
        { text: 'DESCRIPTION VIETNAMESE', sortable: false }
      ]
    };
  },
  watch: {
    dataRejectUploadFile() {
      for (let i = 0; i < this.dataRejectUploadFile.length; i++) {
        this.dataRejectUploadFile[i].no = i + 1;
      }
    }
  },
  created() {},

  methods: {
    ...mapActions('rejectUploadGLSAPFile', [
      'getRejectUploadFileGLSAPData',
      'exportData'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async getData() {
      this.loading = true;
      var param = {
        remarks: this.remarks,
        uploadDate: this.uploadDate
      };

      const result = await this.getRejectUploadFileGLSAPData(param);
      if (result.data != null) {
        this.dataRejectUploadFile = result.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async exportReport() {
      var filename = Date.now();
      var exporter;
      if (this.dataRejectUploadFile.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      await this.exportData();
      exporter = new Exporter('Reject Upload File GL SAP ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = this.dataRejectUploadFile;
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
.fixedColumn {
  min-width: 300px;
  max-width: 300px;
  word-wrap: break-word;
}
</style>
