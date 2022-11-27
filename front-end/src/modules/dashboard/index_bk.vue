<template>
  <v-container grid-list-xl fluid>
    <v-card style="width:100%">
      <v-card-title style="color:#02786B" class="display-1">
        <span>Dashboard</span>
      </v-card-title>
      <!-- Go to top -->
      <app-fab-fresh @save="refeshForm" />
      <v-container grid-list-md fluid>
        <!-- <v-layout>
          <v-flex md-5></v-flex>
          <v-flex>
            <v-tooltip top>
              <template v-slot:activator="{ on }">
                 <v-btn
                color="white"
                depressed
                @click="refeshForm"
                v-on="on"
              > 
                <i
                  class="material-icons pr-2"
                  style="cursor: pointer; font-size: 45px"
                  depressed
                  @click="refeshForm"
                  v-on="on"
                >refresh</i
                >
                </v-btn> 
              </template>
              <span>Refresh</span>
            </v-tooltip>
          </v-flex>
        </v-layout> -->
        <v-layout wrap text-xs-center class="mb-3">
          <v-flex>
            <v-card class="mt-4 mr-4">
              <v-sheet class="v-sheet--offset mx-auto" elevation="12">
                <!-- load sum system success and system fail -->
                <chart-success
                  v-if="isloaded"
                  :data-chart="dataChartSystem"
                ></chart-success>
              </v-sheet>
              <v-card-text class="pt-0">
                <div class="subheading font-weight-light grey--text">
                  Import Data FinnOne in 7 days
                </div>
              </v-card-text>
            </v-card>
          </v-flex>
          <v-flex>
            <v-card class="mt-4">
              <v-sheet class="v-sheet--offset mx-auto" elevation="12">
                <!-- load sum system success and system fail -->
                <chart-statistic-status
                  v-if="isloaded"
                  :data-chart="dataChartStatistic"
                ></chart-statistic-status>
              </v-sheet>
              <v-card-text class="pt-0">
                <div class="subheading font-weight-light grey--text">
                  User's Status Real Time
                </div>
              </v-card-text>
            </v-card>
          </v-flex>
        </v-layout>
        <v-layout wrap text-xs-center>
          <v-flex>
            <v-card class="v-card">
              <v-toolbar
                class="v-offset"
                style="top:-24px; margin-bottom:-24px; padding:5px;"
                color="#66bb6a"
              >
                <v-tabs
                  v-model="tabs"
                  fixed-tabs
                  dark
                  color="#66bb6a"
                  slider-color="white"
                >
                  <span
                    class="subheading font-weight-light mr-3"
                    style="align-self: center; color:#ffffff"
                  >Step:</span
                  >
                  <v-tab>IMPORT STATUS</v-tab>
                  <v-tab>SYSTEM</v-tab>
                  <v-tab>MAKER</v-tab>
                  <v-tab>CHECKER 1</v-tab>
                  <v-tab>CHECKER 2</v-tab>
                </v-tabs>
              </v-toolbar>
              <v-tabs-items v-model="tabs" class="v-card__text">
                <v-tab-item>
                  <v-card flat>
                    <v-card-title class="headline">IMPORT STATUS</v-card-title>
                    <v-card-text>
                      <div
                        style="position: relative"
                        class="no_box_shadow border_table pointer-table"
                      >
                        <v-data-table
                          :headers="headersImportStatus"
                          :items="table_import_data"
                          item-key="createdDate"
                          no-data-text="No data available"
                          class="elevation-1 full_box_table"
                        >
                          <template v-slot:headers="{ headers }">
                            <tr class="header_row">
                              <template v-for="header in headers">
                                <td :key="header.value" class="header_cell">
                                  {{ header.text }}
                                </td>
                              </template>
                            </tr>
                          </template>

                          <template v-slot:items="{ item, index }">
                            <tr
                              @dblclick="$set(expands, index, !expands[index])"
                            >
                              <td class="text-xs-center body_cell">
                                <v-icon
                                  v-if="item.dbtDashboardImportStatusChildList"
                                  @click="$set(expands, index, !expands[index])"
                                >
                                  {{
                                    expands[index]
                                      ? 'keyboard_arrow_down'
                                      : 'keyboard_arrow_right'
                                  }}
                                </v-icon>
                              </td>
                              <td class="text-xs-center body_cell">
                                {{ item.createdDate }}
                              </td>
                              <td class="full_text_table text-xs-center"></td>
                              <td class="full_text_table text-xs-center"></td>
                              <td
                                v-if="item.status == 1"
                                class="full_text_table text-xs-center"
                              >
                                Success
                              </td>
                              <td
                                v-if="item.status == 2"
                                class="full_text_table text-xs-center"
                              >
                                Failure
                              </td>
                              <td
                                v-if="item.status == 3"
                                class="full_text_table text-xs-center"
                              >
                                In Process
                              </td>
                              <td class="full_text_table text-xs-center"></td>
                              <td class="full_text_table text-xs-center">
                                {{ item.record }}
                              </td>
                              <td class="full_text_table text-xs-center"></td>
                              <td class="full_text_table text-xs-center"></td>
                            </tr>
                            <template
                              v-if="item.dbtDashboardImportStatusChildList"
                            >
                              <template
                                v-for="sub_item in item.dbtDashboardImportStatusChildList"
                              >
                                <tr v-show="expands[index]" :key="sub_item.no">
                                  <td
                                    class="text-xs-center full_text_table body_cell"
                                  ></td>
                                  <td class="full_text_table text-xs-center">
                                    {{ sub_item.createdDate }}
                                  </td>
                                  <td class="full_text_table text-xs-center">
                                    {{ sub_item.createdDateString }}
                                  </td>
                                  <td
                                    class="text-xs-left full_text_table body_cell"
                                  >
                                    {{ sub_item.fileName }}
                                  </td>
                                  <td
                                    v-if="sub_item.status == 0"
                                    class="full_text_table text-xs-center"
                                  >
                                    Success
                                  </td>
                                  <td
                                    v-if="
                                      sub_item.status == 2 ||
                                        sub_item.status == 3 ||
                                        sub_item.status == 6
                                    "
                                    class="full_text_table text-xs-center"
                                  >
                                    Failure
                                  </td>
                                  <td
                                    v-if="
                                      sub_item.status == 1 ||
                                        sub_item.status == 4 ||
                                        sub_item.status == 5
                                    "
                                    class="full_text_table text-xs-center"
                                  >
                                    In Process
                                  </td>
                                  <td class="full_text_table text-xs-center">
                                    {{ sub_item.message }}
                                  </td>
                                  <td class="full_text_table text-xs-center">
                                    {{ sub_item.record }}
                                  </td>
                                  <td class="full_text_table text-xs-center">
                                    {{ sub_item.validateFalse }}
                                  </td>
                                  <td class="full_text_table text-xs-center">
                                    {{ sub_item.validateSuccess }}
                                  </td>
                                </tr>
                              </template>
                            </template>
                          </template>
                        </v-data-table>
                      </div>
                    </v-card-text>
                  </v-card>
                </v-tab-item>

                <v-tab-item>
                  <v-card flat>
                    <v-card-title class="headline">System Auto</v-card-title>
                    <v-card-text>
                      <filter-table
                        :headers="headersSysAuto"
                        :data="table_sys_auto_data"
                        :setting_header="false"
                        primary="no"
                      >
                        <template slot="tbody" slot-scope="props">
                          <td class="full_text_table text-xs-center">
                            {{ props.item.no }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.statusDateTime }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.statusName }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.numberOfLines }}
                          </td>
                        </template>
                      </filter-table>
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-title class="headline">Maker</v-card-title>
                    <v-card-text>
                      <filter-table
                        :headers="headersMaker"
                        :data="table_maker_data"
                        :setting_header="false"
                        primary="no"
                      >
                        <template slot="tbody" slot-scope="props">
                          <td class="full_text_table text-xs-center">
                            {{ props.item.no }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.statusDateTime }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.statusName }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.numberOfLines }}
                          </td>
                        </template>
                      </filter-table>
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-title class="headline">Checker 1</v-card-title>
                    <v-card-text>
                      <filter-table
                        :headers="headersChecker1"
                        :data="table_checker1_data"
                        :setting_header="false"
                        primary="no"
                      >
                        <template slot="tbody" slot-scope="props">
                          <td class="full_text_table text-xs-center">
                            {{ props.item.no }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.statusDateTime }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.statusName }}
                          </td>
                          <td class="full_text_table text-xs-center">
                            {{ props.item.numberOfLines }}
                          </td>
                        </template>
                      </filter-table>
                    </v-card-text>
                  </v-card>
                </v-tab-item>
                <v-tab-item>
                  <v-card flat>
                    <v-card-title class="headline">Checker 2</v-card-title>
                    <v-card-text>
                      <div
                        style="position: relative"
                        class="no_box_shadow border_table"
                      >
                        <filter-table
                          :headers="headersChecker2"
                          :data="table_checker2_data"
                          :setting_header="false"
                          primary="no"
                        >
                          <template slot="tbody" slot-scope="props">
                            <tr>
                              <td class="full_text_table text-xs-center">
                                {{ props.item.no }}
                              </td>
                              <td class="full_text_table text-xs-center">
                                {{ props.item.statusDateTime }}
                              </td>
                              <td class="full_text_table text-xs-center">
                                {{ props.item.statusName }}
                              </td>
                              <td class="full_text_table text-xs-center">
                                {{ props.item.numberOfLines }}
                              </td>
                            </tr>
                          </template>
                        </filter-table>
                      </div>
                    </v-card-text>
                  </v-card>
                </v-tab-item>
              </v-tabs-items>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-card>
  </v-container>
