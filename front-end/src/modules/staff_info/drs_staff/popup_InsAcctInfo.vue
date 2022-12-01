<template>
  <v-dialog v-model="dialog" width="80%">
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
                >
                  Direct Sale Staff Information Input Form
                </v-card-title>
                <v-container grid-list-md fluid>
                  <v-layout row wrap class="block">
                    <v-flex xs12 md4>
                      <v-card-text>
                        <v-layout row wrap class="block">
                          <v-flex xs12>
                            <v-text-field
                              ref="mafcCodeRef"
                              v-model="staff.mafcCode"
                              label="Staff code"
                            />
                          </v-flex>
                          <v-flex xs12>
                            <v-text-field
                              ref="accountIdRef"
                              v-model="staff.accountId"
                              label="User name"
                            />
                          </v-flex>
                          <v-flex xs12>
                            <v-text-field
                              v-model="staff.supervisorId"
                              label="Supervisor code"
                            />
                          </v-flex>
                        </v-layout>
                      </v-card-text>
                    </v-flex>
                    <v-flex xs12 md8>
                      <v-card>
                        <v-card-text>
                          <v-layout row wrap>
                            <v-flex xs12 md6>
                              <v-text-field
                                ref="fullNameRef"
                                v-model="staff.fullName"
                                label="Full name"
                              />
                            </v-flex>
                            <v-flex xs12 md6>
                              <v-text-field
                                v-model="staff.personalId"
                                label="Personal ID"
                              />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <v-text-field
                                ref="cellPhoneRef"
                                v-model="staff.mobile"
                                label="Mobile"
                              />
                            </v-flex>
                            <v-flex xs6>
                              <v-text-field
                                ref="debtPhoneRef"
                                v-model="staff.phone"
                                label="Dept phone"
                              />
                            </v-flex>
                            <v-flex xs12 md6>
                              <v-text-field
                                ref="emailRef"
                                v-model="staff.email"
                                label="Email"
                              />
                            </v-flex>
                            <v-flex xs12 md6>
                              <date-picker
                                v-model="staff.joiningDate"
                                label="Joining Date"
                                icon="event"
                                readonly
                              />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-layout row wrap>
                              <v-flex xs12 md6>
                                <v-text-field
                                  v-model="staff.area"
                                  label="Area"
                                />
                              </v-flex>
                              <v-flex xs12 md6>
                                <v-select
                                  ref="branchId"
                                  v-model="staff.branchId"
                                  :items="listBranch"
                                  box
                                  label="Branch"
                                />
                              </v-flex>
                              <v-flex xs12 md6>
                                <v-text-field
                                  v-model="staff.departmentId"
                                  label="Department ID"
                                  disabled
                                />
                              </v-flex>
                              <v-flex xs12 md6>
                                <v-text-field
                                  v-model="staff.group"
                                  label="Group"
                                />
                              </v-flex>
                              <v-flex xs12 md6>
                                <v-text-field
                                  v-model="staff.team"
                                  label="Team"
                                />
                              </v-flex>
                              <v-flex xs12 md6>
                                <v-text-field
                                  ref="positionCompanyIdRef"
                                  v-model="staff.positionCompanyId"
                                  label="Position"
                                />
                              </v-flex>
                            </v-layout>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs12 md6>
                              <v-text-field
                                v-model="staff.staffType"
                                label="Staff type"
                              />
                            </v-flex>
                            <v-flex xs12 md6>
                              <v-text-field
                                ref="level"
                                v-model="staff.level"
                                label="Level"
                              />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs12 md6>
                              <v-checkbox
                                v-model="staff.isAdminTemp"
                                label="Is Admin"
                              />
                            </v-flex>
                          </v-layout>
                        </v-card-text>
                      </v-card>
                    </v-flex>
                  </v-layout>
                </v-container>
              </v-card>
            </v-card-text>
            <v-card-actions>
              <v-spacer />
              <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnAddClick"
              >
                Add
              </v-btn>
              <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnCancelClick"
              >
                Cancel
              </v-btn>
              <v-spacer />
            </v-card-actions>
          </v-card>
        </v-container>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import _ from 'lodash';
