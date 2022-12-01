<template>
  <v-container class="home-app-wrapper" grid-list-md fluid text-xs-center>
    <v-card style="width:100%">
      <v-card
        style=" background-color: #FFFFFF; height: 100%"
        class="overflow_scroll_vertical"
      >
        <v-layout row map>
          <v-card-title style="color:#02786B" class="display-1">
            Direct Sales Detail Information
          </v-card-title>

          <v-layout align-center justify-end row fill-height>
            <v-card-title>{{ me.account_id }}</v-card-title>
          </v-layout>
        </v-layout>
        <v-layout row wrap justify-center>
          <v-flex xs12 md3>
            <v-text-field v-model="staff.accountId" label="DRS code" />
          </v-flex>
          <v-flex xs12 md3>
            <v-btn color="success" class="vbutton" @click="btnSearchClick">
              Search
            </v-btn>
          </v-flex>
          <v-flex xs12 md3>
            <v-btn
              color="success"
              class="vbutton"
              @click="handleBtnExportClick"
            >
              Export
            </v-btn>
          </v-flex>
          <v-flex xs12 md3>
            <v-btn color="success" class="vbutton" @click="handleUpdateDetail">
              Update Detail
            </v-btn>
          </v-flex>
        </v-layout>
        <v-container grid-list-md fluid>
          <v-layout>
            <v-flex md12>
              <v-data-table
                :headers="headers"
                :items="listStaffSearch"
                :rows-per-page-items="rowsPerPageItems"
                class="elevation-1"
              >
                <template slot="items" slot-scope="props">
                  <tr>
                    <td class="text-xs-left">{{ props.item.id }}</td>
                    <td class="text-xs-left">{{ props.item.accountId }}</td>
                    <td class="text-xs-left">{{ props.item.fullName }}</td>
                    <td class="text-xs-left">{{ props.item.birthday }}</td>
                    <td class="text-xs-left">{{ props.item.gender }}</td>
                    <td class="text-xs-left">{{ props.item.idNo }}</td>
                    <td class="text-xs-left">{{ props.item.idDate }}</td>
                    <td class="text-xs-left">{{ props.item.placeId }}</td>
                    <td class="text-xs-left">{{ props.item.permanentAdd }}</td>
                    <td class="text-xs-left">{{ props.item.currentAdd }}</td>
                    <td class="text-xs-left">{{ props.item.taxCode }}</td>
                    <td class="text-xs-left">
                      <v-btn
                        icon
                        class="vbutton_icon"
                        @click="handleBtnUpdClick(props.item)"
                      >
                        <v-icon>edit</v-icon>
                      </v-btn>
                    </td>
                  </tr>
                </template>
              </v-data-table>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card>

      <upd-acct-info
        :show.sync="showAddForm.updAcctInfo"
        :params.sync="edit_model"
      />
    </v-card>
  </v-container>
</template>

<script>
import Exporter from 'modules/common/JsonExporter.js';
import { mapActions, mapGetters } from 'vuex';
import UpdAcctInfo from './popup_UpdAcctInfo';
export default {
  components: {
    UpdAcctInfo
  },

  data() {
    return {
      showAddForm: {
        updAcctInfo: false
      },
      accountName: null,
      accountIsAdmin: false,
      staff: {
        accountId: null
      },
      m123: {},
      rowsPerPageItems: [10, 50, 100],
      edit_model: {},
      listData: [],
      listStaffSearch: [],
      headers: [
        { text: 'ID', sortable: false, value: '' },
        { text: 'DRS Code', sortable: false, value: '' },
        { text: 'Name', sortable: false, value: '' },
        { text: 'Birthday', sortable: false, value: '' },
        { text: 'Gender', sortable: false, value: '' },
        { text: 'ID No', sortable: false, value: '' },
        { text: 'ID Date', sortable: false, value: '' },
        { text: 'Place of ID', sortable: false, value: '' },
        { text: 'Permanent address', sortable: false, value: '' },
        { text: 'Current address', sortable: false, value: '' },
        { text: 'Tax code', sortable: false, value: '' }
      ]
    };
  },

  created() {
    this.checkAdmin();
  },

  computed: {
    ...mapGetters('login', ['me'])
  },

  methods: {
    ...mapActions('drs_detail', ['getInfo', 'insertInfo', 'getAccount']),
    ...mapActions('drs_staff', ['getInfoAccount']),
    ...mapActions('global', ['setLoading']),

    handleBtnUpdClick(chooseItem) {
      this.showAddForm.updAcctInfo = true;
      this.edit_model = chooseItem;
    },

    handleBtnExportClick() {
      this.export_loading = true;
      let exporter = new Exporter('drsDetail');
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#1f4e78'
      });
      let headers = {
        id: {
          text: 'ID',
          type: 'String',
          width: 80
        },
        accountId: {
          text: 'DRS code',
          type: 'String',
          width: 80
        },
        fullName: {
          text: 'Name',
          type: 'String',
          width: 80
        },
        birthday: {
          text: 'Birthday',
          type: 'String',
          width: 80
        },
        gender: {
          text: 'Gender',
          type: 'String',
          width: 80
        },
        idNo: {
          text: 'ID No',
          type: 'String',
          width: 80
        },
        idDate: {
          text: 'ID Date',
          type: 'String',
          width: 80
        },
        placeId: {
          text: 'Place ID',
          type: 'String',
          width: 80
        },
        permanentAdd: {
          text: 'Permanent address',
          type: 'String',
          width: 80
        },
        currentAdd: {
          text: 'Current address',
          type: 'String',
          width: 80
        },
        taxCode: {
          text: 'Tax code',
          type: 'String',
          width: 80
        }
      };

      exporter.addSheet(headers, this.listStaffSearch, 'Sheet0');
      let result = exporter.exportExcel();

      if (result.success) {
        this.$message.success(result.message);
      } else {
        this.$message.error(result.message);
      }
      this.export_loading = false;
    },

    async btnSearchClick() {
      this.setLoading(true);

      if (this.accountIsAdmin) {
        const result = await this.getInfo(this.staff);
        if (result.success) {
          this.listStaffSearch = result.data;
          this.$message.success('Search staff information successfull.');
        } else {
          this.$message.error('Error. Please try again.');
        }
      } else {
        this.notAdmin = {
          accountId: this.me.account_id
        };
        const result = await this.getInfo(this.notAdmin);
        if (result.success) {
          this.listStaffSearch = result.data;
          this.$message.success('Search staff information successfull.');
        } else {
          this.$message.error('Error. Please try again.');
        }
      }
      this.setLoading(false);
    },

    async handleUpdateDetail() {
      this.m123 = {
        accountId: this.me.account_id
      };
      const resultDetail = await this.getAccount(this.m123);
      if (resultDetail.data.length < 1) {
        const resultDrs = await this.getInfoAccount(this.m123);
        if (resultDrs.data.length > 0) {
          const result1 = await this.insertInfo(this.m123);
          if (result1.success) {
            this.$message.success('Insert Detail');
          }
        } else {
          this.$message.error('Not in DRS');
        }
      } else {
        this.$message.error('Account already Detail');
      }
    },

    async checkAdmin() {
      this.m123 = {
        accountId: this.me.account_id
      };
      const result = await this.getInfoAccount(this.m123);
      if (result.data[0].isAdmin == 1) {
        this.accountIsAdmin = true;
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
