<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>Termination simulation report</span>
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
          <!-- <v-flex xs2 style="padding-top: 20px; padding-left: 25px">
            <v-card-title  style="margin-left:40px"><span>Net Receivable:</span></v-card-title>
          </v-flex> -->
          <v-flex xs2 style="margin-left: 80px">
            <v-text-field
              v-model="from"
              :hint="`${suggestPriceFrom.toString()}`"
              label="Net receivable from"
              type="number"
              persistent-hint
            ></v-text-field>
            <div
              v-show="showMessageFrom"
              style="font-size: 12px !important; color: red"
            >
              {{ messageFromTo }}
            </div>
          </v-flex>
          <v-flex xs2>
            <v-text-field
              v-model="to"
              :hint="`${suggestPriceTo.toString()}`"
              label="Net receivable to"
              type="number"
              persistent-hint
            ></v-text-field>
          </v-flex>
        </v-layout>
        <v-layout wrap>
          <!-- <v-layout align-center>
            <v-checkbox
              v-model="cbLastDuedate"
              hide-details
              class="shrink mr-2"
              style="margin-top: -10px"
            ></v-checkbox>
            <date-picker
              :disabledDate="disabledDate"
              v-model="lastDuedt"
              label="Last Duedate <="
              icon="event"
            />
          </v-layout> -->
          <v-layout align-center>
            <v-checkbox
              v-model="cbLastDuedateFrom"
              hide-details
              class="shrink mr-2"
              style="margin-top: -10px"
            ></v-checkbox>
            <date-picker
              v-model="fromDate"
              :smaller="toDateSmaller"
              :disabled-date="disabledDateFrom"
              label="Last Duedate >="
              icon="event"
            />
            <v-checkbox
              v-model="cbLastDuedateTo"
              hide-details
              class="shrink mr-2"
              style="margin-top: -10px"
            ></v-checkbox>
            <date-picker
              v-model="toDate"
              :greater="fromDateGreater"
              :disabled-date="disabledDateTo"
              label="Last Duedate <="
              icon="event"
            />
          </v-layout>
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
              :items="dataAccounting"
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
      disabledDateTo: true,
      cbLastDuedateTo: false,
      cbLastDuedate: false,
      cbLastDuedateFrom: false,
      disabledDateFrom: true,
      toDateSmaller: new Date(
        new Date().setFullYear(new Date().getFullYear() + 1000)
      ).toLocaleDateString('en-US'),
      fromDateGreater: new Date(
        new Date().setFullYear(new Date().getFullYear() - 1000)
      ).toLocaleDateString('en-US'),
      fromDate: new Date().toLocaleDateString('en-US'),
      toDate: new Date().toLocaleDateString('en-US'),
      showPagin: false,
      search: '',
      pagination: {},
      errorFromTo: true,
      disabledDate: true,
      isUsed: false,
      loading: false,
      agreementId: null,
      from: null,
      to: null,
      messageFromTo: null,
      lastDuedt: new Date().toLocaleDateString('en-US'),
      dataAccounting: [],
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
        { text: 'Last Duedate', value: 'lstDuedate', sortable: false }
      ],
      errorFromToLess: true,
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
    labelName() {
      if (this.isUsed) {
        this.disabledDate = false;
        return 'Used';
      }
      this.disabledDate = true;
      return 'Not used';
    },
    showMessageFrom() {
      this.from = this.from == '' ? (this.from = null) : this.from;
      this.to = this.to == '' ? (this.to = null) : this.to;
      if (this.from != null && this.to == null) {
        this.errorFromTo = false;
      } else if (this.from == null && this.to != null) {
        this.errorFromTo = false;
      } else if (this.from == null && this.to == null) {
        this.errorFromTo = true;
      } else if (this.from != null && this.to != null) {
        this.errorFromTo = true;
      }

      if (this.to != null && this.to != '') {
        if (parseInt(this.from) > parseInt(this.to)) {
          this.messageFromTo = 'From must the less than To';
          this.errorFromToLess = false;
          return true;
        }
      }
      this.errorFromToLess = true;
      return false;
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
    cbLastDuedateTo() {
      if (this.cbLastDuedateTo && this.cbLastDuedateFrom) {
        this.fromDateGreater = new Date().toLocaleDateString('en-US');
        this.toDateSmaller = new Date().toLocaleDateString('en-US');
      } else {
        this.fromDateGreater = new Date(
          new Date().setFullYear(new Date().getFullYear() - 1000)
        ).toLocaleDateString('en-US');
        this.toDateSmaller = new Date(
          new Date().setFullYear(new Date().getFullYear() + 1000)
        ).toLocaleDateString('en-US');
      }

      if (this.cbLastDuedateTo) {
        this.disabledDateTo = false;
      }
      if (!this.cbLastDuedateTo) {
        this.disabledDateTo = true;
      }
    },
    cbLastDuedateFrom() {
      if (this.cbLastDuedateTo && this.cbLastDuedateFrom) {
        this.fromDateGreater = new Date().toLocaleDateString('en-US');
        this.toDateSmaller = new Date().toLocaleDateString('en-US');
      } else {
        this.fromDateGreater = new Date(
          new Date().setFullYear(new Date().getFullYear() - 1000)
        ).toLocaleDateString('en-US');
        this.toDateSmaller = new Date(
          new Date().setFullYear(new Date().getFullYear() + 1000)
        ).toLocaleDateString('en-US');
      }
      if (this.cbLastDuedateFrom) {
        this.disabledDateFrom = false;
      }
      if (!this.cbLastDuedateFrom) {
        this.disabledDateFrom = true;
      }
    }
  },
  created() {
    this.resetFeild();
  },

  methods: {
    ...mapActions('accounting', ['getDataAccounting', 'exportExcelFile']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async nextPages() {
      this.loading = true;

      var param = {
        to: this.to,
        from: this.from,
        agreementId: this.agreementId,
        page: this.page
      };
      param.fromDate = !this.disabledDateFrom ? this.fromDate : null;
      param.toDate = !this.disabledDateTo ? this.toDate : null;
      const result = await this.getDataAccounting(param);
      if (result.data != null) {
        this.dataAccounting = result.data.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async getData() {
      this.loading = true;
      this.page = 1;
      if (!this.errorFromToLess) {
        this.loading = false;
        return;
      }
      if (!this.errorFromTo) {
        this.showWarningMsg('Please full fill for From and To!');
        this.loading = false;
        return;
      }
      var param = {
        to: this.to,
        from: this.from,
        agreementId: this.agreementId,
        page: 1
      };

      param.fromDate = !this.disabledDateFrom ? this.fromDate : null;
      param.toDate = !this.disabledDateTo ? this.toDate : null;
      const result = await this.getDataAccounting(param);
      if (result.data != null) {
        this.dataAccounting = result.data.data;
        this.totalData = result.data.totalPages;
        this.showPagin = true;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    resetFeild() {
      this.agreementId = null;
      this.from = null;
      this.to = null;
    },
    async exportReport() {
      if (this.dataAccounting.length == 0) {
        this.showWarningMsg('Please search data for export!');
        return;
      }
      this.loading = true;
      var param = {
        to: this.to,
        from: this.from,
        agreementId: this.agreementId
      };

      param.fromDate = !this.disabledDateFrom ? this.fromDate : null;
      param.toDate = !this.disabledDateTo ? this.toDate : null;

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
    },
    useDateOnly() {
      this.disabledDateFromTo = true;
      this.disabledDate = false;
    },
    dontUseBoth() {
      this.disabledDateFromTo = true;
      this.disabledDate = true;
    },
    useFromTo() {
      this.disabledDateFromTo = false;
      this.disabledDate = true;
    }
  }
};
</script>
