<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Return Booking Case</span>
      </v-card-title>
    </v-card>
    <v-card>
      <v-container fluid grid-list-md>
        <v-layout>
          <v-flex md1 xs1 style="padding-right: 50px;">
            <v-layout align-center>
              <v-select
                :items="type.items"
                v-model="type.selected"
                label="Type"
              ></v-select
            ></v-layout>
          </v-flex>
          <v-flex xs4>
            <v-layout align-center>
              <v-checkbox v-model="isUseDt"></v-checkbox>
              <date-picker
                v-model="fromDate"
                :smaller="toDate"
                :disabled-date="disableDt"
                :disabled-param="disableDt"
                label="From"
                icon="event"
              />
              <date-picker
                v-model="toDate"
                :greater="fromDate"
                :disabled-date="disableDt"
                :disabled-param="disableDt"
                label="To"
                icon="event"
              />
            </v-layout>
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
              @click="exportDataExcel"
            >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataReturnBookingCase"
              :rows-per-page-items="[10, 15, 20]"
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td class="text-xs-center">{{ props.item.appId }}</td>
                  <td class="text-xs-center">
                    {{
                      String(props.item.amtReturn).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.batchId }}</td>
                  <td class="text-xs-center">{{ props.item.uploadDt }}</td>
                  <td class="text-xs-center">{{ props.item.type }}</td>
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
      type: {
        selected: 'ALL',
        items: ['ALL', 'OV', 'PP']
      },
      disableDt: true,
      isUseDt: false,
      dataReturnBookingCase: [],
      loading: false,
      headers: [
        { text: 'APPID', sortable: false, align: 'center' },
        { text: 'AMT_Return', sortable: false, align: 'center' },
        { text: 'BatchID', sortable: false, align: 'center' },
        { text: 'Upload_Date', sortable: false, align: 'center' },
        { text: 'Type', sortable: false, align: 'center' }
      ],
      fromDate: new Date().toLocaleDateString('en-US'),
      toDate: new Date().toLocaleDateString('en-US')
    };
  },
  computed: {},
  watch: {
    isUseDt() {
      if (this.isUseDt) {
        this.disableDt = false;
      } else {
        this.disableDt = true;
      }
    }
  },
  created() {},

  methods: {
    ...mapActions('returnBookingCase', ['getReturnBookingCase', 'exportData']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),

    async getData() {
      this.loading = true;
      var fromDt = null;
      var toDt = null;
      if (this.isUseDt) {
        fromDt = this.fromDate;
        toDt = this.toDate;
      } else {
        fromDt = null;
        toDt = null;
      }
      var param = {
        fromDt: fromDt,
        toDt: toDt,
        type: this.type.selected
      };
      var result = await this.getReturnBookingCase(param);
      if (result.success) {
        if (result.data.length == 0) {
          this.dataReturnBookingCase = [];
          this.showWarningMsg('No data found!');
        } else {
          this.dataReturnBookingCase = result.data;
        }
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async exportReduceInterest() {
      var filename = Date.now();
      var exporter;
      if (this.dataReturnBookingCase.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      await this.exportData();
      exporter = new Exporter('Return Booking Case ' + filename);
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#4285F4'
      });
      var data = this.dataReturnBookingCase;
      exporter.addSheet(MainConstant.headersReport, data, 'sheet_1');
      const result = exporter.exportExcel();
      if (result.success) {
        console.log('Export file is successful');
      } else {
        console.error(result.message);
      }
      this.loading = false;
    },
    exportDataExcel() {
      this.exportReduceInterest();
    }
  }
};
</script>
<style scoped>
.changeColorRow {
  background-color: rgb(215, 215, 44);
}
</style>
