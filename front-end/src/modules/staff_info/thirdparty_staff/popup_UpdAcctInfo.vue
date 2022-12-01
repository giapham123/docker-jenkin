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
                  Third Party Edit Form
                </v-card-title>
                <v-container grid-list-md fluid>
                  <v-layout row wrap class="block">
                    <v-flex xs4>
                      <v-card>
                        <v-card-text>
                          <v-layout row wrap class="block">
                            <v-flex xs12>
                              <v-text-field
                                ref="mafcCode"
                                v-model="staff.mafcCode"
                                disabled
                                label="Staff code"
                              />
                            </v-flex>
                            <v-flex xs12>
                              <v-text-field
                                ref="accountId"
                                v-model="staff.accountId"
                                disabled
                                label="User name"
                              />
                            </v-flex>
                            <v-flex xs12>
                              <v-text-field
                                ref="supervisorId"
                                v-model="staff.supervisorId"
                                label="Supervisor code"
                              />
                            </v-flex>
                          </v-layout>
                        </v-card-text>
                      </v-card>
                    </v-flex>
                    <v-flex xs8>
                      <v-card>
                        <v-card-text>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <v-text-field
                                v-model="staff.fullName"
                                label="Full name"
                              />
                            </v-flex>
                            <v-flex xs6>
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
                            <v-flex xs6>
                              <v-text-field
                                ref="emailRef"
                                v-model="staff.email"
                                label="Email"
                              />
                            </v-flex>
                            <v-flex xs6>
                              <v-text-field v-model="staff.area" label="Area" />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <v-select
                                ref="branchId"
                                v-model="staff.branchId"
                                :items="listBranch"
                                box
                                label="Branch"
                              />
                            </v-flex>
                            <v-flex xs6>
                              <v-text-field
                                v-model="staff.departmentId"
                                disabled
                                label="Deparment ID"
                              />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <v-text-field
                                v-model="staff.group"
                                label="Group"
                              />
                            </v-flex>
                            <v-flex xs6>
                              <v-text-field v-model="staff.team" label="Team" />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <v-text-field
                                ref="positionCompanyIdRef"
                                v-model="staff.positionCompanyId"
                                label="Position"
                              />
                            </v-flex>
                            <v-flex xs6>
                              <v-select
                                v-model="staff.status"
                                :items="listStatus"
                                label="Status"
                              />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <date-picker
                                v-model="staff.joiningDate"
                                label="Joining Date"
                                icon="event"
                                readonly
                              />
                            </v-flex>
                            <v-flex xs6>
                              <date-picker
                                v-model="staff.endDate"
                                label="End Date"
                                icon="event"
                                readonly
                              />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <v-text-field
                                v-model="staff.staffType"
                                label="Staff type"
                              />
                            </v-flex>
                            <v-flex xs6>
                              <v-text-field
                                ref="level"
                                v-model="staff.level"
                                label="Level"
                              />
                            </v-flex>
                          </v-layout>
                          <v-layout row wrap>
                            <v-flex xs6>
                              <v-checkbox
                                v-model="staff.isAdmin"
                                true-value="1"
                                false-value="0"
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
                @click="handleBtnUpdateClick"
              >
                Edit
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
      default() {
        return {};
      }
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
        departmentId: null,
        createDate: null,
        modifiedDate: null,
        createdBy: null,
        modifiedBy: null,
        deletedDate: null,
        deletedBy: null,
        supervisorId: null,
        staffType: null,
        group: null,
        team: null,
        area: null,
        personalId: null
      },
      listBranch: [],
      listStatus: []
    };
  },

  watch: {
    show(value) {
      this.dialog = value;
      this.loadBranch();
      this.loadStatus();
    },

    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
      }
    },

    params() {
      this.staff = this.params;
    }
  },

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('staff_info', ['getAllBranch', 'getAllStatus']),
    ...mapActions('thirdparty_staff', ['updateInfo']),

    emitShownState(flag) {
      this.$emit('update:show', flag);
    },

    handleBtnUpdateClick() {
      this.updateDrsInfo();
    },

    handleBtnCancelClick() {
      this.emitShownState(false);
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
    },

    async loadStatus() {
      const result = await this.getAllStatus();
      if (result.data != null) {
        var listStatusTemp = [];
        result.data.forEach(element => {
          listStatusTemp.push({
            text: element.statusName,
            value: element.statusCode
          });
        });
        this.listStatus = listStatusTemp;
      } else {
        this.listStatus = [];
      }
    },

    async updateDrsInfo() {
      //const emailPattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      const mobilePattern = /^[0-9]*$/;
      const positionPattern = /^\w{1,5}$/;
      const levelPattern = /^[0-9]*$/;

      if (!mobilePattern.test(this.staff.mobile) || this.staff.mobile == '') {
        this.$refs.cellPhoneRef.focus();
        this.$message.error('Mobile is only number');
      } else if (!positionPattern.test(this.staff.positionCompanyId)) {
        this.$refs.positionCompanyIdRef.focus();
        this.$message.error('Position not more 5 letter');
      } else if (!levelPattern.test(this.staff.level)) {
        this.$refs.level.focus();
        this.$message.error('Level is invalid. Only Number');
      } else {
        const result = await this.updateInfo(this.staff);
        if (result.success) {
          this.$message.success('Update staff information successfull.');
        } else {
          this.$message.error('Error. Please try again');
        }
        this.$emit('update:params', this.staff);
        this.emitShownState(false);
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
