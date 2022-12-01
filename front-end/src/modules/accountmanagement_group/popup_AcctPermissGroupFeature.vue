<template>
  <v-dialog v-model="dialog" width="60%">
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
                  >Account Permission Feature</v-card-title
                >
                <v-container grid-list-md>
                  <v-layout>
                    <v-flex xs8>
                      <v-text-field
                        ref="accountId"
                        :rules="accountIdRules"
                        v-model="params.data"
                        label="MAFC code"
                        readonly
                      />
                    </v-flex>
                  </v-layout>
                  <v-layout row wrap>
                    <v-flex xs8>
                      <v-select
                        ref="groupCodeId"
                        v-model="employee.groupCodeId"
                        :rules="groupIdRules"
                        :items="listGroup"
                        box
                        label="Group"
                    /></v-flex>
                    <v-flex x4 style="text-align:center;margin:auto;">
                      <v-btn
                        color="#088e7d"
                        class="white--text"
                        @click="handleBtnAddClick"
                      >Add
                      </v-btn>
                    </v-flex>
                  </v-layout>
                  <v-layout>
                    <v-flex md12>
                      <v-data-table
                        :headers="headers"
                        :items="listAcctPermissFeatureButton"
                      >
                        <template slot="items" slot-scope="props">
                          <tr>
                            <td>{{ props.item.featureName }}</td>
                            <td>
                              <v-btn
                                icon
                                class="vbutton_icon"
                                @click="handleBtnDelClick(props.item)"
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
            </v-card-text>
            <v-card-actions>
              <v-spacer />
              <v-btn
                color="#088e7d"
                class="white--text"
                @click="handleBtnCancelClick"
              >
                {{ $t('button.cancel') }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-container>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex';
export default {
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
      employee: {
        id: null,
        accountId: '',
        featureCodeId: null,
        buttonCodeId: null,
        featureName: null,
        groupCodeId: null
      },
      listAcctPermissFeatureButton: [],
      listGroup: [],
      headers: [{ text: 'GroupName', sortable: false, value: 'featureName' }],
      accountIdRules: [v => !!v || 'Account ID field is required'],
      groupIdRules: [v => !!v || 'Group ID field is required']
    };
  },

  watch: {
    show(value) {
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
        this.listGroup = [];
        this.employee.groupCodeId = null;
      } else {
        this.loadGroup();
        this.loadAcctPermissionGroupFeature();
      }
    }
  },

  created() {
    this.dialog = this.show;
    this.employee.accountId = this.params.data;
  },

  methods: {
    ...mapActions('accountmanagement_group', [
      'getAcctPermissionGroupFeature',
      'addAcctPermissionGroupFeature',
      'delAcctPermissionGroupFeature',
      'getAllGroup'
    ]),
    emitShownState(flag) {
      this.$emit('shown', flag);
    },
    async handleBtnAddClick() {
      this.employee.accountId = this.params.data;
      if (this.employee.accountId == null || this.employee.accountId == '') {
        this.$refs.accountId.focus();
      } else if (
        this.employee.groupCodeId == null ||
        this.employee.groupCodeId == ''
      ) {
        this.$refs.groupCodeId.focus();
      } else {
        let result = await this.addAcctPermissionGroupFeature(this.employee);
        if (result.success) {
          this.loadAcctPermissionGroupFeature();
        } else {
          this.$message.error(result.message);
        }
      }
    },
    async handleBtnDelClick(item) {
      this.employee = {
        id: item.id,
        accountId: item.accountId,
        featureCodeId: item.featureCodeId,
        groupCodeId: item.groupCodeId
      };
      let result = await this.delAcctPermissionGroupFeature(this.employee);
      if (result.success) {
        this.loadAcctPermissionGroupFeature();
      } else {
        this.$message.error(result.message);
      }
    },
    handleBtnCancelClick() {
      this.emitShownState(false);
    },
    async loadGroup() {
      const result = await this.getAllGroup();
      if (result.data != null) {
        var listGroupTemp = [];
        listGroupTemp.push({
          text: '',
          value: null
        });
        result.data.forEach(element => {
          listGroupTemp.push({
            text: element.name,
            value: element.codeId
          });
        });
        this.listGroup = listGroupTemp;
        this.employee.groupCodeId = {
          text: '',
          value: null
        };
      } else {
        this.listGroup = [];
      }
    },
    async loadAcctPermissionGroupFeature() {
      this.employee.accountId = this.params.data;
      let result = await this.getAcctPermissionGroupFeature(this.employee);
      if (result.data != '') {
        this.listAcctPermissFeatureButton = result.data;
      } else {
        this.listAcctPermissFeatureButton = [];
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
