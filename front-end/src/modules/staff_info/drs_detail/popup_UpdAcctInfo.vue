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
                >
                  Direct Sale Detail Staff Edit Form
                </v-card-title>
                <v-container class="home-app-wrapper" grid-list-md fluid>
                  <v-card>
                    <v-card-text>
                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-text-field
                            v-model="staff.accountId"
                            :disabled="true"
                            label="DRS code"
                          />
                        </v-flex>
                      </v-layout>

                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-text-field
                            v-model="staff.fullName"
                            :disabled="true"
                            label="Name"
                          />
                        </v-flex>
                      </v-layout>

                      <v-layout row wrap>
                        <v-flex xs12>
                          <date-picker
                            v-model="staff.birthday"
                            label="Birthday"
                            icon="event"
                            readonly
                          ></date-picker>
                        </v-flex>
                      </v-layout>

                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-select
                            v-model="staff.gender"
                            :items="listGender"
                            label="Gender"
                          />
                        </v-flex>
                      </v-layout>
                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-text-field v-model="staff.idNo" label="ID No" />
                        </v-flex>
                      </v-layout>
                      <v-layout row wrap>
                        <v-flex xs12>
                          <date-picker
                            v-model="staff.idDate"
                            label="ID date"
                            icon="event"
                            readonly
                          ></date-picker>
                        </v-flex>
                      </v-layout>
                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-text-field
                            v-model="staff.placeId"
                            label="Place of ID"
                          />
                        </v-flex>
                      </v-layout>

                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-text-field
                            v-model="staff.permanentAdd"
                            label="Permenant Address"
                          />
                        </v-flex>
                      </v-layout>

                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-text-field
                            v-model="staff.currentAdd"
                            label="Current Address"
                          />
                        </v-flex>
                      </v-layout>

                      <v-layout row wrap>
                        <v-flex xs12>
                          <v-text-field
                            v-model="staff.taxCode"
                            label="Tax code"
                          />
                        </v-flex>
                      </v-layout>
                    </v-card-text>
                  </v-card>
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
      staff: {},
      listGender: ['Female', 'Male']
    };
  },

  watch: {
    show(value) {
      this.dialog = value;
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
    ...mapActions('drs_detail', ['updateInfo']),
    formatDate(date) {
      if (!date) return null;
      const [year, month, day] = date.split('-');
      return `${month}/${day}/${year}`;
    },

    emitShownState(flag) {
      this.$emit('update:show', flag);
    },

    handleBtnUpdateClick() {
      this.updateDetailInfo();
    },

    handleBtnCancelClick() {
      this.emitShownState(false);
    },

    async updateDetailInfo() {
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
