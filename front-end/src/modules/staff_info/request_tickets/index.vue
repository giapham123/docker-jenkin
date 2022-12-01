<template>
  <v-container class="home-app-wrapper" grid-list-md fluid text-xs-center>
    <v-card style="width:100%">
      <v-card-title style="color:#02786B" class="display-1"
      >Request Ticket</v-card-title
      >
      <v-container grid-list-md fluid>
        <v-layout row wrap class="block" justify-center>
          <v-flex md12>
            <v-card>
              <v-card-text>
                <v-layout row wrap>
                  <v-flex md12>
                    <v-select
                      ref="departmentID"
                      :items="department"
                      item-text="name"
                      item-value="departmentID"
                      label="Department"
                      autofocus
                      @change="selectDep"
                  /></v-flex>
                </v-layout>
                <v-layout row wrap>
                  <v-flex md12>
                    <v-text-field
                      ref="accountId"
                      v-model.trim="AccountInfo.accountId"
                      label="Account ID"
                      autofocus
                    />
                  </v-flex>
                </v-layout>

                <v-layout row wrap justify-center>
                  <v-flex md2 mr-3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="getUserAcctSearch"
                    >Search</v-btn
                    >
                  </v-flex>
                  <v-flex md2 mr-3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="resgisterUserAcct"
                    >Register</v-btn
                    >
                  </v-flex>
                  <v-flex md2 mr-3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="handleBtnAcctiveInactive"
                    >Active/Inactive</v-btn
                    >
                  </v-flex>
                  <v-flex md2 mr-3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="handleBtnResetPassword"
                    >Reset Password</v-btn
                    >
                  </v-flex>
                  <v-flex md2 mr-3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="handleBtnRemovePassword"
                    >Remove</v-btn
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
              class="elevation-1"
            >
              <template slot="items" slot-scope="props">
                <tr>
                  <td>
                    <v-checkbox
                      v-model="props.selected"
                      primary
                      hide-details
                    ></v-checkbox>
                  </td>
                  <!-- <td class="text-xs-center">{{ props.item.id }}</td> -->
                  <td class="text-xs-left">{{ props.item.accountId }}</td>
                  <td class="text-xs-left">{{ props.item.fullName }}</td>
                  <!-- <td class="text-xs-left">{{ props.item.mafcCode }}</td> -->
                  <td class="text-xs-left">
                    {{ props.item.isStatus == 1 ? 'Yes' : 'No' }}
                  </td>
                  <td class="text-xs-left">{{ props.item.departmentName }}</td>
                  <td class="text-xs-left">
                    <!-- <v-btn
                      icon
                      class="vbutton_icon"
                      @click="deleteRegisterUserAcct(props.item)"
                    >
                      <v-icon>delete_forever</v-icon>
                    </v-btn> -->
                    <v-btn
                      icon
                      class="vbutton_icon"
                      @click="showPopupUpdateAccount(props.item)"
                    >
                      <v-icon>border_color</v-icon>
                    </v-btn>
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
      </v-container>
    </v-card>
    <register-account
      :show="showAddForm.regiterAccount"
      :params="{
        data: AccountInfo.accountId,
        dataDeparment: AccountInfo.acctDepartmentName,
        dataDeparmentId: AccountInfo.acctDepartmentId
      }"
      @shown="closePopupRegisterAccount($event)"
    />
    <update-account
      :show="showAddForm.updateAccount"
      :params="{
        data: dataUpdateAccount
      }"
      @shown="showAddForm.updateAccount = $event"
    />
    <v-dialog v-model="showAddForm.resetPass" max-width="450">
      <v-card>
        <v-card-title class="headline orange primary-title">
          Message Confirm
        </v-card-title>
        <v-card-text>
          <h5>
            Do you want to request Reset Password Accounts <br />
            <b> {{ listAccountResetPass }}? </b>
          </h5>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="teal vbutton" text @click="resetPassword()">
            OK
          </v-btn>
          <v-btn
            color="teal vbutton"
            text
            @click="showAddForm.resetPass = false"
          >
            Cancel
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showAddForm.removeAccount" max-width="450">
      <v-card>
        <v-card-title class="headline orange primary-title">
          Message Confirm
        </v-card-title>
        <v-card-text>
          <h5>
            Do you want to request Delete Accounts <br />
            <b> {{ listAccountRemove }}? </b>
          </h5>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="teal vbutton" text @click="removeAccount()">
            OK
          </v-btn>
          <v-btn
            color="teal vbutton"
            text
            @click="showAddForm.removeAccount = false"
          >
            Cancel
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import RegisterAccount from './popup_RegisterAccount';
import UpdateAccount from './popup_UpdateAccount';

