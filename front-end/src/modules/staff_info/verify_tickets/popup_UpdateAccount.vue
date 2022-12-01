<template>
  <v-dialog v-model="dialog" width="100%">
    <v-card
      style=" background-color: #FFFFFF; height: 100%"
      class="overflow_scroll_vertical"
    >
      <v-form v-model="valid">
        <v-container class="home-app-wrapper" grid-list-md fluid>
          <v-card style=" background-color: #FFFFFF;">
            <v-card-text style=" background-color: #FFFFFF;">
              <v-card
                style=" background-color: #FFFFFF; border: 1px solid #92D2CD;"
              >
                <v-card-title
                  style=" background-color: #009688;padding-top:4px;padding-bottom: 4px; color:white"
                  >Update Account Info</v-card-title
                >
                <v-container grid-list-md>
                  <v-layout row wrap>
                    <v-flex md12 sm12 xs12>
                      <v-tabs
                        v-model="active_tab"
                        color="white"
                        grow
                        slider-color="#00695c"
                      >
                        <v-tab key="1" :href="'#tab-1'" ripple
                        >Update Info</v-tab
                        >
                        <v-tab key="2" :href="'#tab-2'" ripple>
                          Update Permision
                        </v-tab>
                        <v-tab-item key="1" :value="'tab-1'" class="heightTab">
                          <v-container grid-list-md align-content-center>
                            <v-layout>
                              <v-flex xs4>
                                <v-text-field
                                  ref="accountId"
                                  :rules="accountIdRules"
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
                                />
                              </v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-text-field
                                  ref="fullName"
                                  v-model="accountInfo.fullName"
                                  label="FullName(*)"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-text-field
                                  ref="mafcCode"
                                  v-model="accountInfo.mafcCode"
                                  label="MAFC Code"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-text-field
                                  ref="mobile"
                                  v-model="accountInfo.mobile"
                                  label="Mobile"
                                />
                              </v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-text-field
                                  ref="email"
                                  v-model="accountInfo.email"
                                  label="Email"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-text-field
                                  ref="personalId"
                                  v-model="accountInfo.personalId"
                                  label="Persional ID"
                                />
                              </v-flex>
                              <v-flex xs4 class="no_padding_vertical">
                                <date-picker
                                  v-model="accountInfo.joinDate"
                                  label="Joining Date"
                                  icon="event"
                                />
                              </v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-text-field
                                  ref="area"
                                  v-model="accountInfo.area"
                                  label="Area"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-text-field
                                  ref="deptPhone"
                                  v-model="accountInfo.deptPhone"
                                  label="Dept Phone"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-text-field
                                  ref="position"
                                  v-model="accountInfo.position"
                                  label="Position"
                                />
                              </v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-text-field
                                  ref="group"
                                  v-model="accountInfo.group"
                                  label="Group"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-autocomplete
                                  v-model="selectBranch.selected"
                                  :items="selectBranch.items"
                                  label="Branch"
                                >
                                </v-autocomplete>
                              </v-flex>
                              <v-flex xs4>
                                <v-text-field
                                  ref="staffType"
                                  v-model="accountInfo.staffType"
                                  label="Staff Type"
                                />
                              </v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-text-field
                                  ref="team"
                                  v-model="accountInfo.team"
                                  label="Team"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-text-field
                                  ref="level"
                                  v-model="accountInfo.level"
                                  label="Level"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-checkbox
                                  v-model="accountInfo.isAdmin"
                                  label="Is Admin"
                                />
                              </v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-checkbox
                                  v-model="accountInfo.isChangeDept"
                                  label="Is Change Dept"
                                  @change="changeIsChangeDept"
                                />
                              </v-flex>
                              <v-flex xs4>
                                <v-select
                                  ref="departmentID"
                                  :disabled="disabledTxtNewDept"
                                  :items="department"
                                  item-text="name"
                                  item-value="departmentID"
                                  box
                                  label="New Dept"
                                  autofocus
                                  @change="selectDep"
                              /></v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-textarea
                                  ref="note"
                                  v-model="accountInfo.note"
                                  label="Note"
                                />
                              </v-flex>
                              <v-flex md4>
                                <v-btn
                                  color="#088e7d"
                                  class="white--text"
                                  center
                                  @click="handleBtnSubmitUpdateInfo"
                                >
                                  Submit
                                </v-btn>
                              </v-flex>
                            </v-layout>
                          </v-container>
                        </v-tab-item>
                        <v-tab-item key="2" :value="'tab-2'" class="heightTab">
                          <v-container grid-list-md align-content-center>
                            <v-layout>
                              <v-flex md12>
                                <v-data-table
                                  :headers="headers"
                                  :items="tablePermissionCurrent"
                                  class="elevation-1"
                                >
                                  <template slot="items" slot-scope="props">
                                    <tr>
                                      <td class="text-xs-left">
                                        {{ props.item.id }}
                                      </td>
                                      <td class="text-xs-left">
                                        {{ props.item.accountId }}
                                      </td>
                                      <td class="text-xs-left">
                                        {{ props.item.groupName }}
                                      </td>
                                      <td class="text-xs-left">
                                        {{ props.item.listFeture }}
                                      </td>
                                      <td class="text-xs-left">
                                        {{ props.item.requestBy }}
                                      </td>
                                      <td class="text-xs-left">
                                        {{ props.item.requestTimeString }}
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
                            <v-layout row wrap>
                              <v-flex xs8>
                                <v-layout row wrap>
                                  <v-flex xs4>
                                    <v-autocomplete
                                      v-model="selectApp.selected"
                                      :items="selectApp.items"
                                      label="Application"
                                      item-text="text"
                                      item-value="value"
                                      multiple
                                      @change="changeCbbSelectApp"
                                    >
                                      <template
                                        slot="selection"
                                        slot-scope="data"
                                      >
                                        <v-chip
                                          :selected="data.selected"
                                          close
                                          class="chip--select-multi"
                                          @input="clearSelectedItem(data.item)"
                                        >
                                          {{ data.item.text }}
                                        </v-chip>
                                      </template>
                                    </v-autocomplete>
                                  </v-flex>
                                </v-layout>
                              </v-flex>
                            </v-layout>
                            <v-layout>
                              <v-flex xs4>
                                <v-textarea
                                  ref="note"
                                  v-model="permissionNote"
                                  label="Note"
                                />
                              </v-flex>
                            </v-layout>
                            <v-layout wrap row class="rowStyle">
                              <v-flex md5>
                                <div
                                  style="position: relative"
                                  class="no_box_shadow"
                                >
                                  <filter-table
                                    ref="table"
                                    :headers="header_permision"
                                    :data="table_permission"
                                    :setting_header="false"
                                    primary="GROUPID"
                                    class="elevation-1 full_box_table"
                                  >
                                    <template slot="tbody" slot-scope="props">
                                      <tr
                                        :class="{
                                          highlight:
                                            validatorDataLeft(props.item) ==
                                            true,
                                          movedhighlight:
                                            validatorMovedDataLeft(
                                              props.item
                                            ) == true
                                        }"
                                        class="border_top"
                                        @click="chooseFromLeft(props.item)"
                                      >
                                        <td>
                                          {{ props.item.APPLICATION }}
                                        </td>
                                        <td>
                                          {{ props.item.GROUPNAME }}
                                        </td>
                                        <td>
                                          {{ props.item.LIST_FEATURE }}
                                        </td>
                                      </tr>
                                    </template>
                                  </filter-table>
                                </div>
                              </v-flex>
                              <v-flex md2 class="text-xs-center">
                                <div
                                  style="position: relative"
                                  class="no_box_shadow"
                                >
                                  <v-btn
                                    depressed
                                    color="rgb(0, 105, 92)"
                                    class="white--text"
                                    @click="moveLeft"
                                  >
                                    <v-icon>arrow_back</v-icon>
                                  </v-btn>
                                </div>
                                <div
                                  style="position: relative"
                                  class="no_box_shadow  row wrap"
                                >
                                  <v-btn
                                    depressed
                                    color="rgb(0, 105, 92)"
                                    class="white--text"
                                    @click="moveRight"
                                  >
                                    <v-icon>arrow_forward</v-icon>
                                  </v-btn>
                                </div>
                                <div
                                  style="position: relative"
                                  class="no_box_shadow  row wrap"
                                >
                                  <v-btn
                                    color="#088e7d"
                                    class="white--text"
                                    @click="handleBtnSubmitUpdatePermission"
                                  >
                                    Submit
                                  </v-btn>
                                </div>
                              </v-flex>

                              <v-flex md5>
                                <div
                                  style="position: relative"
                                  class="no_box_shadow"
                                >
                                  <filter-table
                                    ref="table"
                                    :headers="header_permision"
                                    :data="table_permission_request"
                                    :setting_header="false"
                                    primary="GROUPID"
                                    class="elevation-1 full_box_table"
                                  >
                                    <template slot="tbody" slot-scope="props">
                                      <tr
                                        :class="{
                                          highlight:
                                            validatorDataRight(props.item) ==
                                            true,
                                          movedhighlight:
                                            validatorMovedDataRight(
                                              props.item
                                            ) == true
                                        }"
                                        class="border_top"
                                        @click="chooseFromRight(props.item)"
                                      >
                                        <td>
                                          {{ props.item.APPLICATION }}
                                        </td>
                                        <td>
                                          {{ props.item.GROUPNAME }}
                                        </td>
                                        <td>
                                          {{ props.item.LIST_FEATURE }}
                                        </td>
                                      </tr>
                                    </template>
                                  </filter-table>
                                </div>
                              </v-flex>
                            </v-layout>
                          </v-container>
                        </v-tab-item>
                      </v-tabs>
                    </v-flex>
                  </v-layout>
                </v-container>
              </v-card>
            </v-card-text>
            <v-card-actions>
              <v-spacer />
              <!-- <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnSubmitUpdatePermission"
                >Submit
              </v-btn> -->
              <!-- <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnCancelClick"
              >
                {{ $t('button.close') }}
              </v-btn> -->
            </v-card-actions>
          </v-card>
        </v-container>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex';
