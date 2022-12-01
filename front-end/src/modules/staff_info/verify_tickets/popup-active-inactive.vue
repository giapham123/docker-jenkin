<template>
  <v-dialog
    v-model="dialog"
    :scrollable="true"
    width="80%"
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
              Verify Active/Inactive
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
                md8
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                Request Type: {{ accountInfo.requestTypeName }}
              </v-flex>
            </v-layout>
            <v-layout mt-4 row wrap>
              <v-flex
                md2
                class="font-weight-bold "
                style="color:rgb(0, 105, 92)"
              >
                <h4><b>REQUEST LIST:</b></h4>
              </v-flex>
            </v-layout>
            <v-layout mt-2>
              <v-flex md12 xs12>
                <v-data-table
                  v-model="selected"
                  :headers="headers_permission"
                  :items="list_table_permission"
                  class="elevation-1"
                >
                  <template slot="items" slot-scope="props">
                    <tr>
                      <td class="text-xs-left">
                        {{ props.item.id }}
                      </td>
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
                        {{ props.item.accountId }}
                      </td>
                      <!-- <td class="text-xs-left">
                        {{ props.item.password }}
                      </td> -->
                      <td class="text-xs-left">
                        <v-text-field
                          ref="note"
                          v-model="props.item.note"
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
            <v-card-actions>
              <v-spacer />
              <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnResetPassApprove()"
                >APPROVED
              </v-btn>
              <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnResetPassReject()"
                >REJECTED
              </v-btn>
            </v-card-actions>
          </v-container>
        </v-card-text>
      </v-card>
    </v-form>
    <v-dialog v-model="dialogConfirmActiveInactive" max-width="450">
      <v-card>
        <v-card-title class="headline orange primary-title">
          Warning
        </v-card-title>
        <v-card-text>
          <h5>
            Would you like <b>{{ statusDecision }}</b> to
            {{ statusActiveInactive }}?
            <br />
            <b> {{ listResetPass }} </b>
          </h5>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="teal vbutton"
            class="white--text"
            text
            @click="resetPass()"
          >
            OK
          </v-btn>
          <v-btn
            color="teal vbutton"
            class="white--text"
            text
            @click="dialogConfirmActiveInactive = false"
          >
            Cancel
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
      dialogConfirmActiveInactive: false,
      listResetPass: '',
      statusDecision: '',
      statusActiveInactive: '',
      selectApp: {
        items: [],
        selected: null
      },
      selected: [],
      dialog: false,
      active_tab: 'tab-1',
      accountInfo: {
        accountId: '',
        requestId: '',
        requestTypeName: '',
        password: '',
        note: '',
        isChangeDept: false
      },
      disabledTxtNewDept: true,
      list_table_permission: [],
      headers_permission: [
        { text: '#', sortable: false, value: '' },
        { text: 'Check All', sortable: false, value: '' },
        { text: 'AccountID', sortable: false, value: '' },
        // { text: 'New Password', sortable: false, value: '' },
        { text: 'Note', sortable: false, value: '' },
        { text: 'Result', sortable: false, value: '' }
      ],
      decisionNote: '',
      selectResult: {
        selected: null,
        items: [
          {
            value: 'APPROVED',
            text: 'APPROVED'
          },
          {
            value: 'REJECTED',
            text: 'REJECTED'
          }
        ]
      }
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
        // this.loadResultDecision();
      }
    },

    list_table_permission: {
      handler() {
        for (var i = 0; i < this.list_table_permission.length; i++) {
          this.list_table_permission[i].id = i + 1;
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
      'getListActiveInactiveRequest',
      'updVerifyActiveInactiveRequest'
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
      if (this.params) {
        this.accountInfo.requestId = this.params.data.requestId;
        this.accountInfo.requestTypeName = this.params.data.requestTypeName;
        let result = await this.getListActiveInactiveRequest(
          this.params.data.requestId
        );
        if (result.success) {
          this.list_table_permission = result.data;
        } else {
          this.$message.error(result.message);
        }
      }
    },

    handleBtnResetPassApprove() {
      if (this.selected.length > 0) {
        this.dialogConfirmActiveInactive = true;
        this.statusDecision = 'APPROVED';
        this.statusActiveInactive = this.params.data.requestTypeName;
        this.listResetPass = '';
        this.selected.forEach(element => {
          this.listResetPass += element.accountId + ' ';
        });
      }
    },

    handleBtnResetPassReject() {
      if (this.selected.length > 0) {
        if (this.selected.length > 0) {
          this.dialogConfirmActiveInactive = true;
          this.statusDecision = 'REJECTED';
          this.statusActiveInactive = this.params.data.requestTypeName;
          this.listResetPass = '';
          this.selected.forEach(element => {
            this.listResetPass += element.accountId + ' ';
          });
        }
      }
    },

    async resetPass() {
      let result = await this.updVerifyActiveInactiveRequest({
        decision: this.statusDecision,
        requestId: this.params.data.requestId,
        listAccountInfoDetails: this.selected
      });
      if (result.success) {
        this.$message.success(this.statusDecision + ' List account success');
        this.dialogConfirmActiveInactive = false;
        this.list_table_permission = result.data;
        this.selected = [];
      } else {
        this.$message.error(result.message);
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

.v-dialog:not(.v-dialog--fullscreen) {
  max-height: 90%;
}
</style>