import { mapActions } from 'vuex';
import DatePicker from 'modules/common/datePicker';
export default {
  components: {
    DatePicker
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
      valid: true,
      dialog: false,
      staff: {
        id: null,
        fullName: null,
        accountId: null,
        mafcCode: null,
        email: null,
        phone: null,
        mobile: null,
        level: null,
        positionCompanyId: null,
        isDeleted: null,
        joiningDate: null,
        endDate: null,
        status: null,
        branchId: null,
        departmentId: 'DM04',
        createDate: null,
        modifiedDate: null,
        createdBy: null,
        modifiedBy: null,
        deletedDate: null,
        deletedBy: null,
        supervisorId: null,
        isAdminTemp: false,
        staffType: null,
        group: null,
        team: null,
        area: null,
        personalId: null
      },
      listBranch: []
    };
  },

  watch: {
    show(value) {
      this.loadBranch();
      this.staff.joiningDate = new Date().toLocaleDateString('en-US');
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
        this.listBranch = [];
      }
    }
  },

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('staff_info', ['getAllBranch']),
    ...mapActions('drs_staff', ['insertInfo']),
    ...mapActions('global', ['setLoading']),

    emitShownState(flag) {
      this.$emit('shown', flag);
    },

    handleBtnAddClick() {
      this.insertDrsInfo();
    },

    handleBtnCancelClick() {
      this.emitShownState(false);
    },

    async insertDrsInfo() {
      this.setLoading(true);
      if (this.staff.isAdminTemp) {
        this.staff.isAdmin = 1;
      } else {
        this.staff.isAdmin = 0;
      }
      this.staff.status = 'ACT';

      //const emailPattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      const mobilePattern = /^[0-9]*$/;
      const levelPattern = /^[0-9]*$/;
      const positionPattern = /^\w{0,5}$/;

      if (this.staff.mafcCode == null || this.staff.mafcCode == '') {
        this.$refs.mafcCodeRef.focus();
        this.$message.error('Staff Code is null');
      } else if (this.staff.accountId == null || this.staff.accountId == '') {
        this.$refs.accountIdRef.focus();
        this.$message.error('User Name is null');
      } else if (this.staff.fullName == null || this.staff.fullName == '') {
        this.$refs.fullNameRef.focus();
        this.$message.error('Full Name is null');
      } else if (
        !mobilePattern.test(this.staff.mobile) ||
        this.staff.mobile == ''
      ) {
        this.$refs.cellPhoneRef.focus();
        this.$message.error('Mobile is null or invalid');
      } else if (!positionPattern.test(this.staff.positionCompanyId)) {
        this.$refs.positionCompanyIdRef.focus();
        this.$message.error('Position not more 5 letter');
      } else if (
        this.staff.level == null ||
        this.staff.level == '' ||
        !levelPattern.test(this.staff.level)
      ) {
        this.$refs.level.focus();
        this.$message.error('Level is null or invalid');
      } else {
        const result = await this.insertInfo(this.staff);
        if (result.success) {
          if (result.data != null && result.data != '') {
            this.$message.error('This User Information has already existed.');
          } else {
            this.$message.success('Insert staff information successfull.');
            this.$message.success(result.message);
            this.staff = {
              accountId: null,
              fullName: null,
              email: null,
              phone: null,
              mobile: null,
              departmentId: 'DM04',
              level: null,
              supervisorId: null,
              staffType: null,
              team: null,
              area: null,
              group: null,
              status: null,
              personalId: null,
              positionCompanyId: null,
              mafcCode: null
            };
          }
        } else {
          this.$message.error(result.message);
        }
        this.emitShownState(false);
      }
      this.setLoading(false);
    },

    async loadBranch() {
      const result = await this.getAllBranch();
      if (result.data != null) {
        var listBranchTemp = [];
        result.data.forEach(element => {
          listBranchTemp.push({
            text: element.branchName,
            value: element.branchId
          });
        });
        this.listBranch = listBranchTemp;
      } else {
        this.listBranch = [];
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
</style>
