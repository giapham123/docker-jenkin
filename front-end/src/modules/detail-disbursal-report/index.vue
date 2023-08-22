<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Detail Disbursal Report</span>
      </v-card-title>
    </v-card>
    <v-card min-height="80vh">
      <v-container fluid grid-list-md>
        <v-layout ref="form">
          <v-flex xs2>
            <v-select
              v-model="lsProducts.selected"
              :items="lsProducts.items"
              label="Product"
            ></v-select>
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
              @click="exportDataBankStatement"
            >Export</v-btn
            >
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataDetailDisbursalReport"
              :rows-per-page-items="[10, 15, 20]"
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
                  <td class="text-xs-center">{{ props.item.disDt }}</td>
                  <td class="text-xs-center">{{ props.item.typeProduct }}</td>
                  <td class="text-xs-center">{{ props.item.appId }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.customerNm }}
                  </td>
                  <td class="text-xs-left fixColumn">{{ props.item.benNm }}</td>
                  <td class="text-xs-center">{{ props.item.agreeNo }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.bankNm }}
                  </td>
                  <td class="text-xs-center">{{ props.item.bankCd }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.bankBranch }}
                  </td>
                  <td class="text-xs-center">{{ props.item.bankAccNum }}</td>
                  <td class="text-xs-center">
                    {{
                      String(props.item.disAmt).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.partBank }}</td>
                  <td class="text-xs-left fixColumn">
                    {{ props.item.schemeNm }}
                  </td>
                  <td class="text-xs-center">
                    {{
                      String(props.item.disAmtIns).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td class="text-xs-center">{{ props.item.txnNo }}</td>
                  <td class="text-xs-center">{{ props.item.statementDt }}</td>
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
export default {
  components: {
    DatePicker,
    Loading
  },

  data() {
    return {
      fromDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      toDate: new Date(new Date().getTime()).toLocaleDateString('en-US'),
      dataDetailDisbursalReport: [],
      lsProducts: {
        selected: 'All Product',
        items: [
          'All Product',
          'SALPIL',
          'OCLPIL',
          'FALPIL',
          'MAEALPIL',
          'SELPIL',
          'TOPPIL',
          'TGDD',
          'FPT',
          'Cao Phong',
          'CDL',
          'Stock loan',
          'Auto Loan'
        ]
      },
      loading: false,
      headers: [
        { text: 'Date Dis (F1)', sortable: false, align: 'center' },
        { text: 'Product', sortable: false, align: 'center' },
        { text: 'AppID', sortable: false, align: 'center' },
        { text: 'Customer Name', sortable: false, align: 'center' },
        { text: 'Beneficiary Name', sortable: false, align: 'center' },
        { text: 'Agreement No', sortable: false, align: 'center' },
        { text: 'Bank Name', sortable: false, align: 'center' },
        { text: 'Bank Code', sortable: false, align: 'center' },
        { text: 'Bank Branch Name', sortable: false, align: 'center' },
        { text: 'Bank Account Number', sortable: false, align: 'center' },
        { text: 'Disbursal Amount', sortable: false, align: 'center' },
        { text: 'Partner Bank', sortable: false, align: 'center' },
        { text: 'Scheme Name', sortable: false, align: 'center' },
        { text: 'Disbursal Amount Ins', sortable: false, align: 'center' },
        { text: 'Txn No', sortable: false, align: 'center' },
        { text: 'Actual Date', sortable: false, align: 'center' }
      ]
    };
  },
  computed: {},
  watch: {},
  created() {},

  methods: {
    ...mapActions('detailDisbursalReport', [
      'getDataDetailDisbursalReport',
      'exportExcelFile'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async getData() {
      this.loading = true;
      var date1 = new Date(this.fromDate);
      var date2 = new Date(this.toDate);
      var Difference_In_Time = date2.getTime() - date1.getTime();
      var Difference_In_Days = Difference_In_Time / (1000 * 3600 * 24);
      if (Difference_In_Days > 31) {
        this.showWarningMsg('Please select in 1 month!');
        this.loading = false;
        return;
      }
      var param = {
        appId: this.appId,
        typeProduct: this.lsProducts.selected,
        fromDt: this.fromDate,
        toDt: this.toDate
      };
      var result = await this.getDataDetailDisbursalReport(param);
      if (result.success) {
        this.dataDetailDisbursalReport = result.data;
      } else {
        this.dataDetailDisbursalReport = [];
        this.showWarningMsg(result.message);
        this.loading = false;
      }
      this.loading = false;
    },
    async exportDataBankStatement() {
      if (this.dataDetailDisbursalReport.length < 1) {
        this.showWarningMsg('Have no data to export');
        return;
      }
      this.loading = true;
      var param = {
        appId: this.appId,
        typeProduct: this.lsProducts.selected,
        fromDt: this.fromDate,
        toDt: this.toDate
      };
      const result = await this.exportExcelFile(param);
      if (result.data != null) {
        const helloWorldExcelContent = result.data;
        const anchor_href =
          'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,' +
          helloWorldExcelContent;
        const exportLinkElement = document.createElement('a');

        exportLinkElement.hidden = true;
        exportLinkElement.download = 'Detail_Disbursal_Report.xlsx';
        exportLinkElement.href = anchor_href;
        exportLinkElement.text = 'downloading...';

        document.body.appendChild(exportLinkElement);
        exportLinkElement.click();
        exportLinkElement.remove();
        this.loading = false;
      } else {
        this.loading = false;
        this.showWarningMsg(result.message);
      }
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
