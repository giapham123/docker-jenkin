<template>
  <v-dialog v-model="dialog" width="30%">
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
                Check score range customer
              </v-card-title>
              <v-container grid-list-md>
                <v-layout align-center justify-center row fill-height>
                  <v-flex xs8>
                    <v-text-field
                      v-model="customer.mobileOrIdno"
                      label="Mobile or IdNo"
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
                <v-layout wrap row>
                  <v-flex xs4>
                    <v-text-field
                      v-model="customer.importCode"
                      label="Import Code"
                      readonly
                    />
                  </v-flex>
                  <v-flex xs4>
                    <v-text-field
                      v-model="customer.scoreRange"
                      label="Score Range"
                      readonly
                    />
                  </v-flex>
                  <v-flex xs4>
                    <v-text-field
                      v-model="customer.tsPic"
                      label="Ts Pic"
                      readonly
                    />
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
        mobileOrIdno: '',
        importCode: '',
        scoreRange: '',
        tsPic: ''
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
          mobileOrIdno: '',
          importCode: '',
          scoreRange: '',
          tsPic: ''
        };
      }
    }
  },

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('dashboard', ['getScoreRangeCustomer']),
    emitShownState(flag) {
      this.$emit('shown', flag);
    },

    handleBtnCancelClick() {
      this.emitShownState(false);
    },

    async searchData() {
      this.loading = true;
      const result = await this.getScoreRangeCustomer(
        this.customer.mobileOrIdno
      );
      if (result != null) {
        this.customer.scoreRange = result.score == null ? '' : result.score;
        this.customer.importCode = result.import_Code;
        this.customer.tsPic = result.ts_Pic == null ? '' : result.ts_Pic;
      } else {
        this.customer = {
          importCode: '',
          scoreRange: '',
          tsPic: ''
        };
        this.$message.error('Could not find data by this mobile number or id.');
      }
      this.loading = false;
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
