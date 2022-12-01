<template>
  <v-container class="home-app-wrapper" grid-list-md fluid text-xs-center>
    <v-card style="width:100%">
      <v-card-title style="color:#02786B" class="display-1"
      >Account Info Review</v-card-title
      >
      <v-container grid-list-md fluid>
        <v-layout row wrap class="block" justify-center>
          <v-flex md12>
            <v-card>
              <v-card-text>
                <v-layout row wrap>
                  <v-flex xs3>
                    <v-autocomplete
                      v-model="selectDepartment.selected"
                      :items="selectDepartment.items"
                      label="Department"
                    >
                    </v-autocomplete>
                  </v-flex>
                  <!-- <v-flex md2>
                    <v-text-field
                      ref="requestId"
                      v-model.trim="AccountInfo.requestId"
                      label="Request ID"
                      autofocus
                    />
                  </v-flex> -->
                  <v-flex md3>
                    <v-text-field
                      ref="accountId"
                      v-model.trim="AccountInfo.accountId"
                      label="Account ID"
                    />
                  </v-flex>

                  <v-flex sx12 md3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="searchAccountInfo"
                    >Search</v-btn
                    >
                  </v-flex>
                </v-layout>
                <v-layout row wrap />
              </v-card-text>
            </v-card>
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex md12>
            <v-data-table
              v-model="selected"
              :headers="headers"
              :items="listAccountInfoSearch"
              :rows-per-page-items="rowsPerPageItems"
              class="elevation-1"
            >
              <template slot="items" slot-scope="props">
                <tr @dblclick="openPoupDetail(props.item)">
                  <td class="text-xs-left">{{ props.item.no }}</td>
                  <td class="text-xs-left">{{ props.item.accountId }}</td>
                  <td class="text-xs-left">{{ props.item.accountName }}</td>
                  <td class="text-xs-left">{{ props.item.mafcCode }}</td>
                  <td class="text-xs-left">{{ props.item.departmentName }}</td>
                  <td class="text-xs-left">
                    {{ props.item.isStatus == '1' ? 'Yes' : 'No' }}
                  </td>
                  <td class="text-xs-left">
                    {{ props.item.isDeleted == '1' ? 'Yes' : 'No' }}
                  </td>
                  <td class="text-xs-left">
                    {{ props.item.isChangePass == '1' ? 'Yes' : 'No' }}
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
      </v-container>
    </v-card>

    <popup-detail-info
      :show="showAddForm.accountInfoDetail"
      :params="{ data: dataRegister }"
      @shown="handleClosePopupRegister($event)"
    />
  </v-container>
</template>

<script>
import { mapActions } from 'vuex';
import PopupDetailInfo from './popup-detail';
import DatePicker from 'modules/common/datePicker';

export default {
  components: {
    PopupDetailInfo,
    DatePicker
  },

  data() {
    return {
      fromdate: null,
      todate: null,
      booleanCheckDate: false,
      selectDepartment: {
        items: [],
        selected: null
      },
      selected: [],
      listAccountResetPass: '',
      showAddForm: {
        accountInfoDetail: false
      },
      dataRegister: null,
      AccountInfo: {
        accountId: '',
        departmentId: ''
      },
      dataUpdateAccount: null,
      department: [],
      headers: [
        // { text: '', sortable: false, align: 'center' },
        { text: '#', sortable: false, value: '' },
        { text: 'Account ID', sortable: false, value: 'accountId' },
        { text: 'Full Name', sortable: false, value: 'accountName' },
        { text: 'MAFC Code', sortable: false, value: 'mafcCode' },
        { text: 'Department Name', sortable: false, value: 'departmentName' },
        { text: 'Active', sortable: false, value: 'isStatus' },
        { text: 'Remove', sortable: false, value: 'isDeleted' },
        { text: 'Is Change Pass', sortable: false, value: 'isChangePass' }

        // { text: 'Edit', sortable: false }
      ],
      listAccountInfoSearch: [],
      rowsPerPageItems: [10, 50, 100]
    };
  },

  watch: {
    listAccountInfoSearch: {
      handler() {
        for (var i = 0; i < this.listAccountInfoSearch.length; i++) {
          this.listAccountInfoSearch[i].no = i + 1;
        }
      },
      deep: true
    }
  },

  created() {
    this.getDepartment();
    // this.getRequestType();
  },

  methods: {
    ...mapActions('accountInfoReview', [
      'getAllDepartment',
      'getListAccountInfoByDepartment',
      'getAllDepartmentByUser'
    ]),

    selectDep(departmentID) {
      this.AccountInfo.departmentId = departmentID;
    },

    async getDepartment() {
      this.selectDepartment.items = [];
      this.selectDepartment.selected = null;
      const result = await this.getAllDepartmentByUser();
      if (result.data != null) {
        result.data.forEach(element => {
          this.selectDepartment.items.push({
            value: element.departmentID,
            text: element.name,
            table: element.table
          });
        });
        this.selectDepartment.items.unshift({
          value: null,
          text: 'ALL'
        });
      }
    },

    async searchAccountInfo() {
      var listDepartment = [];
      if (
        this.selectDepartment.selected == '' ||
        this.selectDepartment.selected == null
      ) {
        this.selectDepartment.items.forEach(element => {
          if (element.value != null) {
            listDepartment.push(element.value);
          }
        });
      } else listDepartment.push(this.selectDepartment.selected);
      console.log(listDepartment);
      let result = await this.getListAccountInfoByDepartment({
        accountId: this.AccountInfo.accountId,
        listDepartment: listDepartment
      });

      if (result.success) {
        if (result.data.length > 0) {
          this.listAccountInfoSearch = result.data;
        } else {
          this.listAccountInfoSearch = [];
        }
      } else {
        this.$message.warning(result.message);
      }
    },

    async openPoupDetail(params) {
      this.showAddForm.accountInfoDetail = true;
      this.dataRegister = params;
    },

    handleClosePopupRegister(event) {
      this.showAddForm.accountInfoDetail = event;
      this.searchAccountInfo();
    }
  }
};
</script>

<style lang="scss" scoped>
.vbutton {
  background-color: #009688 !important;
  color: #ffffff;
  width: 200px;
}
.vbutton_icon {
  background-color: #009688 !important;
}
</style>
