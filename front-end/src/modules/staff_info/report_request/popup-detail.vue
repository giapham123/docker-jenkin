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
              Request Details
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
                md12
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                Request Type: {{ accountInfo.requestTypeName }}
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex xs4>
                <v-text-field
                  ref="accountId"
                  v-model="accountInfo.accountId"
                  label="Account (*)"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="Deparment"
                  v-model="accountInfo.departmentName"
                  label="Deparment"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="supervisorCode"
                  v-model="accountInfo.supervisorCode"
                  label="SupervisorCode"
                  readonly
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex xs4>
                <v-text-field
                  ref="fullName"
                  v-model="accountInfo.fullName"
                  label="FullName(*)"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="mafcCode"
                  v-model="accountInfo.mafcCode"
                  label="MAFC Code"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="mobile"
                  v-model="accountInfo.mobile"
                  label="Mobile"
                  readonly
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex xs4>
                <v-text-field
                  ref="email"
                  v-model="accountInfo.email"
                  label="Email"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="personalId"
                  v-model="accountInfo.personalId"
                  label="Persional ID"
                  readonly
                />
              </v-flex>
              <v-flex xs4 class="no_padding_vertical">
                <!-- <date-picker
                  v-model="accountInfo.joinDate"
                  label="Joining Date"
                  icon="event"
                /> -->
                <v-text-field
                  ref="joinningDate"
                  v-model="accountInfo.joinDate"
                  label="Joinning Date"
                  readonly
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex xs4>
                <v-text-field
                  ref="area"
                  v-model="accountInfo.area"
                  label="Area"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="deptPhone"
                  v-model="accountInfo.deptPhone"
                  label="Dept Phone"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="position"
                  v-model="accountInfo.position"
                  label="Position"
                  readonly
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex xs4>
                <v-text-field
                  ref="group"
                  v-model="accountInfo.group"
                  label="Group"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <!-- <v-autocomplete
                  v-model="selectBranch.selected"
                  :items="selectBranch.items"
                  label="Branch"
                >
                </v-autocomplete> -->
                <v-text-field
                  ref="group"
                  v-model="accountInfo.branchName"
                  label="Branch"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="staffType"
                  v-model="accountInfo.staffType"
                  label="Staff Type"
                  readonly
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex xs4>
                <v-text-field
                  ref="team"
                  v-model="accountInfo.team"
                  label="Team"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-text-field
                  ref="level"
                  v-model="accountInfo.level"
                  label="Level"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <v-checkbox
                  v-model="accountInfo.isAdmin"
                  label="Is Admin"
                  readonly
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex xs4>
                <v-checkbox
                  v-model="accountInfo.isChangeDept"
                  label="Is Change Dept"
                  readonly
                />
              </v-flex>
              <v-flex xs4>
                <!-- <v-select
                  ref="departmentID"
                  :disabled="disabledTxtNewDept"
                  :items="department"
                  item-text="name"
                  item-value="departmentID"
                  box
                  label="New Dept"
                  autofocus
              /> -->
                <v-text-field
                  ref="level"
                  v-model="accountInfo.newDept"
                  label="New Dept"
                  readonly
                />
              </v-flex>
            </v-layout>
            <v-layout>
              <v-flex md8 xs12>
                <v-data-table
                  :headers="headers_permission"
                  :items="list_table_permission"
                  class="elevation-1"
                >
                  <template slot="items" slot-scope="props">
                    <tr>
                      <td class="text-xs-left">
                        {{ props.item.application }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.groupId }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.listFeture }}
                      </td>
                      <td class="text-xs-left">
                        {{ props.item.action }}
                      </td>
                    </tr>
                  </template>
                </v-data-table>
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
      dialog: false,
      active_tab: 'tab-1',
      accountInfo: {
        accountId: '',
        requestTypeName: '',
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
      list_table_permission: [],
      headers_permission: [
        { text: 'Application', sortable: false, value: '' },
        { text: 'Group', sortable: false, value: '' },
        { text: 'Feature List', sortable: false, value: '' },
        { text: 'Action', sortable: false, value: '' }
      ],
      decisionNote: ''
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
    }
  },

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('verifyTickets', [
      'getRequestDetailByRequestId',
      'verifyRegisterResult'
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
      this.noteDecision = '';
      if (this.params) {
        let result = await this.getRequestDetailByRequestId({
          requestId: this.params.data.requestId
        });
        if (result.success) {
          if (result.data.persionalInfo != null) {
            var resultRequestDetail = result.data.persionalInfo[0];
            this.accountInfo.accountId = resultRequestDetail.accountId;
            this.accountInfo.departmentName =
              resultRequestDetail.departmentName;
            this.accountInfo.password = resultRequestDetail.password;
            this.accountInfo.requestTypeName =
              resultRequestDetail.requestTypeName;
            this.accountInfo.supervisorCode =
              resultRequestDetail.supervisorCode;
            this.accountInfo.fullName = resultRequestDetail.fullName;
            this.accountInfo.mafcCode = resultRequestDetail.mafcCode;
            this.accountInfo.mobile = resultRequestDetail.mobile;
            this.accountInfo.email = resultRequestDetail.email;
            this.accountInfo.personalId = resultRequestDetail.personalId;
            // this.selectBranch.selected = resultRequestDetail.branchId;
            this.accountInfo.joinDate = resultRequestDetail.joinDate;
            this.accountInfo.area = resultRequestDetail.area;
            this.accountInfo.deptPhone = resultRequestDetail.deptPhone;
            this.accountInfo.position = resultRequestDetail.position;
            this.accountInfo.group = resultRequestDetail.group;
            this.accountInfo.staffType = resultRequestDetail.staffType;
            this.accountInfo.team = resultRequestDetail.team;
            this.accountInfo.level = resultRequestDetail.level;
            this.accountInfo.isAdmin =
              resultRequestDetail.isAdmin == null
                ? false
                : resultRequestDetail.isAdmin;
            this.accountInfo.newDept = resultRequestDetail.newDept;
            this.accountInfo.isChangeDept =
              resultRequestDetail.isChangeDept == null
                ? false
                : resultRequestDetail.isChangeDept;
            this.accountInfo.branchName = resultRequestDetail.branchName;
          }

          if (result.data.groupPermission != null) {
            this.list_table_permission = result.data.groupPermission;
          }
        }
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
