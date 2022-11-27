<template>
  <v-dialog v-model="dialog" width="50%">
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
                Search customer file scan
              </v-card-title>
              <v-container grid-list-md>
                <v-layout align-center justify-center row fill-height>
                  <v-flex xs8>
                    <v-text-field
                      v-model="customer.idNo"
                      label="Search by id or name customer"
                    />
                  </v-flex>
                  <v-flex x4 style="text-align:center;margin:auto;">
                    <v-btn
                      :loading="loading"
                      color="#088e7d"
                      class="white--text"
                      @click="searchData"
                    >
                      <v-icon>send</v-icon>
                    </v-btn>
                  </v-flex>
                </v-layout>
                <v-data-table
                  :items="customer.customerFileScanList"
                  :search="search"
                  :rows-per-page-items="[5, 10]"
                >
                  <template slot="items" slot-scope="props">
                    <tr @click="openFileItem(props.item)">
                      <td>{{ props.item.documentFileName }}</td>
                    </tr>
                  </template>
                </v-data-table>
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
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex';
export default {
  props: {
    show: {
      type: Boolean,
      default: true
    }
  },

  data() {
    return {
      valid: true,
      dialog: false,
      customer: {
        idNo: '',
        customerFileScanList: []
      },
      loading: false
    };
  },

  watch: {
    show(value) {
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
        this.customer = {
          idNo: '',
          customerFileScanList: []
        };
      }
    }
  },

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('dashboard', ['getCustomerFileScan', 'viewDocumentFile']),
    emitShownState(flag) {
      this.$emit('shown', flag);
    },

    handleBtnCancelClick() {
      this.emitShownState(false);
    },
    async searchData() {
      this.loading = true;
      const result = await this.getCustomerFileScan(this.customer.idNo);
      if (result.data != null && result.data != '') {
        this.customer.customerFileScanList = result.data;
      } else {
        this.customer.filescan = '';
        this.$message.error(
          'Could not find the documnent name data by this id or name.'
        );
      }
      this.loading = false;
    },
    async openFileItem(chooseItem) {
      var fileName = chooseItem.documentFileName.substring(
        0,
        chooseItem.documentFileName.length - 4
      );
      //console.log('fileName: ' + fileName);
      const result = await this.viewDocumentFile(fileName);
      if (result != null && result != '') {
        let url = document.location.origin + '/./' + result.data;
        //console.log('url: ' + url);
        window.open(url);
      } else {
        this.$message.error('Could not find the documnent file of this link.');
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
</style>
