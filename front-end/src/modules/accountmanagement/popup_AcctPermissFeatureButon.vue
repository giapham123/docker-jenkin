<template>
  <v-dialog v-model="dialog" width="50%">
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
                      <!-- <v-select
                        :items="this.listFeature"
                        v-model="employee.featureCodeId"
                        item-text="name"
                        item-value="codeId"
                        box
                        label="Feature"
                        @change="selectFeature"
                    /> -->
                      <v-autocomplete
                        v-model="selectFeature.selected"
                        :items="selectFeature.items"
                        label="Feature"
                        @change="changeFeature"
                      >
                      </v-autocomplete>
                    </v-flex>
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
                            <td>{{ props.item.id }}</td>
                            <td>{{ props.item.featureName }}</td>
                            <td>{{ props.item.buttonCodeId }}</td>
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
                {{ $t('button.close') }}
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
        featureName: null
      },
      listAcctPermissFeatureButton: [],
      listFeature: [],
      headers: [
        { text: 'Id', sortable: false, value: 'id' },
        { text: 'FeatureName', sortable: false, value: 'featureName' },
        { text: 'ButtonCodeID', sortable: false, value: 'buttonCodeId' }
      ],
      accountIdRules: [v => !!v || 'Account ID field is required'],
      selectFeature: {
        items: [],
        selected: null
      }
    };
  },

  watch: {
    show(value) {
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
      } else {
        this.loadFeatureInfo();
        this.loadAcctPermissionFeatureButton();
      }
    }
  },

  created() {
    this.dialog = this.show;
    this.employee.accountId = this.params.data;
  },

  methods: {
    ...mapActions('accountmanagement', [
      'getFeatureInfo',
      'getAcctPermissionFeatureButton',
      'addAcctPermissionFeatureButton',
      'delAcctPermissionFeatureButton'
    ]),
    emitShownState(flag) {
      this.$emit('shown', flag);
    },
    // selectFeature(codeId) {
    //   this.employee.featureCodeId = codeId;
    // },

    changeFeature() {
      this.employee.featureCodeId = this.selectFeature.selected;
    },
    async handleBtnAddClick() {
      this.employee.accountId = this.params.data;
      if (this.employee.accountId == null || this.employee.accountId == '') {
        this.$refs.accountId.focus();
      } else {
        let result = await this.addAcctPermissionFeatureButton(this.employee);
        if (result.success) {
          this.loadAcctPermissionFeatureButton();
        } else {
          this.$message.error(result.message);
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
      // if (result.data != null) {
      //   this.listFeature = result.data;
      // } else {
      //   this.listFeature = [];
      // }

      if (result.data != null) {
        result.data.forEach(element => {
          this.selectFeature.items.push({
            value: element.codeId,
            text: element.name
          });
        });
      } else {
        this.selectFeature = [];
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
