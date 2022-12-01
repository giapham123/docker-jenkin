<template>
  <v-container class="home-app-wrapper" grid-list-md fluid text-xs-center>
    <v-card style="width:100%">
      <v-card
        style=" background-color: #FFFFFF; height: 100%"
        class="overflow_scroll_vertical"
      >
        <v-layout row map>
          <v-card-title style="color:#02786B" class="display-1">
            Underwriting staff information
          </v-card-title>

          <v-layout align-center justify-end row fill-height>
            <v-card-title>{{ upper(me.account_id) }}</v-card-title>
          </v-layout>
        </v-layout>
        <v-container grid-list-md fluid>
          <v-layout row wrap class="block" justify-center>
            <v-flex md12>
              <v-card>
                <v-card-text>
                  <v-layout row wrap>
                    <v-flex md12>
                      <v-text-field
                        ref="accountId"
                        v-model="staff.accountId"
                        label="User name"
                      />
                    </v-flex>
                  </v-layout>
                  <v-layout row wrap>
                    <v-flex md12>
                      <v-text-field
                        ref="mafcCode"
                        v-model="staff.mafcCode"
                        label="Staff code"
                      />
                    </v-flex>
                  </v-layout>
                  <v-layout row wrap>
                    <v-flex md12>
                      <v-text-field
                        ref="personalId"
                        v-model="staff.personalId"
                        label="Personal ID"
                      />
                    </v-flex>
                  </v-layout>
                  <v-layout row wrap>
                    <v-flex md12>
                      <v-text-field
                        ref="phone"
                        v-model="staff.mobile"
                        label="Mobile"
                      />
                    </v-flex>
                  </v-layout>
                  <v-layout row wrap justify-center>
                    <v-flex xs12 md3>
                      <v-btn
                        color="success"
                        class="vbutton"
                        style="margin-top: 20px;"
                        @click="btnSearchClick"
                      >
                        Search
                      </v-btn>
                    </v-flex>
                    <v-flex xs12 md3>
                      <v-btn
                        color="success"
                        class="vbutton"
                        style="margin-top: 20px;"
                        @click="handleBtnAddClick"
                      >
                        Register
                      </v-btn>
                    </v-flex>
                    <v-flex xs12 md3>
                      <v-btn
                        color="success"
                        class="vbutton"
                        style="margin-top: 20px;"
                        @click="handleBtnInsFileClick"
                      >
                        Create by file
                      </v-btn>
                    </v-flex>
                    <v-flex xs12 md3>
                      <v-btn
                        color="success"
                        class="vbutton"
                        style="margin-top: 20px;"
                        @click="handleBtnUpdFileClick"
                      >
                        Update by file
                      </v-btn>
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
                :headers="headers"
                :items="listStaffSearch"
                class="elevation-1"
              >
                <template slot="items" slot-scope="props">
                  <tr>
                    <td class="text-xs-left">{{ props.item.id }}</td>
                    <td class="text-xs-left">{{ props.item.accountId }}</td>
                    <td class="text-xs-left">{{ props.item.fullName }}</td>
                    <td class="text-xs-left">{{ props.item.mobile }}</td>
                    <td class="text-xs-left">{{ props.item.phone }}</td>
                    <td class="text-xs-left">{{ props.item.email }}</td>
                    <td class="text-xs-left">{{ props.item.personalId }}</td>
                    <td class="text-xs-left">
                      {{ props.item.positionCompanyId }}
                    </td>
                    <td class="text-xs-left">{{ props.item.level }}</td>
                    <td class="text-xs-left">{{ props.item.supervisorId }}</td>
                    <td class="text-xs-left">{{ props.item.isAdmin }}</td>
                    <td class="text-xs-left">{{ props.item.group }}</td>
                    <td class="text-xs-left">{{ props.item.team }}</td>
                    <td class="text-xs-left">{{ props.item.staffType }}</td>
                    <td class="text-xs-left">{{ props.item.branchId }}</td>
                    <td class="text-xs-left">{{ props.item.area }}</td>
                    <td class="text-xs-left">{{ props.item.joiningDate }}</td>
                    <td class="text-xs-left">{{ props.item.status }}</td>
                    <td class="text-xs-left">
                      <v-btn
                        icon
                        class="vbutton_icon"
                        @click="handleBtnUpdClick(props.item)"
                      >
                        <v-icon>edit</v-icon>
                      </v-btn>
                    </td>
                    <td class="text-xs-left">
                      <v-btn
                        icon
                        class="vbutton_icon"
                        @click="handleDeleteClick(props.item)"
                      >
                        <v-icon>delete_forever</v-icon>
                      </v-btn>
                    </td>
                  </tr>
                </template>
              </v-data-table>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card>
      <ins-acct-info
        :show="showAddForm.insAcctInfo"
        @shown="showAddForm.insAcctInfo = $event"
      />
      <upd-acct-info
        :show.sync="showAddForm.updAcctInfo"
        :params.sync="edit_model"
      />
      <ins-acct-info-by-file
        :show="showAddForm.insAcctInfoByFile"
        @shown="showAddForm.insAcctInfoByFile = $event"
      />
      <upd-acct-info-by-file
        :show="showAddForm.updAcctInfoByFile"
        @shown="showAddForm.updAcctInfoByFile = $event"
      />
    </v-card>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import InsAcctInfo from './popup_InsAcctInfo';