</template>

<script>
import { TABLE_PAGE_SIZES } from 'core/constants';
import FilterTable from 'modules/common/filterTable';
import { mapActions, mapGetters } from 'vuex';
import AddCheckCustomer from './popup_CheckInfoCustomer';
import AddCheckScoreRangeCus from './popup_CheckScoreRangeCus';
import AddSearchFileScan from './popup_SearchFileScan';
import AddBlackList from './popup_Blacklist';
import Chart from 'chart.js';
import chartSuccess from './chart_success';
import chartStatisticStatus from './chart_statistic';
import AppFabFresh from './AppFabFresh';

export default {
  components: {
    FilterTable,
    AddCheckCustomer,
    AddCheckScoreRangeCus,
    AddSearchFileScan,
    AddBlackList,
    chartSuccess,
    chartStatisticStatus,
    AppFabFresh
  },

  data() {
    return {
      page_size: TABLE_PAGE_SIZES,
      headersMaker: this.createMakerHeaderGrid(),
      headersSysAuto: this.createMakerHeaderGrid(),
      headersChecker1: this.createMakerHeaderGrid(),
      headersChecker2: this.createMakerHeaderGrid(),
      headersImportStatus: this.createImportStatusHeaderGrid(),
      table_maker_data: [],
      table_sys_auto_data: [],
      table_checker1_data: [],
      table_checker2_data: [],
      expands: [],
      table_import_data: [],
      lanceData: '',
      sumCustomer: '',
      tabs: null,
      dataChartSystem: {
        dateDBT: [],
        dataSuccess: [],
        dataFail: [],
        dataInProcess: []
      },
      dataChartStatistic: {
        dateDBT: [],
        dataMK_NEW: [],
        dataCK1_NEW: [],
        dataCK2_NEW: []
      },
      isloaded: false
    };
  },

  watch: {
    table_maker_data: {
      handler() {
        for (var i = 0; i < this.table_maker_data.length; i++) {
          this.table_maker_data[i].no = i + 1;
        }
      },
      deep: true
    },
    table_sys_auto_data: {
      handler() {
        for (var i = 0; i < this.table_sys_auto_data.length; i++) {
          this.table_sys_auto_data[i].no = i + 1;
        }
      },
      deep: true
    },
    table_checker1_data: {
      handler() {
        for (var i = 0; i < this.table_checker1_data.length; i++) {
          this.table_checker1_data[i].no = i + 1;
        }
      },
      deep: true
    },
    table_checker2_data: {
      handler() {
        for (var i = 0; i < this.table_checker2_data.length; i++) {
          this.table_checker2_data[i].no = i + 1;
        }
      },
      deep: true
    },
    table_import_data: {
      handler() {
        for (var i = 0; i < this.table_import_data.length; i++) {
          this.table_import_data[i].no = i + 1;
        }
      },
      deep: true
    }
  },

  created() {
    this.loadChart();
    this.loadMakerData();
    this.getListImportStatus();
  },

  methods: {
    ...mapActions('dashboard', [
      'getSummarySystem',
      'getDashboardMaker',
      'getSummaryStatistic',
      'getImportStatus'
    ]),

    createMakerHeaderGrid() {
      return [
        { text: '#', sortable: false, align: 'center', value: 'no' },
        {
          text: 'Date',
          sortable: false,
          align: 'center',
          value: 'statusDateTime',
          type: 'date'
        },
        {
          text: 'Status',
          sortable: false,
          align: 'center',
          value: 'statusName',
          type: 'string'
        },
        {
          text: 'Total',
          sortable: false,
          align: 'center',
          value: 'numberOfLines',
          type: 'number'
        }
      ];
    },

    createImportStatusHeaderGrid() {
      return [
        { text: '#', sortable: false, align: 'center' },
        { text: 'Date', sortable: false, align: 'center' },
        { text: 'Time', sortable: false, align: 'center' },
        { text: 'File Name', sortable: false, align: 'center' },
        { text: 'Status', sortable: false, align: 'center' },
        { text: 'Message', sortable: false, align: 'center' },
        { text: 'Total', sortable: false, align: 'center' },
        { text: 'Validate False', sortable: false, align: 'center' },
        { text: 'Validate Success', sortable: false, align: 'center' }
      ];
    },

    refeshForm() {
      this.loadChart();
      this.loadMakerData();
      this.getListImportStatus();
    },

    async loadChart() {
      this.isloaded = false;
      this.dataChartSystem.dateDBT = [];
      this.dataChartSystem.dataSuccess = [];
      this.dataChartSystem.dataFail = [];
      this.dataChartSystem.dataInProcess = [];
      this.dataChartStatistic.dateDBT = [];
      this.dataChartStatistic.dataMK_NEW = [];
      this.dataChartStatistic.dataCK1_NEW = [];
      this.dataChartStatistic.dataCK2_NEW = [];

      let result = await this.getSummarySystem();
      if (result.success) {
        let dataSystem = result.data;
        dataSystem.data.forEach(item => {
          this.dataChartSystem.dateDBT.push(item.CREATED_DATE);
          this.dataChartSystem.dataSuccess.push(item.DBT_RECORD_SUCCESS);
          this.dataChartSystem.dataFail.push(item.DBT_RECORD_FAIL);
          this.dataChartSystem.dataInProcess.push(item.DBT_RECORD_INPROCESS);
        });
      }

      let result_statistic = await this.getSummaryStatistic();
      if (result_statistic.success) {
        let dataStatistic = result_statistic.data;
        dataStatistic.data.forEach(item => {
          this.dataChartStatistic.dateDBT.push(item.CREATED_DATE);
          this.dataChartStatistic.dataMK_NEW.push(item.COUNT_MK_NEW);
          this.dataChartStatistic.dataCK1_NEW.push(item.COUNT_CK1_NEW);
          this.dataChartStatistic.dataCK2_NEW.push(item.COUNT_CK2_NEW);
        });
      }
      this.isloaded = true;
    },

    async loadMakerData() {
      this.table_loading = true;
      this.lanceData = 'SYS_AUTO, MARKER, CHECKER1, CHECKER2';
      let result = await this.getDashboardMaker(this.lanceData);
      if (result.success) {
        if (result.data.MARKER.length > 0) {
          this.table_maker_data = result.data.MARKER;
        }
        if (result.data.SYS_AUTO.length > 0) {
          this.table_sys_auto_data = result.data.SYS_AUTO;
        }
        if (result.data.CHECKER1.length > 0) {
          this.table_checker1_data = result.data.CHECKER1;
        }
        if (result.data.CHECKER2.length > 0) {
          this.table_checker2_data = result.data.CHECKER2;
        }
      } else {
        this.$message.warning(result.message);
        this.table_maker_data = [];
        this.table_sys_auto_data = [];
        this.table_checker1_data = [];
        this.table_checker2_data = [];
        this.table_loading = false;
      }
      this.table_loading = false;
    },

    async getListImportStatus() {
      let result = await this.getImportStatus();
      if (result.success) {
        if (result.data != null && result.data.length > 0) {
          this.table_import_data = result.data;
        } else {
          this.table_import_data = [];
        }
      } else {
        this.$message.error(result.message);
        this.table_import_data = [];
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.vbutton {
  background-color: #009688 !important;
  color: #ffffff;
  width: 300px;
}

.v-offset {
  margin: 0 auto;
  max-width: calc(100% - 32px);
  position: relative;
}

.v-card {
  -webkit-box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.14);
  box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.14);
  border-radius: 3px;
}

.v-card__text {
  padding: 16px;
  width: 100%;
}

.v-card > :last-child:not(.v-btn):not(.v-chip) {
  border-bottom-left-radius: inherit;
  border-bottom-right-radius: inherit;
}

.v-sheet--offset {
  top: -24px;
  position: relative;
}
</style>
