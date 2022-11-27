<template>
  <div>
    <loading :active.sync="loading" :can-cancel="false" color="#009688" />
    <v-card class="home-app-wrapper">
      <v-card-title style="color: #02786b" class="display-1">
        <span>WriteOff EXT</span>
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
              :items="dataWriteOff"
              :search="search"
              :pagination.sync="pagination"
              :rows-per-page-items="[10]"
              hide-actions
              class="elevation-1 full_box_table"
            >
              <template v-slot:items="props">
                <tr>
                  <td>{{ props.item.no }}</td>
                  <td>{{ props.item.agreementNo }}</td>
                  <td>{{ props.item.agreementId }}</td>
                  <td>{{ props.item.outDate }}</td>
                  <td>
                    {{ String(props.item.amtFin).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.principleBf).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.interestBf).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.lppBf).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.principleAt).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.interestAt).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.lppAt).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td>
                  </td><td>
                    {{ String(props.item.totalPrinciple).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.totalInterest).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.totalLpp).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.paid).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.waiveoffAmt).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.totalExcess).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td></td>
                  <td>
                    {{ String(props.item.totalNetReceivable).replace(/(?<!\..*)(\d)(?=(?:\d{3})+(?:\.|$))/g, '$1,') }}</td>
                  </td>
                  </td><td>{{ props.item.insertDt }}</td>
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
      totalData: 0,
      page: 1,
      showPagin: false,
      search: '',
      pagination: {},
      loading: false,
      agreementId: null,
      dataWriteOff: [],
      headers: [
        { text: 'No', value: 'no', sortable: false },
        { text: 'Agreement No', value: 'agreementNo', sortable: false },
        { text: 'Agreement ID', value: 'agreementId', sortable: false },
        { text: 'Out_date', value: 'outDate', sortable: false },
        { text: 'AMTFIN', value: 'amtFin', sortable: false },
        { text: 'PRINCIPLE_BF', value: 'principleBf', sortable: false },
        { text: 'INTEREST_BF', value: 'interestBf', sortable: false },
        { text: 'LPP_BF', value: 'lppBf', sortable: false },
        { text: 'PRINCIPLE_AT', value: 'principleAt', sortable: false },
        { text: 'INTEREST_AT', value: 'interestAt', sortable: false },
        { text: 'LPP_AT', value: 'lppAt', sortable: false },
        { text: 'TOTAL_PRINCIPLE', value: 'totalPrinciple', sortable: false },
        { text: 'TOTAL_INTEREST', value: 'totalInterest', sortable: false },
        { text: 'TOTAL_LPP', value: 'totalLpp', sortable: false },
        { text: 'PAID', value: 'paid', sortable: false },
        { text: 'WAIVEOFF_AMT', value: 'waiveoffAmt', sortable: false },
        { text: 'TOTAL_EXCESS', value: 'totalExcess', sortable: false },
        {
          text: 'total_net_receivable',
          value: 'totalNetReceivable',
          sortable: false
        },
        { text: 'insert_date', value: 'insertDt', sortable: false }
      ]
    };
  },
  computed: {
    pages() {
      if (
        this.pagination.rowsPerPage == null ||
        this.pagination.totalItems == null
      )
        return 0;

      return Math.ceil(this.totalData / this.pagination.rowsPerPage);
    }
  },

  created() {
    this.resetFeild();
  },

  methods: {
    ...mapActions('writeoff', ['getDataWriteOff', 'exportDataWriteOff']),
    ...mapActions('global', [
      'showErrorMsg',
      'showSuccessMsg',
      'showWarningMsg'
    ]),
    async nextPages() {
      this.loading = true;
      var param = {
        agreementId: this.agreementId,
        page: this.page
      };

      const result = await this.getDataWriteOff(param);
      if (result.data != null) {
        this.dataWriteOff = result.data.data;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    async getData() {
      this.loading = true;
      var param = {
        agreementId: this.agreementId,
        page: 1
      };

      const result = await this.getDataWriteOff(param);
      if (result.data != null) {
        this.dataWriteOff = result.data.data;
        this.totalData = result.data.totalPages;
        this.showPagin = true;
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    },
    resetFeild() {
      this.agreementId = null;
    },
    async exportReport() {
      if (this.dataWriteOff.length == 0) {
        this.showWarningMsg('Please search data for export!');
        return;
      }
      this.loading = true;
      var param = {
        agreementId: this.agreementId
      };

      const result = await this.exportDataWriteOff(param);
      if (result.data != null) {
        var blob;
        blob = window.atob(result.data);
        let today = new Date().toISOString().slice(0, 10);
        var link = document.createElement('a');
        if (link.download !== undefined) {
          link.href = 'data:text/csv;charset=utf-8,' + encodeURI(blob);
          link.target = '_blank';
          link.download =
            'Termination_SO_' + moment(today).format('DD/MM/YYYY') + '.csv';
          link.click();
        }
      } else {
        this.showWarningMsg(result.message);
      }
      this.loading = false;
    }
  }
};
</script>
