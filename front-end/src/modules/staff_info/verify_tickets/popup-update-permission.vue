<template>
  <v-dialog
    v-model="dialog"
    :scrollable="true"
    width="60%"
    fullscreen
    hide-overlay
    transition="dialog-bottom-transition"
  >
    <v-form ref="formCd" class="overflow_hidden">
      <v-card
        class="home-app-wrapper popup popupAllocation auto-allocate"
        flat
        style="font-weight: bold !important;"
      >
        <v-layout align-center fill-height class="header white--text ui_title">
          <v-flex md6>
            <v-card-title class="subheading white--text title">
              Verify Update Permission
            </v-card-title>
          </v-flex>
          <v-flex md6>
            <v-layout row align-center justify-end fill-height>
              <!-- <v-btn flat class="white--text">
                <v-icon left>people</v-icon>
                {{ upper(me.account_id) }}
              </v-btn> -->
              <v-btn icon dark @click="handleClose">
                <v-icon>
                  close
                </v-icon>
              </v-btn>
            </v-layout>
          </v-flex>
        </v-layout>

        <v-card-text class="body">
          <v-container grid-list-md align-content-center>
            <v-layout row wrap>
              <v-flex
                md2
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                RequestID: {{ accountInfo.requestId }}
              </v-flex>
              <v-flex
                md2
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                Request Type: {{ accountInfo.requestTypeName }}
              </v-flex>
              <v-flex
                md2
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                AccountID: {{ accountInfo.accountId }}
              </v-flex>
              <v-flex
                md2
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                Account Name: {{ accountInfo.fullName }}
              </v-flex>
              <v-flex
                md2
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                Dept: {{ accountInfo.departmentName }}
              </v-flex>
            </v-layout>
            <v-layout mt-2>
              <v-flex
                md5
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                <h4><b>Current Permissions List</b></h4>
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex md10 xs12>
                <v-data-table
                  :headers="headers_permission"
                  :items="table_permission_current"
                  class="elevation-1"
                >
                  <template slot="items" slot-scope="props">
                    <tr>
                      <td class="text-xs-left">
                        {{ props.item.id }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.application }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.groupName }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.listFeture }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.verifyBy }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.verifyTimeString }}
                      </td>
                    </tr>
                  </template>
                </v-data-table>
              </v-flex>
            </v-layout>
            <v-layout mt-2>
              <v-flex
                md5
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                <h4><b>List Permissions Request</b></h4>
              </v-flex>
            </v-layout>
            <v-layout mt-2>
              <v-flex md10 xs12>
                <v-data-table
                  v-model="selected"
                  :headers="headers_permission_verify"
                  :items="table_permission_verify"
                  class="elevation-1"
                >
                  <template slot="items" slot-scope="props">
                    <tr>
                      <td>
                        <v-checkbox
                          :disabled="
                            props.item.verifyStatus == 'APPROVED' ||
                              props.item.verifyStatus == 'REJECTED'
                          "
                          v-model="props.selected"
                          primary
                          hide-details
                        ></v-checkbox>
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.application }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.groupName }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.listFeture }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.action }}
                      </td>
                      <td class="text-xs-left">
                        <v-text-field
                          ref="note"
                          v-model="props.item.verifyNote"
                          label="Note"
                        />
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.verifyStatus }}
                      </td>
                    </tr>
                  </template>
                </v-data-table>
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex md10>
                <v-card-actions>
                  <v-spacer />
                  <v-btn
                    color="#088e7d"
                    class="white--text"
                    @click="handleBtnUpdatePermissionApprove()"
                  >APPROVED
                  </v-btn>
                  <v-btn
                    color="#088e7d"
                    class="white--text"
                    @click="handleBtnUpdatePermissionReject()"
                  >REJECTED
                  </v-btn>
                </v-card-actions>
              </v-flex>
            </v-layout>

            <!-- <v-layout>
              <v-flex md2></v-flex>
              <v-flex md2>
                <v-btn
                  color="#088e7d"
                  class="white--text"
                  @click="handleBtnVerifyRegister"
                  >Submit
                </v-btn>
              </v-flex>
            </v-layout> -->
          </v-container>
        </v-card-text>
      </v-card>
    </v-form>
    <!-- <v-dialog v-model="dialogDelete" max-width="450">
      <v-card>
        <v-card-title class="headline orange primary-title">
          Warning
        </v-card-title>
        <v-card-text>
          <h5>Would you like to delete this file?</h5>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="teal vbutton" text @click="deleteFile()">
            OK
          </v-btn>
          <v-btn color="teal vbutton" text @click="dialogDelete = false">
            Cancel
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog> -->
  </v-dialog>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import FilterTable from 'modules/common/filterTable';

