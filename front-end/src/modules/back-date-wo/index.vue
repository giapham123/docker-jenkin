<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Back Date WO</span>
      </v-card-title>
    </v-card>
    <v-card min-height="80vh" style="padding-bottom: 20px;">
      <v-container fluid grid-list-md>
        <v-layout row wrap>
          <v-flex xs2>
            <v-layout align-center>
              <date-picker
                v-model="backDate"
                label="WO/Revert_WO Date"
                icon="event"
            /></v-layout>
          </v-flex>
          <v-flex xs3 style="margin-top: -4px;">
            <v-select
              v-model="backType.selected"
              :items="backType.items"
              label="Type"
              item-value="code"
              item-text="name"
            ></v-select>
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
              @click="exportDataSap"
            >Export</v-btn
            >
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="processBackDateWO('BDWO')"
            >Back Date WO</v-btn
            >
            <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="processBackDateWO('BDRWO')"
            >Back Date Revert WO</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataBackDate"
              :rows-per-page-items="[10]"
              item-key="agreementId"
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
                <tr
                  :active="props.selected"
                  @click="props.selected = !props.selected"
                >
                  <td class="text-xs-center">{{ props.item.agreementId }}</td>
                  <td class="text-xs-center">{{ props.item.backDt }}</td>
                  <td class="text-xs-center">{{ props.item.uptDt }}</td>
                  <td class="text-xs-center">{{ props.item.runYn }}</td>
                  <td class="text-xs-left">{{ props.item.errorMsg }}</td>
                  <td class="text-xs-left">{{ props.item.userLogin }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <popupBackdate
          :param="paramPopup"
          :show="showPupopBackdate"
          @close="handleClosePopupBackdate"
          @save="processBackDateWOAfter"
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
import popupBackdate from './popup-backdate.vue';
export default {
  components: {
    DatePicker,
    popupBackdate,
    Loading
  },

  data() {
    return {
      backType: {
        selected: '',
        items: [
          {
            code: '',
            name: 'All'
          },
          {
            code: 'BDWO',
            name: 'Back Date WO'
          },
          {
            code: 'BDRWO',
            name: 'Back Date Revert WO'
          }
        ]
      },
      backDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      paramPopup: {},
      showPupopBackdate: false,
      dataBackDate: [],
      loading: false,
      selected: [],
      headers: [
        { text: 'AgreementId', sortable: false, align: 'center' },
        { text: 'WO/Revert_WO Date', sortable: false, align: 'center' },
        { text: 'Process Date', sortable: false, align: 'center' },
        { text: 'Proccess YN', sortable: false, align: 'center' },
        { text: 'Error Detail', sortable: false, align: 'center' },
        { text: 'User Process', sortable: false, align: 'center' }
      ]
    };
  },
  computed: {},
  watch: {},
  created() {},

  methods: {
    ...mapActions('backDateWo', ['getDataBackDate', 'exportFunc']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),

    processBackDateWO(item) {
      if (item == 'BDWO') {
        this.paramPopup = {
          type: item,
          title: 'Back Date WO'
        };
      } else {
        this.paramPopup = {
          type: item,
          title: 'Back Date Revert WO'
        };
      }

      this.showPupopBackdate = true;
    },
    async processBackDateWOAfter(item) {
      if (item.success) {
        this.getData();
        this.showSuccessMsg(item.message);
        this.showPupopBackdate = false;
      } else {
        this.showWarningMsg(item.message);
        this.showPupopBackdate = true;
      }
    },
    async handleClosePopupBackdate() {
      this.showPupopBackdate = false;
    },
    async getData() {
      this.loading = true;
      var param = {
        date: this.backDate,
        type: this.backType.selected
      };
      var result = await this.getDataBackDate(param);
      if (result.success) {
        this.dataBackDate = result.data;
      } else {
        this.dataBackDate = [];
      }
      this.loading = false;
    },

    exportDataSap() {
      this.exportData(
        this.dataBackDate,
        MainConstant.headersPendingDisbur,
        'Data Pending Disbursement '
      );
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