import DatePicker from 'modules/common/datePicker';
import FilterTable from 'modules/common/filterTable';

export default {
  components: {
    DatePicker,
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
      selectBranch: {
        items: [],
        selected: null
      },
      selectApp: {
        items: [],
        selected: null
      },
      disabledTxtNewDept: true,
      accountInfo: {
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
      permissionNote: '',
      valid: true,
      dialog: false,
      employee: {
        id: null,
        accountId: '',
        featureCodeId: null,
        buttonCodeId: null,
        featureName: null,
        departmentName: null,
        tableDepartment: null
      },
      listAcctPermissFeatureButton: [],
      listFeature: [],
      headers: [
        { text: '#', sortable: false },
        { text: 'APPLICATION', sortable: false },
        { text: 'GROUP', sortable: false },
        { text: 'FEATURE', sortable: false },
        { text: 'Request By', sortable: false },
        { text: 'Request Time', sortable: false },
        { text: 'Verify By', sortable: false },
        { text: 'Verify Time', sortable: false }
      ],
      accountIdRules: [v => !!v || 'Account ID field is required'],

      table_permission: [],
      table_permission_request: [],
      active_tab: 'tab-1',
      department: [],
      header_permision: [
        {
          text: 'Application',
          sortable: false,
          value: 'APPLICATION',
          type: 'string'
        },
        { text: 'Group', sortable: false, value: 'GROUPNAME', type: 'string' },
        {
          text: 'Feature',
          sortable: false,
          value: 'LIST_FEATURE',
          type: 'string'
        }
      ],
      tablePermissionCurrent: [],
      itemFromLeft: {},
      itemFromRight: {},
      regionId: '',
      selectedIdListLeft: [],
      movedListLeft: [],
      selectedIdListRight: [],
      movedListRight: []
    };
  },

  computed: {
    likesAllStatus() {
      return this.selectApp.selected.length === this.selectApp.items.length;
    },

    likesSomeStatus() {
      return this.selectApp.selected.length > 0 && !this.likesAllStatus;
    },

    icon() {
      if (this.likesAllStatus) return 'mdi-close-box';
      if (this.likesSomeStatus) return 'mdi-minus-box';
      return 'mdi-checkbox-blank-outline';
    }
  },

  watch: {
    tablePermissionCurrent: {
      handler() {
        for (var i = 0; i < this.tablePermissionCurrent.length; i++) {
          this.tablePermissionCurrent[i].id = i;
        }
      },
      deep: true
    },
    table_permission: {
      handler() {
        for (var i = 0; i < this.table_permission.length; i++) {
          this.table_permission[i].no = i;
        }
      },
      deep: true
    },

    table_permission_request: {
      handler() {
        for (var i = 0; i < this.table_permission_request.length; i++) {
          this.table_permission_request[i].no = i;
        }
      },
      deep: true
    },
    show(value) {
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
      } else {
        // this.loadAcctPermissionFeatureButton();
        this.loadCbbBranch();
        this.loadInfoAccountInfo();
        this.getDepartment();
        this.getGroupCurrentAcctPermission();
        // this.loadRegionDesByRegionId();

        this.loadListAppByDepartmentId();
      }
    }
  },

  created() {
    this.dialog = this.show;
    this.table_permission = [];
    this.table_permission_request = [];
    this.accountInfo.joinDate = new Date().toLocaleDateString('en-US');
  },

  methods: {
    ...mapActions('requestTicket', [
      'getAllBranch',
      'getFeatureInfo',
      'getAppByDepartmentId',
      'getListPermissionGroupFeatureByApp',
      'getAcctPermissionFeatureButton',
      'addAcctPermissionFeatureButton',
      'delAcctPermissionFeatureButton',
      'convertStringToList',
      'createAccountInfoDetail',
      'getInfoAccountInfoDetail',
      'createRequestUpdateAccountInfo',
      'getAllDepartment',
      'getGroupFeatureCurrentPermission',
      'createRequestUpdatePermission'
    ]),

    toggle() {
      this.$nextTick(() => {
        if (this.likesAllStatus) {
          this.formSearch.statusSelected = [];
        } else {
          this.formSearch.statusSelected = this.formSearch.listStatus.slice();
        }
      });
    },

    async loadInfoAccountInfo() {
      this.disabledTxtNewDept = true;
      this.active_tab = 'tab-1';
      let paramsAcct = {
        accountId: this.params.data.accountId,
        departmentId: this.params.data.departmentId,
        tableDepartment: this.params.data.tableDepartment
      };

      let result = await this.getInfoAccountInfoDetail(paramsAcct);
      if (result.success) {
        if (result.data != null) {
          this.accountInfo.accountId = result.data.accountId;
          this.accountInfo.departmentName = result.data.departmentName;
          this.accountInfo.supervisorCode = null;
          this.accountInfo.fullName = result.data.fullName;
          this.accountInfo.mafcCode = result.data.mafcCode;
          this.accountInfo.mobile = result.data.mobile;
          this.accountInfo.email = result.data.email;
          this.accountInfo.personalId = result.data.personalId;
          this.selectBranch.selected = result.data.branchId;
          this.accountInfo.joinDate = result.data.joinDate;
          this.accountInfo.area = result.data.area;
          this.accountInfo.deptPhone = result.data.phone;
          this.accountInfo.position = result.data.position;
          this.accountInfo.group = result.data.group;
          this.accountInfo.staffType = result.data.staffType;
          this.accountInfo.team = result.data.team;
          this.accountInfo.level = result.data.level;
          this.accountInfo.isAdmin =
            result.data.isAdmin == null ? false : result.data.isAdmin;
        }
        this.accountInfo.isChangeDept = false;
      }
    },

    selectDep(departmentID) {
      this.accountInfo.newDept = departmentID;
    },

    async getDepartment() {
      const result = await this.getAllDepartment();
      if (result.data != null) {
        this.department = result.data == null ? '' : result.data;
      }
    },

    async getGroupCurrentAcctPermission() {
      // this.params.data.accountId,
      // this.params.data.departmentId
      let result = await this.getGroupFeatureCurrentPermission({
        accountId: this.params.data.accountId
      });
      if (result.success) {
        this.tablePermissionCurrent = result.data;
        result.data.forEach(element => {
          this.table_permission_request.push({
            GROUPID: element.groupId,
            APPLICATION: element.application,
            GROUPNAME: element.groupName,
            LIST_FEATURE: element.listFeture
          });
        });
      }
    },

    changeIsChangeDept() {
      if (this.accountInfo.isChangeDept == false) {
        this.disabledTxtNewDept = true;
      } else this.disabledTxtNewDept = false;
    },

    async loadCbbBranch() {
      this.selectBranch.items = [];
      this.selectBranch.selected = null;

      let result = await this.getAllBranch();
      if (result.success) {
        result.data.forEach(element => {
          this.selectBranch.items.push({
            value: element.branchId,
            text: element.branchName
          });
        });
      }
    },

    async loadListAppByDepartmentId() {
      this.table_permission = [];
      this.table_permission_request = [];
      this.selectApp.items = [];
      this.selectApp.selected = null;
      let result = await this.getAppByDepartmentId(
        this.params.data.departmentId
      );
      if (result.success) {
        result.data.forEach(element => {
          this.selectApp.items.push({
            value: element,
            text: element
          });
        });
      }
    },

    async loadRegionDesByRegionId() {
      this.selectedIdListLeft = [];
      this.movedListLeft = [];
      this.selectedIdListRight = [];
      this.movedListRight = [];
      this.table_loading = true;

      // this.table_permission = this.tablePermissionCurrent;

      // this.tablePermissionCurrent = this.tablePermissionCurrent;
      this.table_loading = false;
    },

    async changeCbbSelectApp() {
      let appList = await this.convertStringToList(this.selectApp.selected);
      let result = await this.getListPermissionGroupFeatureByApp({
        listAppCode: appList
      });

      if (result.success) {
        if (result.data != null) {
          var listGroupIdPermission = result.data;
          //handle check trùng
          listGroupIdPermission.forEach(element => {
            var isCoincideGroup = false;
            this.table_permission_request.forEach(elementRequest => {
              if (element.GROUPID == elementRequest.GROUPID) {
                isCoincideGroup = true;
              }
            });
            if (isCoincideGroup == false) {
              this.table_permission.push(element);
            }
          });
        } else this.table_permission = [];
      }

      this.selectedIdListLeft = [];
      this.movedListLeft = [];
      // this.selectedIdListRight = [];
      // this.movedListRight = [];
      // this.table_permission_request = [];
    },

    emitShownState(flag) {
      this.$emit('shown', flag);
    },
    selectFeature(codeId) {
      this.employee.featureCodeId = codeId;
    },

    handleBtnClearClick() {
      this.accountInfo.supervisorCode = '';
      this.accountInfo.fullName = '';
      this.accountInfo.mafcCode = '';
      this.accountInfo.mobile = '';
      this.accountInfo.email = '';
      this.accountInfo.personalId = '';
      this.accountInfo.joinDate = null;
      this.accountInfo.area = '';
      this.accountInfo.deptPhone = '';
      this.accountInfo.staffType = '';
      this.accountInfo.team = '';
      this.accountInfo.level = '';
      this.accountInfo.note = '';
      this.accountInfo.isAdmin = false;
    },

    async handleBtnSubmitUpdatePermission() {
      var listResultRequest = [];
      this.table_permission_request.forEach(element => {
        listResultRequest.push(element.GROUPID);
      });

      let result = await this.createRequestUpdatePermission({
        listGroupPermission: listResultRequest,
        accountId: this.params.data.accountId,
        note: this.permissionNote
      });
      if (result.success) {
        this.$message.success(result.message);
      } else {
        this.$message.error(result.message);
      }
    },

    async handleBtnSubmitUpdateInfo() {
      this.employee.accountId = this.params.data;
      if (this.employee.accountId == null || this.employee.accountId == '') {
        this.$refs.accountId.focus();
      } else if (
        this.accountInfo.fullName == null ||
        this.accountInfo.fullName == ''
      ) {
        this.$refs.fullName.focus();
      } else {
        let result = await this.createRequestUpdateAccountInfo({
          accountId: this.accountInfo.accountId,
          fullName: this.accountInfo.fullName,
          mafcCode: this.accountInfo.mafcCode,
          email: this.accountInfo.email,
          departmentId: this.params.departmentId,
          personalId: this.accountInfo.personalId,
          mobile: this.accountInfo.mobile,
          deptPhone: this.accountInfo.deptPhone,
          supervisorCode: this.accountInfo.supervisorCode,
          staffType: this.accountInfo.staffType,
          position: this.accountInfo.position,
          group: this.accountInfo.group,
          team: this.accountInfo.team,
          isAdmin: this.accountInfo.isAdmin == true ? 1 : 0,
          // status: 'ACTIVE',
          area: this.accountInfo.area,
          level: this.accountInfo.level,
          branchId: this.selectBranch.selected,
          joinDate: this.accountInfo.joinDate,
          note: this.accountInfo.note,
          isChangeDept: this.accountInfo.isChangeDept == true ? 1 : 0,
          newDept: this.accountInfo.newDept
        });

        if (result.success == true) {
          if (result.data != null && result.data != '') {
            this.$message.error('Error. Please try again.');
          } else {
            this.$message.success('Update account successfull.');
            this.dialog = false;
          }
        } else {
          this.$message.error('Error. Please try again.');
        }
      }
    },

    async handleBtnDelClick(item) {
      this.employee = {
        id: item.id,
        accountId: item.accountId,
        featureCodeId: item.featureCodeId
      };
      let result = await this.delAcctPermissionFeatureButton(this.employee);
      if (result.success) {
        this.loadAcctPermissionFeatureButton();
      } else {
        this.$message.error(result.message);
      }
    },
    handleBtnCancelClick() {
      this.emitShownState(false);
    },

    async loadFeatureInfo() {
      const result = await this.getFeatureInfo();
      if (result.data != null) {
        this.listFeature = result.data;
      } else {
        this.listFeature = [];
      }
    },
    async loadAcctPermissionFeatureButton() {
      this.employee.accountId = this.params.data;
      this.employee.featureCodeId = null;
      let result = await this.getAcctPermissionFeatureButton(this.employee);
      if (result.data != '') {
        this.listAcctPermissFeatureButton = result.data;
      } else {
        this.listAcctPermissFeatureButton = [];
      }
    },

    preventChar(e) {
      if (e.keyCode == 13) {
        this.onSearchRelease();
      } else if (e.keyCode < 48 || e.keyCode > 57) {
        e.preventDefault();
      }
    },

    validatorDataLeft(value) {
      return this.selectedIdListLeft.includes(value);
    },
    validatorDataRight(value) {
      return this.selectedIdListRight.includes(value);
    },
    validatorMovedDataRight(value) {
      return this.movedListRight.includes(value.id);
    },
    validatorMovedDataLeft(value) {
      return this.movedListLeft.includes(value.id);
    },

    async chooseFromLeft(params) {
      let checkExist = false;
      this.movedListLeft = [];
      this.selectedIdListRight = [];
      for (var i = 0; i < this.selectedIdListLeft.length; i++) {
        if (this.selectedIdListLeft[i] == params) {
          this.selectedIdListLeft.splice(i, 1);
          checkExist = true;
        }
      }
      if (!checkExist) {
        this.selectedIdListLeft.push(params);
      }
    },

    async chooseFromRight(params) {
      let checkExist = false;
      this.movedListRight = [];
      this.selectedIdListLeft = [];
      for (var i = 0; i < this.selectedIdListRight.length; i++) {
        if (this.selectedIdListRight[i] == params) {
          this.selectedIdListRight.splice(i, 1);
          checkExist = true;
        }
      }
      if (!checkExist) {
        this.selectedIdListRight.push(params);
      }
    },

    async moveRight() {
      if (this.selectedIdListLeft.length == 0) {
        this.$message.warning({
          message: 'Please choose a district!',
          options: { position: { y: 'top', x: '' } }
        });
        return false;
      }
      this.movedListRight = [];
      for (var i = 0; i < this.selectedIdListLeft.length; i++) {
        this.movedListRight.push(this.selectedIdListLeft[i].id);
        this.table_permission_request.unshift(this.selectedIdListLeft[i]);
        for (var j = 0; j < this.table_permission.length; j++) {
          if (this.table_permission[j].id == this.selectedIdListLeft[i].id) {
            this.table_permission.splice(this.table_permission[j].no, 1);
            this.resetNo();
            break;
          }
        }
      }
      this.selectedIdListLeft = [];
      this.resetNo();
    },
    async moveLeft() {
      if (this.selectedIdListRight.length == 0) {
        this.$message.warning({
          message: 'Please choose an other district!',
          options: { position: { y: 'top', x: '' } }
        });
        return false;
      }
      this.movedListLeft = [];
      for (var i = 0; i < this.selectedIdListRight.length; i++) {
        this.movedListLeft.push(this.selectedIdListRight[i].id);
        this.table_permission.unshift(this.selectedIdListRight[i]);
        for (var j = 0; j < this.table_permission_request.length; j++) {
          if (
            this.table_permission_request[j].id ==
            this.selectedIdListRight[i].id
          ) {
            this.table_permission_request.splice(
              this.table_permission_request[j].no,
              1
            );
            this.resetNo();
            break;
          }
        }
      }
      this.selectedIdListRight = [];
      this.resetNo();
    },
    async resetNo() {
      for (var i = 0; i < this.table_permission.length; i++) {
        this.table_permission[i].no = i;
      }

      for (var i = 0; i < this.table_permission_request.length; i++) {
        this.table_permission_request[i].no = i;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.file-select > .select-button {
  padding-top: 1rem;
  padding-bottom: 1rem;
  padding-right: 3%;
  padding-left: 3%;
  color: white;
  background-color: #2ea169;
  border-radius: 0.3rem;
  text-align: center;
  font-weight: bold;
}

.vbutton_icon {
  background-color: #088e7d !important;
}

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
.highlight {
  background-color: rgba(75, 226, 211, 0.15) !important;
}
.movedhighlight {
  background-color: rgb(127, 245, 186) !important;
}
tr:hover {
  cursor: pointer;
}

.heightTab {
  height: 775px !important;
}

.search_header {
  background-color: white !important;
}
</style>
