<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Termination simulation report-appid</span>
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
            <!-- <v-btn
              small
              color="rgb(0, 105, 92)"
              class="white--text"
              @click="exportReport"
              >Export</v-btn
            > -->
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="dataTerminationReportApp"
              :search="search"
              :pagination.sync="pagination"
              :rows-per-page-items="[10]"
              hide-actions
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td>{{ props.item.no }}</td>
                  <td>{{ props.item.product }}</td>
                  <td>{{ props.item.scheme }}</td>
                  <td>{{ props.item.agreementNo }}</td>
                  <td>{{ props.item.agreementId }}</td>
                  <td>
                    {{
                      String(props.item.principalOs).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>

                  <td>
                    {{
                      String(props.item.pendingInst).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>
                    {{
                      String(props.item.currentMonthint).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>
                    {{
                      String(props.item.currLpi).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>

                  <td>
                    {{
                      String(props.item.overDuecharges).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>
                    {{
                      String(props.item.forceClosureCharges).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>

                  <td>
                    {{
                      String(props.item.excessAmount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>

                  <td>
                    {{
                      String(props.item.netReceiable).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>{{ props.item.npaStage }}</td>
                  <td>{{ props.item.dpd }}</td>
                  <td>
                    {{
                      String(props.item.lastEmi).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>{{ props.item.lastDuedt }}</td>
                  <td>
                    {{
                      String(props.item.waiveOffAmount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>
                    {{
                      String(props.item.advancedInter).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>
                    {{
                      String(props.item.deductAmount).replace(
                        /(\d)(?=(?:\d{3})+(?:\.|$))/g,
                        '$1,'
                      )
                    }}
                  </td>
                  <td>
                    {{
                      String(props.item.netReceiAfterDeduct).replace(
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
      </v-container>
    </v-card>
  </div>
</template>
<script>
import DatePicker from 'modules/common/datePicker';
import { mapActions } from 'vuex';
import Loading from 'vue-loading-overlay';
import moment from 'moment';
export default {
  components: {
    DatePicker,
    Loading
  },

  data() {
    return {
      showPagin: false,
      search: '',
      pagination: {},
      loading: false,
      agreementId: null,
      messageFromTo: null,
      dataTerminationReportApp: [],
      headers: [
        { text: 'S.N', value: 'sn', sortable: false },
        { text: 'Product', value: 'product', sortable: false },
        { text: 'Scheme', value: 'scheme', sortable: false },
        { text: 'Agreement No', value: 'agreementNo', sortable: false },
        { text: 'Agreement ID', value: 'agreementId', sortable: false },
        { text: 'Principal O/S', value: 'principalOs', sortable: false },
        { text: 'Pending Installemnts', value: 'pendingIns', sortable: false },
        { text: 'Currentmonthint', value: 'currentMonthint', sortable: false },
        { text: 'CurrLPI', value: 'currLpi', sortable: false },
        { text: 'Over Due Charges', value: 'overDueCharge', sortable: false },
        { text: 'Force-closure Charge', value: 'forceCharge', sortable: false },
        { text: 'Excessamount', value: 'excessamount', sortable: false },
        { text: 'Net Receiable', value: 'netReceiable', sortable: false },
        { text: 'Npa Stage', value: 'npaStage', sortable: false },
        { text: 'Dpd', value: 'dpd', sortable: false },
        { text: 'Last EMI', value: 'lastEmi', sortable: false },
        { text: 'Last Duedate', value: 'lstDuedate', sortable: false },
        { text: 'Waive Off Amount', value: 'npaStage', sortable: false },
        { text: 'Advanced interest', value: 'dpd', sortable: false },
        { text: 'Deduct amount', value: 'lastEmi', sortable: false },
        {
          text: 'Net Receivable after deduct',
          value: 'lstDuedate',
          sortable: false
        }
      ],
      totalData: 0,
      page: 1
    };
  },
  computed: {
    suggestPriceFrom() {
      if (this.from != null) {
        return String(this.from).replace(/(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
      }
      return '';
    },
    suggestPriceTo() {
      if (this.to != null) {
        return String(this.to).replace(/(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,');
      }
      return '';
    },
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
    agreementId() {
      if (this.agreementId.length != 0) {
        this.disableDt = true;
      } else {
        this.disableDt = false;
      }
    }
  },
  created() {
    this.resetFeild();
  },

  methods: {
    ...mapActions('terminationReportApp', [
      'getTerminationReportApp',
      'exportExcelFile'
    ]),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async nextPages() {},
    async getData() {
      if (this.agreementId == null) {
        this.showErrorMsg('Please Input AgreementId');
        return;
      }
      if (this.agreementId.trim() == '') {
        this.showErrorMsg('Please Input AgreementId');
        return;
      }
      this.loading = true;
      this.page = 1;
      var param = {
        agreementId: this.agreementId,
        page: 1
      };
      const result = await this.getTerminationReportApp(param);
      if (result.data != null) {
        this.dataTerminationReportApp = result.data.data;
        this.totalData = result.data.totalPages;
        this.showPagin = false;
      } else {
        this.showWarningMsg(result.message);
        this.loading = false;
        this.dataTerminationReportApp = [];
        this.totalData = 0;
        this.showPagin = false;
      }
      this.loading = false;
    },
    resetFeild() {
      this.agreementId = null;
    },
    async exportReport() {
      if (this.dataTerminationReportApp.length == 0) {
        this.showWarningMsg('Please search data for export!');
        return;
      }
      this.loading = true;
      var param = {
        agreementId: this.agreementId
      };
      const result = await this.exportExcelFile(param);
      if (result.data != null) {
        var blob;
        blob = window.atob(result.data);
        let today = new Date().toISOString().slice(0, 10);
        var link = document.createElement('a');
        if (link.download !== undefined) {
          link.href = 'data:text/csv;charset=utf-8,' + encodeURI(blob);
          link.target = '_blank';
          link.download =
            'Termination SIM Report ' + moment(today).format('DD/MM/YYYY');
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
