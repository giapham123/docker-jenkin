<template>
  <v-container class="home-app-wrapper" grid-list-md fluid text-xs-center>
    <v-card style="width:100%">
      <v-card-title style="color:#02786B" class="display-1"
      >Account Management</v-card-title
      >
      <v-container grid-list-md fluid>
        <v-layout row wrap class="block" justify-center>
          <v-flex md12>
            <v-card>
              <v-card-text>
                <v-layout row wrap>
                  <v-flex md12>
                    <v-text-field
                      ref="accountId"
                      v-model="AccountInfo.accountId"
                      label="User Name"
                      autofocus
                    />
                  </v-flex>
                </v-layout>
                <v-layout row wrap>
                  <v-flex md12>
                    <v-select
                      ref="departmentID"
                      :items="department"
                      item-text="name"
                      item-value="departmentID"
                      box
                      label="Department"
                      autofocus
                      @change="selectDep"
                  /></v-flex>
                </v-layout>
                <v-layout row wrap justify-center>
                  <v-flex sx12 md3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="getUserAcctSearch"
                    >Search</v-btn
                    >
                  </v-flex>
                  <!-- <v-flex sx12 md3>
                    <v-btn
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="resgisterUserAcct"
                    >Register</v-btn
                    >
                  </v-flex> -->
                  <v-flex sx12 md3>
                    <v-btn
                      :disabled="showAddForm.acctPermissFeatureButton"
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="handleBtnAcctPermissFeatureButton"
                    >Grant Permission Feature</v-btn
                    >
                  </v-flex>
                  <!-- <v-flex sx12 md3>
                    <v-btn
                      :disabled="showAddForm.acctPermissFeatureButton"
                      color="success"
                      class="vbutton"
                      style="margin-top: 20px;"
                      @click="resetPassword"
                    >Reset Password</v-btn
                    >
                  </v-flex> -->
                </v-layout>
                <v-layout row wrap />
              </v-card-text>
            </v-card>
          </v-flex>
        </v-layout>
        <v-layout>
          <v-flex md12>
            <v-data-table
              :headers="headers"
              :items="listAccountInfoSearch"
              class="elevation-1"
            >
              <template slot="items" slot-scope="props">
                <tr>
                  <td class="text-xs-left">{{ props.item.id }}</td>
                  <td class="text-xs-left">{{ props.item.accountId }}</td>
                  <td class="text-xs-left">{{ props.item.departmentId }}</td>
                  <td class="text-xs-left">{{ props.item.departmentName }}</td>
                  <!-- <td class="text-xs-center">
                    <v-btn
                      icon
                      class="vbutton_icon"
                      @click="deleteRegisterUserAcct(props.item)"
                    >
                      <v-icon>delete_forever</v-icon>
                    </v-btn>
                  </td> -->
                </tr>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
      </v-container>
    </v-card>
    <acct-permiss-feature-button
      :show="showAddForm.acctPermissFeatureButton"
      :params="{ data: AccountInfo.accountId }"
      @shown="showAddForm.acctPermissFeatureButton = $event"
    />
  </v-container>
</template>

<script>
import AcctPermissFeatureButton from './popup_AcctPermissFeatureButon';
import { mapActions } from 'vuex';
export default {
  components: {
    AcctPermissFeatureButton
  },

  data() {
    return {
      showAddForm: {
        acctPermissFeatureButton: false
      },
      AccountInfo: {
        id: '',
        accountId: '',
        departmentId: '',
        groupUserId: ''
      },
      AccountInfoLoad: {
        id: '',
        accountId: '',
        departmentId: '',
        groupUserId: ''
      },
      department: [],
      headers: [
        { text: 'No', sortable: true, value: 'id' },
        { text: 'User', sortable: false, value: 'accountId' },
        { text: 'Department ID', sortable: false, value: 'departmentId' },
        { text: 'Department', sortable: false, value: 'departmentName' }
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

  created() {
    this.getDepartment();
  },

  methods: {
    ...mapActions('accountmanagement', [
      'getAllDepartment',
      'getAccountInfoSearch',
      'insUserAccount',
      'delUserAccount',
      'resetPass'
    ]),

    handleBtnAcctPermissFeatureButton() {
      this.showAddForm.acctPermissFeatureButton = true;
    },

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
      const result = await this.getAccountInfoSearch(this.AccountInfo);
      if (result.data != null && result.data != '') {
        this.listAccountInfoSearch = result.data;
      } else {
        this.listAccountInfoSearch = [];
        this.$message.warning('No data found.');
      }
    },

    async resgisterUserAcct() {
      if (
        this.AccountInfo.accountId == null ||
        this.AccountInfo.accountId == ''
      ) {
        this.$refs.accountId.focus();
        this.$message.warning('Please input User Name');
      } else if (
        this.AccountInfo.departmentId == null ||
        this.AccountInfo.departmentId == ''
      ) {
        this.$refs.accountId.focus();
        this.$message.warning('Please choose Department');
      } else {
        const result = await this.insUserAccount(this.AccountInfo);
        if (result.success == true) {
          if (result.data != null && result.data != '') {
            this.$message.error('User Account has already existed.');
          } else {
            this.$message.success('Register successfull.');
          }
        } else {
          this.$message.error('Error. Please try again.');
        }
        this.loadUserAcctSearch(
          this.AccountInfo.accountId,
          this.AccountInfo.departmentId
        );
      }
    },

    async deleteRegisterUserAcct(chooseItem) {
      this.AccountInfo.id = chooseItem.id;
      const result = await this.delUserAccount(this.AccountInfo);
      if (result.success == true) {
        this.$message.success('Delete User Account successfull.');
        this.loadUserAcctSearch(this.AccountInfo.accountId);
      } else {
        this.$message.error('Error. Please try again.');
      }
      this.btnDisabled = true;
    },

    async resetPassword() {
      if (
        this.AccountInfo.accountId == null ||
        this.AccountInfo.accountId == ''
      ) {
        this.$refs.accountId.focus();
      } else {
        const result = await this.resetPass(this.AccountInfo);
        if (result.success == true) {
          this.$message.success('Reset password successfull.');
        } else {
          this.$message.error('Error. Please try again.');
        }
      }
    },

    async loadUserAcctSearch(acctID, depID) {
      this.AccountInfoLoad.accountId = acctID;
      this.AccountInfoLoad.departmentId = depID;
      const result = await this.getAccountInfoSearch(this.AccountInfoLoad);
      if (result.data != null && result.data != '') {
        this.listAccountInfoSearch = result.data;
      } else {
        this.listAccountInfoSearch = [];
      }
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