import { mapActions } from 'vuex';
export default {
  components: {
    RegisterAccount,
    UpdateAccount
  },

  data() {
    return {
      selected: [],
      listAccountResetPass: '',
      listAccountRemove: '',
      showAddForm: {
        regiterAccount: false,
        updateAccount: false,
        resetPass: false,
        activeInactiveAcct: false,
        removeAccount: false
      },
      AccountInfo: {
        id: '',
        accountId: '',
        departmentId: '',
        groupUserId: '',
        acctDepartmentName: '',
        acctDepartmentId: ''
      },
      AccountInfoLoad: {
        id: '',
        accountId: '',
        departmentId: '',
        groupUserId: ''
      },
      dataUpdateAccount: null,
      department: [],
      headers: [
        { text: '', sortable: false, align: 'center' },
        // { text: '#', sortable: false, align: 'center' },
        // { text: 'No', sortable: true, value: 'id' },
        { text: 'Account ID', sortable: false, value: 'accountId' },
        { text: 'Full Name', sortable: false, value: 'fullName' },
        // { text: 'MAFC Code', sortable: false, value: 'mafcCode' },
        { text: 'Active', sortable: false, value: 'isStatus' },
        { text: 'Department', sortable: false, value: 'departmentName' },
        { text: 'Edit', sortable: false }
      ],
      listAccountInfoSearch: [],
      btnDisabled: true
      // accountIdRules: [v => !!v || 'User name field is required'],
      // departmentIdRules: [
      //   v =>
      //     !!v || 'Department field is required for registering new user account'
      // ]
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
  },

  methods: {
    ...mapActions('requestTicket', [
      'getAllDepartment',
      'getAccountInfoSearchRequestTicket',
      'insUserAccount',
      'delUserAccount',
      'resetPass',
      'createRequestResetPassword',
      'createRequestActiveInactive',
      'createRequestRemoveAccount',
      'getAccountInfoRequestExist'
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

    async getUserAcctSearch() {
      if (
        (this.AccountInfo.accountId != null &&
          this.AccountInfo.accountId != '') ||
        (this.AccountInfo.departmentId != null &&
          this.AccountInfo.departmentId != '')
      ) {
        const result = await this.getAccountInfoSearchRequestTicket(
          this.AccountInfo
        );
        if (result.data != null && result.data != '') {
          this.listAccountInfoSearch = result.data;
        } else {
          this.listAccountInfoSearch = [];
          this.$message.warning('No data found.');
        }
      } else {
        this.$message.warning('Please choose department or Account ID');
      }
    },

    async resgisterUserAcct() {
      if (
        this.AccountInfo.departmentId == null ||
        this.AccountInfo.departmentId == ''
      ) {
        this.$refs.accountId.focus();
        this.$message.warning('Please choose Department');
      } else if (
        this.AccountInfo.accountId == null ||
        this.AccountInfo.accountId == ''
      ) {
        this.$refs.accountId.focus();
        this.$message.warning('Please input User Name');
      } else {
        //check account register exist in accountinfo and request detail
        let result = await this.getAccountInfoRequestExist(
          this.AccountInfo.accountId
        );
        if (result.success) {
          this.showAddForm.regiterAccount = true;
          this.department.forEach(element => {
            if (this.AccountInfo.departmentId == element.departmentID) {
              this.AccountInfo.acctDepartmentName = element.name;
              this.AccountInfo.acctDepartmentId = element.departmentID;
            }
          });
        } else {
          this.$message.warning(result.message);
          return;
        }
      }
    },

    closePopupRegisterAccount(event) {
      this.showAddForm.regiterAccount = event;
    },

    async handleBtnResetPassword() {
      this.listAccountResetPass = '';
      if (this.selected.length > 0) {
        //validate
        var validate = this.validateResetPassword();
        if (validate) {
          //handle show popup reset
          this.selected.forEach(element => {
            this.listAccountResetPass += element.accountId + ' ';
          });
          this.showAddForm.resetPass = true;
        }
      } else {
        this.$message.error('Please select Account to reset password');
      }
    },

    validateResetPassword() {
      var isValidate = true;
      this.selected.forEach(element => {
        if (element.isStatus != 1) {
          this.$message.error(
            'User ' + element.accountId + ' inactive. No allow reset password'
          );
          isValidate = false;
        }
      });
      return isValidate;
    },

    handleBtnRemovePassword() {
      this.listAccountRemove = '';
      if (this.selected.length > 0) {
        this.selected.forEach(element => {
          this.listAccountRemove += element.accountId + ' ';
        });
        this.showAddForm.removeAccount = true;
      } else {
        this.$message.error('Please select Account to remove');
      }
    },

    async removeAccount() {
      let result = await this.createRequestRemoveAccount(this.selected);
      if (result.success) {
        this.$message.success(result.message + '. RequestID = ' + result.data);
        this.showAddForm.removeAccount = false;
        this.selected = [];
      } else {
        this.$message.error(result.message);
      }
    },

    async resetPassword() {
      let result = await this.createRequestResetPassword(this.selected);
      if (result.success) {
        this.$message.success(result.message + '. RequestID = ' + result.data);
        this.showAddForm.resetPass = false;
        this.selected = [];
      } else {
        this.$message.error(result.message);
      }
    },

    validateAcctiveInactive() {
      var isValidate = true;
      if (this.selected.length > 0) {
        var typeActiveInactive = this.selected[0].isStatus;
        this.selected.forEach(element => {
          if (element.isStatus != typeActiveInactive) {
            this.$message.error('Please select All Active or All Inactive');
            isValidate = false;
          }
        });
      } else {
        this.$message.error('Please select Account to Active/Inactive');
        isValidate = false;
      }
      return isValidate;
    },

    async handleBtnAcctiveInactive() {
      let validate = await this.validateAcctiveInactive();
      if (validate) {
        let result = await this.createRequestActiveInactive(this.selected);
        if (result.success) {
          this.$message.success(
            result.message + '. RequestID = ' + result.data
          );
          this.showAddForm.resetPass = false;
          this.selected = [];
        } else {
          this.$message.error(result.message);
        }
      }
    },

    showPopupUpdateAccount(item) {
      this.showAddForm.updateAccount = true;
      this.dataUpdateAccount = item;
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