import UpdAcctInfo from './popup_UpdAcctInfo';
import InsAcctInfoByFile from './popup_InsAcctInfoByFile';
import UpdAcctInfoByFile from './popup_UpdAcctInfoByFile';
export default {
  components: {
    InsAcctInfo,
    UpdAcctInfo,
    InsAcctInfoByFile,
    UpdAcctInfoByFile
  },

  data() {
    return {
      showAddForm: {
        insAcctInfo: false,
        updAcctInfo: false,
        insAcctInfoByFile: false,
        updAcctInfoByFile: false
      },
      staff: {
        accountId: null,
        mafcCode: null,
        personalId: null,
        mobile: null
      },

      edit_model: {},
      listStaffSearch: [],
      headers: [
        { text: 'ID', sortable: false, value: '' },
        { text: 'User name', sortable: false, value: '' },
        { text: 'Full name', sortable: false, value: '' },
        { text: 'Mobile', sortable: false, value: '' },
        { text: 'Debt phone', sortable: false, value: '' },
        { text: 'Email', sortable: false, value: '' },
        { text: 'Personal ID', sortable: false, value: '' },
        { text: 'Position', sortable: false, value: '' },
        { text: 'Level', sortable: false, value: '' },
        { text: 'Manager code', sortable: false, value: '' },
        { text: 'Admin', sortable: false, value: '' },
        { text: 'Group', sortable: false, value: '' },
        { text: 'Team', sortable: false, value: '' },
        { text: 'Staff type', sortable: false, value: '' },
        { text: 'Branch', sortable: false, value: '' },
        { text: 'Area', sortable: false, value: '' },
        { text: 'Join date', sortable: false, value: '' },
        { text: 'Status', sortable: false, value: '' }
      ],
      btnDisabled: true
    };
  },

  computed: {
    ...mapGetters('login', ['me'])
  },

  methods: {
    ...mapActions('und_staff', ['getInfo', 'deleteInfo']),
    ...mapActions('global', ['setLoading']),

    upper(val) {
      return _.toUpper(val);
    },

    handleBtnAddClick() {
      this.showAddForm.insAcctInfo = true;
    },
    handleBtnUpdClick(chooseItem) {
      this.showAddForm.updAcctInfo = true;
      this.edit_model = chooseItem;
    },
    handleDeleteClick(chooseItem) {
      this.btnDelClick(chooseItem);
    },
    handleBtnInsFileClick() {
      this.showAddForm.insAcctInfoByFile = true;
    },

    handleBtnUpdFileClick() {
      this.showAddForm.updAcctInfoByFile = true;
    },

    async btnSearchClick() {
      this.setLoading(true);
      const result = await this.getInfo(this.staff);
      if (result.success) {
        this.listStaffSearch = result.data;
        this.$message.success('Search staff information successfull.');
      } else {
        this.$message.error('Error. Please try again.');
      }
      this.setLoading(false);
    },
    async btnDelClick(item) {
      this.setLoading(true);
      const result = await this.deleteInfo(item);
      if (result.success) {
        this.$message.success('Delete successfull.');
      } else {
        this.$message.error('Error. Please try again.');
      }
      this.setLoading(false);
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
