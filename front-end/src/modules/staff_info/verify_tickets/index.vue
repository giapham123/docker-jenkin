<template>
  <v-container class="home-app-wrapper" grid-list-md fluid text-xs-center>
    <v-card style="width:100%">
      <v-card-title style="color:#02786B" class="display-1"
      >Verify Ticket</v-card-title
      >
      <v-container grid-list-md fluid>
        <v-layout row wrap class="block" justify-center>
          <v-flex md12>
            <v-card>
              <v-card-text>
                <v-layout row wrap>
                  <!-- <v-flex md3>
                    <v-select
                      ref="departmentID"
                      :items="department"
                      item-text="name"
                      item-value="departmentID" 
                      label="Department"
                      autofocus
                      @change="selectDep"
                  /></v-flex> -->
                  <v-flex md2>
                    <v-text-field
                      ref="requestId"
                      v-model.trim="AccountInfo.requestId"
                      label="Request ID"
                      autofocus
                    />
                  </v-flex>
                  <v-flex md3>
                    <v-text-field
                      ref="accountId"
                      v-model.trim="AccountInfo.accountId"
                      label="Account ID"
                    />
                  </v-flex>
                  <v-flex xs3>
                    <v-autocomplete
                      v-model="selectRequestType.selected"
                      :items="selectRequestType.items"
                      label="Request Type"
                    >
                    </v-autocomplete>
                  </v-flex>
                  <v-flex sx12 md3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="searchAccountVerifyTicket"
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
                  <td class="text-xs-left">{{ props.item.requestId }}</td>
                  <td class="text-xs-left">{{ props.item.accountId }}</td>
                  <td class="text-xs-left">{{ props.item.requestTypeName }}</td>
                  <td class="text-xs-left">{{ props.item.verifyStatus }}</td>
                  <td class="text-xs-left">{{ props.item.requestNote }}</td>
                  <td class="text-xs-left">
                    {{ props.item.requestTimeString }}
                  </td>
                  <td class="text-xs-left">{{ props.item.requestBy }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
      </v-container>
    </v-card>
    <popup-reset-pass
      :show="showAddForm.resetPass"
      :params="{
        data: dataRegister
      }"
      @shown="handleClosePopupResetPass($event)"
    />

    <popup-detail-info
      :show="showAddForm.verifyAccountRegister"
      :params="{ data: dataRegister }"
      @shown="handleClosePopupRegister($event)"
    />

    <popup-active-inactive
      :show="showAddForm.activeInactive"
      :params="{
        data: dataRegister
      }"
      @shown="handleClosePopupActiveInactive($event)"
    />

    <popup-update-info
      :show="showAddForm.updateInfo"
      :params="{ data: dataRegister }"
      @shown="handleClosePopupUpdateInfo($event)"
    />

    <popup-update-permission
      :show="showAddForm.updatePermission"
      :params="{ data: dataRegister }"
      @shown="handleClosePopupUpdatePermission($event)"
    />

    <popup-remove-account
      :show="showAddForm.removeAccount"
      :params="{
        data: dataRegister
      }"
      @shown="handleClosePopupRemoveAccount($event)"
    />
  </v-container>
</template>

<script>
import { mapActions } from 'vuex';
import UpdateAccount from './popup_UpdateAccount';
import PopupDetailInfo from './popup-detail';
import PopupResetPass from './popup-reset-pass.vue';
import PopupActiveInactive from './popup-active-inactive';
import PopupUpdateInfo from './popup-update-info';
import PopupUpdatePermission from './popup-update-permission.vue';
import PopupRemoveAccount from './popup-remove';