export default {
  $_veeValidate: {
    validator: 'new'
  },
  components: {
    FilterTable
  },
  props: {
    show: {
      type: Boolean,
      default: true
    },
    params: {
      type: Object,
      default() {}
    }
  },

  data() {
    return {
      selectResult: {
        items: [],
        selected: null
      },
      selectApp: {
        items: [],
        selected: null
      },
      selected: [],
      dialog: false,
      active_tab: 'tab-1',
      accountInfo: {
        accountId: '',
        accountName: '',
        requestTypeName: '',
        departmentName: '',
        password: '',
        supervisorCode: '',
        fullName: '',
        mafcCode: '',
        mobile: '',
        email: '',
        personalId: '',
        area: '',
        deptPhone: '',
        position: '',
        group: '',
        branch: '',
        staffType: '',
        team: '',
        level: '',
        note: '',
        isAdmin: false,
        joinDate: null,
        isChangeDept: false,
        newDept: null
      },
      disabledTxtNewDept: true,
      table_permission_current: [],
      headers_permission: [
        { text: '#', sortable: false, value: '' },
        { text: 'Application', sortable: false, value: '' },
        { text: 'Group', sortable: false, value: '' },
        { text: 'Feature List', sortable: false, value: '' },
        { text: 'Verify By', sortable: false, value: '' },
        { text: 'Verify Time', sortable: false, value: '' }
      ],
      decisionNote: '',
      table_permission_verify: [],
      headers_permission_verify: [
        { text: 'Check All', sortable: false, value: '' },
        { text: 'Application', sortable: false, value: '' },
        { text: 'Group', sortable: false, value: '' },
        { text: 'Featue List', sortable: false, value: '' },
        { text: 'Action', sortable: false },
        { text: 'Verify Note', sortable: false, value: '' },
        { text: 'Result', sortable: false, value: '' }
      ]
    };
  },

  computed: {},

  watch: {
    show(value) {
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
      } else {
        this.loadData();
        this.loadResultDecision();
      }
    },
    table_permission_current: {
      handler() {
        for (var i = 0; i < this.table_permission_current.length; i++) {
          this.table_permission_current[i].id = i;
        }
      },
      deep: true
    }
  },

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('verifyTickets', [
      'getRequestDetailByRequestId',
      'verifyRegisterResult',
      'getGroupFeatureCurrentPermission',
      'updVerifyUpdatePermissionRequest'
    ]),

    ...mapActions('requestTicket', [
      'getAllDepartment',
      'getAccountInfoSearchRequestTicket'
    ]),

    emitShownState(flag) {
      this.$emit('shown', flag);
    },

    handleClose() {
      this.dialog = false;
    },

    loadResultDecision() {
      this.selectResult.items = [];
      this.selectResult.selected = null;
      this.selectResult.items.push(
        {
          value: 'APPROVED',
          text: 'APPROVED'
        },
        {
          value: 'REJECTED',
          text: 'REJECTED'
        }
      );
    },
    async loadData() {
      this.accountInfo.requestId = this.params.data.requestId;
      this.accountInfo.requestTypeName = this.params.data.requestTypeName;
      this.accountInfo.accountId = this.params.data.accountId;

      this.loadInfoAccount();
      this.loadCurrentPermissionList();
      this.loadListPermissionVerify();
    },

    async loadInfoAccount() {
      const result_account = await this.getAccountInfoSearchRequestTicket({
        accountId: this.params.data.accountId
      });
      if (result_account.success) {
        if (result_account.data != null) {
          this.accountInfo.fullName = result_account.data[0].fullName;
          this.accountInfo.departmentName =
            result_account.data[0].departmentName;
        }
      }
    },

    async loadCurrentPermissionList() {
      let result = await this.getGroupFeatureCurrentPermission({
        accountId: this.accountInfo.accountId
      });
      if (result.success) {
        this.table_permission_current = result.data;
      }
    },

    async loadListPermissionVerify() {
      let result_verify = await this.getRequestDetailByRequestId({
        requestId: this.accountInfo.requestId
      });
      if (result_verify.success) {
        if (
          result_verify.data.groupPermission != null &&
          result_verify.data.groupPermission.length > 0
        ) {
          this.table_permission_verify = result_verify.data.groupPermission;
        }
      }
    },

    async handleBtnUpdatePermissionApprove() {
      if (this.selected.length > 0) {
        //this.dialogConfirmActiveInactive = true;
        this.statusDecision = 'APPROVED';
        //this.statusActiveInactive = this.params.data.requestTypeName;

        let result = await this.updVerifyUpdatePermissionRequest({
          decision: this.statusDecision,
          requestId: this.params.data.requestId,
          listAccountInfoDetails: this.selected
        });
        if (result.success) {
          this.$message.success(this.statusDecision + ' List account success');
          this.dialogConfirmActiveInactive = false;
          this.table_permission_verify = result.data;
          this.selected = [];
          this.loadListPermissionVerify();
        } else {
          this.$message.error(result.message);
        }
      }
    },

    async handleBtnUpdatePermissionReject() {
      if (this.selected.length > 0) {
        this.statusDecision = 'REJECTED';
        let result = await this.updVerifyUpdatePermissionRequest({
          decision: this.statusDecision,
          requestId: this.params.data.requestId,
          listAccountInfoDetails: this.selected
        });
        if (result.success) {
          this.$message.success(this.statusDecision + ' List account success');
          this.table_permission_verify = result.data;
          this.selected = [];
          this.loadListPermissionVerify();
        } else {
          this.$message.error(result.message);
        }
      }
    },

    async handleBtnVerifyRegister() {
      if (this.selectResult.selected != null) {
        let result = await this.verifyRegisterResult({
          requestId: this.params.data.requestId,
          accountId: this.accountInfo.accountId,
          decision: this.selectResult.selected,
          noteDecision: this.decisionNote
        });
        if (result.success) {
          this.$message.success('Verify update permission account success');
          this.dialog = false;
        } else {
          this.$message.error(result.message);
        }
      } else {
        this.$message.error('Please select Result before change stage');
      }
    }
  }
};
</script>

<style>
.v-tabs__container--grow {
  background-color: #e1e1e1;
}

.v-tabs__item {
  text-transform: capitalize;
}

.v-tabs__item:hover {
  font-weight: 500;
}

.v-tabs__item--active {
  color: rgb(0, 105, 92);
  font-weight: 600;
  background-color: rgba(75, 226, 211, 0.15) !important;
}
.popupAllocation .v-window {
  border: 2px solid #d2d6de;
}
</style>

<style scoped>
.popup .v-card__text {
  padding: 0px !important;
  width: 100%;
}
.popup .v-input--selection-controls {
  margin-top: 0px;
  padding-top: 4px;
}
.v-window {
  border-style: solid;
}
.heightTab {
  height: 500px !important;
}

.rowStyle {
  margin-top: 10px !important;
  margin-left: 10px !important;
  margin-right: 10px !important;
  margin-bottom: 10px !important;
}

.full_text_table {
  white-space: nowrap;
}

.header_cell {
  font-size: 13px;
  font-weight: 600;
  color: var(--v-primary-lighten1);
}

.div-check-maxlength {
  margin-left: 8px;
}
.text-danger {
  color: red !important;
}

.highlight {
  color: red;
}
</style>