export default {
  components: {
    UpdateAccount,
    PopupDetailInfo,
    PopupResetPass,
    PopupActiveInactive,
    PopupUpdateInfo,
    PopupUpdatePermission,
    PopupRemoveAccount
  },

  data() {
    return {
      selectRequestType: {
        items: [],
        selected: null
      },
      selected: [],
      listAccountResetPass: '',
      showAddForm: {
        updateInfo: false,
        resetPass: false,
        activeInactive: false,
        verifyAccountRegister: false,
        updatePermission: false,
        removeAccount: false
      },
      dataRegister: null,
      AccountInfo: {
        id: '',
        accountId: '',
        requestId: '',
        departmentId: '',
        groupUserId: '',
        acctDepartmentName: '',
        acctDepartmentId: ''
      },
      dataUpdateAccount: null,
      department: [],
      headers: [
        // { text: '', sortable: false, align: 'center' },
        { text: 'Request ID', sortable: false, value: 'requestId' },
        { text: 'Account ID', sortable: false, value: 'accountId' },
        { text: 'Request Type', sortable: false, value: 'requestTypeName' },
        { text: 'Verify Status', sortable: false, value: 'verifyStatus' },
        { text: 'Request Note', sortable: false, value: 'requestNote' },
        { text: 'Request Time', sortable: false, value: 'requestTimeString' },
        { text: 'Request By', sortable: false, value: 'requestBy' }
        // { text: 'Edit', sortable: false }
      ],
      listAccountInfoSearch: [],
      rowsPerPageItems: [10, 50, 100]
    };
  },

  created() {
    this.getDepartment();
    this.getRequestType();
  },

  methods: {
    ...mapActions('verifyTickets', [
      'getAllDepartment',
      'getAccountInfoInRequestSummary',
      'insUserAccount',
      'delUserAccount',
      'resetPass',
      'createRequestResetPassword',
      'createRequestActiveInactive',
      'getAllRequestType',
      'getGroupFeatureCurrentPermission'
    ]),

    selectDep(departmentID) {
      this.AccountInfo.departmentId = departmentID;
    },

    async getDepartment() {
      const result = await this.getAllDepartment();
      if (result.data != null) {
        this.department = result.data == null ? '' : result.data;
      }
      this.department.unshift({
        departmentID: null,
        name: 'ALL'
      });
    },

    async getRequestType() {
      let result = await this.getAllRequestType();
      this.selectRequestType.items = [];
      this.selectRequestType.selected = null;
      if (result.success) {
        result.data.forEach(element => {
          this.selectRequestType.items.push({
            value: element.requestId,
            text: element.requestName
          });
        });
      }
      // this.selectRequestType.items.unshift({
      //   value: null,
      //   text: 'ALL'
      // });
    },

    async searchAccountVerifyTicket() {
      const result = await this.getAccountInfoInRequestSummary({
        requestType: this.selectRequestType.selected,
        accountId: this.AccountInfo.accountId,
        requestId: this.AccountInfo.requestId
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
      if (params.requestTypeId == 'NEWAC') {
        this.showAddForm.verifyAccountRegister = true;
      } else if (params.requestTypeId == 'RESET') {
        this.showAddForm.resetPass = true;
      } else if (
        params.requestTypeId == 'ACT' ||
        params.requestTypeId == 'IACT'
      ) {
        this.showAddForm.activeInactive = true;
      } else if (params.requestTypeId == 'UPDIF') {
        this.showAddForm.updateInfo = true;
      } else if (params.requestTypeId == 'UPDPE') {
        this.showAddForm.updatePermission = true;
      } else if (params.requestTypeId == 'REM') {
        this.showAddForm.removeAccount = true;
      }
      this.dataRegister = params;
    },

    showPopupUpdateAccount(item) {
      this.showAddForm.updateAccount = true;
      this.dataUpdateAccount = item;
    },

    handleClosePopupRegister(event) {
      this.showAddForm.verifyAccountRegister = event;
      this.searchAccountVerifyTicket();
    },

    handleClosePopupResetPass(event) {
      this.showAddForm.resetPass = event;
      this.searchAccountVerifyTicket();
    },

    handleClosePopupActiveInactive(event) {
      this.showAddForm.activeInactive = event;
      this.searchAccountVerifyTicket();
    },

    handleClosePopupUpdateInfo(event) {
      this.showAddForm.updateInfo = event;
      this.searchAccountVerifyTicket();
    },

    handleClosePopupUpdatePermission(event) {
      this.showAddForm.updatePermission = event;
      this.searchAccountVerifyTicket();
    },

    handleClosePopupRemoveAccount(event) {
      this.showAddForm.removeAccount = event;
      this.searchAccountVerifyTicket();
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
