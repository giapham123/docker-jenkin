<template>
  <v-container class="home-app-wrapper" grid-list-md fluid>
    <v-expansion-panel :value="[unread_count > 0]" expand>
      <v-expansion-panel-content expand-icon>
        <template slot="header">
          <v-card class="delimiter_row_bottom">
            <v-card-title
              style="color:#02786B"
              class="subheading session_block font-italic"
            >
              View Notifications
              <v-spacer />
              <p v-show="unread_count > 0" class="unread-count">
                {{ unread_count }}
              </p>
            </v-card-title>
          </v-card>
        </template>
        <v-card>
          <v-card-text class="no_padding">
            <v-data-table :headers="headers" :items="notifyHistories">
              <template slot="items" slot-scope="props">
                <tr @click="changeToRead(props.item.notifyId, props.item.read)">
                  <td align="center">
                    <div
                      :class="[
                        'unread-status',
                        props.item.read ? 'disable' : ''
                      ]"
                    />
                  </td>
                  <td>{{ props.item.$no }}</td>
                  <td>{{ props.item.docName }}</td>
                  <td>{{ props.item.createdDate }}</td>
                  <td class="text-no-wrap">{{ props.item.notifyContent }}</td>
                  <td>{{ props.item.createdBy }}</td>
                  <td>
                    <v-icon
                      :class="[
                        'view-file-icon',
                        props.item.filename ? '' : 'disable'
                      ]"
                      :key="props.item.notifyId"
                      @click="viewFile(props.item.filename)"
                    >
                      cloud_download
                    </v-icon>
                  </td>
                </tr>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>
    <v-expansion-panel :value="[true]" expand class="mt-3">
      <v-expansion-panel-content expand-icon>
        <template slot="header">
          <v-card class="delimiter_row_bottom">
            <v-card-title
              style="color:#02786B"
              class="subheading session_block font-italic"
            >
              Print Template
            </v-card-title>
          </v-card>
        </template>
        <v-card>
          <v-subheader class="header sub-header-session">
            Document of Loan
          </v-subheader>
          <v-card-text>
            <v-layout wrap row>
              <v-flex xs5>
                <v-text-field v-model="loan_template.appId" label="AppID" />
              </v-flex>
              <v-flex xs7 />
              <v-flex xs5>
                <v-select
                  v-model="loan_template.document"
                  :items="loanTemplates"
                  label="Document Name"
                  item-value="docCode"
                  item-text="docName"
                />
              </v-flex>
              <v-flex xs7>
                <v-btn
                  small
                  color="#00695c"
                  class="white--text mt-3"
                  @click="printLoanTemplate"
                >
                  View
                </v-btn>
              </v-flex>
            </v-layout>
          </v-card-text>
          <v-subheader class="header mt-4 sub-header-session">
            Authorized Letter
          </v-subheader>
          <v-card-text>
            <v-layout wrap row>
              <v-flex xs5>
                <v-autocomplete
                  v-model="letter_template.bank"
                  :items="bankTemplates"
                  label="Bank"
                />
              </v-flex>
              <v-flex xs7 />
              <v-flex xs5>
                <v-select
                  v-model="letter_template.letter"
                  :items="letterTypes"
                  label="Letter Type"
                  item-value="docCode"
                  item-text="docName"
                />
              </v-flex>
              <v-flex xs7>
                <v-btn
                  small
                  color="#00695c"
                  class="white--text mt-3"
                  @click="printLetterTemplate"
                >
                  View
                </v-btn>
              </v-flex>
            </v-layout>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-container>
</template>

<script>
import __isEmpty from 'lodash/isEmpty';
import __head from 'lodash/head';
import __isString from 'lodash/isString';
import __last from 'lodash/last';
import { mapActions, mapGetters } from 'vuex';
import { viewPDFFile } from 'core/util';
import { setInterval, clearInterval } from 'timers';

export default {
  data() {
    return {
      periodical_job_id: null,

      letter_template: {
        bank: null,
        letter: null
      },

      loan_template: {
        appId: '',
        document: null
      },

      notifyHistories: [],
      headers: [
        {
          text: '',
          sortable: false
        },
        {
          text: 'NO',
          sortable: false,
          class: 'font-weight-bold'
        },
        {
          text: 'DOCUMENT NAME',
          sortable: false,
          class: 'font-weight-bold'
        },
        {
          text: 'DATE LAST TIME',
          sortable: false,
          class: 'font-weight-bold'
        },
        {
          text: 'MESSAGE',
          sortable: false,
          class: 'font-weight-bold'
        },
        {
          text: 'USER UPLOAD',
          sortable: false,
          class: 'font-weight-bold'
        },
        {
          text: 'VIEW FILE',
          sortable: false,
          class: 'font-weight-bold'
        }
      ]
    };
  },

  computed: {
    ...mapGetters('printTool', [
      'isLoaded',
      'allTemplates',
      'bankTemplates',
      'loanTemplates'
    ]),

    unread_count() {
      return this.notifyHistories.filter(notify => !notify.read).length;
    },

    letterTypes() {
      if (this.letter_template.bank != null) {
        return [
          ...this.allTemplates.filter(
            template => template.bankCode == this.letter_template.bank
          )
        ];
      }

      return [];
    }
  },

  watch: {
    'letter_template.bank': {
      handler: 'selectLetter',
      deep: true
    },

    bankTemplates: 'selectDefaultBank',
    loanTemplates: 'selectDefaultDocument'
  },

  beforeMount() {
    this.selectDefaultBank();
    this.selectDefaultDocument();
    this.getNotification();
  },

  created() {
    this.firstLoad();

    // CREATE JOB TO FETCH NOTIFY DATA
    this.periodical_job_id = setInterval(
      () => this.getNotification(),
      2 * 60 * 1000
    );
  },

  beforeDestroy() {
    if (this.periodical_job_id != null) {
      clearInterval(this.periodical_job_id);
    }
  },

  methods: {
    ...mapActions('printTool', [
      'changeReadStatus',
      'fetchAllTemplates',
      'printTemplate',
      'retrieveNotifyHistories',
      'viewTemplateFile',
      'getUploadNotification'
    ]),
    ...mapActions('global', ['setLoading']),

    async firstLoad() {
      if (this.isLoaded) return;

      this.setLoading(true);
      await this.fetchAllTemplates();
      await this.getNotification();
      this.setLoading(false);
    },

    async getNotification() {
      this.setLoading(true);
      try {
        const response = await this.retrieveNotifyHistories();
        const {
          success = false,
          data = [],
          message = 'Can not retrieve data history of Notification'
        } = response || {};
        if (success) {
          this.notifyHistories = data.map((row, index) => {
            row.$no = index + 1;
            return row;
          });
        } else {
          this.$message.error(message);
        }
      } catch (e) {
        this.$message.error('Unknown error while page is loading');
      }
      this.setLoading(false);
    },

    selectDefaultBank() {
      if (!__isEmpty(this.bankTemplates)) {
        this.letter_template.bank = __head(this.bankTemplates).value;
      }
    },

    selectDefaultDocument() {
      if (!__isEmpty(this.loanTemplates)) {
        this.loan_template.document = __head(this.loanTemplates).docCode;
      }
    },

    selectLetter() {
      if (this.letter_template.bank != null && !__isEmpty(this.letterTypes)) {
        this.letter_template.letter = __head(this.letterTypes).docCode;
      }
    },

    printLoanTemplate() {
      if (
        this.loan_template.appId == null ||
        this.loan_template.appId.length < 5
      ) {
        this.$message.warning('Value of AppID require at least 5 characters');
        return;
      }

      if (this.loan_template.document == null) {
        this.$message.warning(
          'Please select a document before printing Loan template'
        );
        return;
      }

      this.handlerPrint({
        docCode: this.loan_template.document,
        requestParams: {
          appId: this.loan_template.appId
        }
      });
    },

    async printLetterTemplate() {
      this.handlerPrint({
        docCode: this.letter_template.letter
      });
    },

    async handlerPrint(params) {
      this.setLoading(true);
      const response = await this.printTemplate(params);
      this.setLoading(false);
      const { data, success, message } = response;
      if (!success) {
        this.$message.error(message);
        return;
      }
      if (!__isString(data)) {
        this.$message.error('Exported structrue file is wrong');
        return;
      }

      viewPDFFile(data);
    },

    async viewFile(filename) {
      if (!__isString(filename)) return;
      this.setLoading(true);
      try {
        const partName = filename.split('.');
        if (partName.length != 2) {
          this.$message.warning(`The filename ${filename} is invalid`);
          return;
        }

        const response = await this.viewTemplateFile({
          filename: __head(partName),
          extension: __last(partName)
        });
        const { success, data, message } = response;
        if (success) {
          viewPDFFile(data);
        } else {
          this.$message.error(message);
        }
      } catch (e) {
        this.$message.error(
          'Unknown error from browser. Please try again later'
        );
      } finally {
        this.setLoading(false);
      }
    },

    async changeToRead(notifyId, isRead) {
      this.setLoading(true);
      try {
        if (isRead) return;
        await this.changeReadStatus(notifyId);

        this.getNotification();
      } catch (e) {
        this.$message.error(
          'Unknown error from browser. Please try again later'
        );
      } finally {
        this.setLoading(false);
      }
    }
  }
};
</script>

<style scoped>
.sub-header-session {
  color: #00695c !important;
  caret-color: #00695c !important;
  background-color: #f1f1f1;
}

.view-file-icon {
  cursor: pointer;
  user-select: none;
}

.view-file-icon:hover {
  color: #003932 !important;
}

.view-file-icon.disable {
  color: #999 !important;
  cursor: none;
}

.unread-count {
  color: white;
  font-style: normal;
  margin: 0;
  width: 50px;
  height: 20px;
  border-radius: 10px;
  background-color: red;
  text-align: center;
  font-size: 14px;
}

.unread-status {
  background-color: red;
  margin: 0;
  width: 10px;
  height: 10px;
  border-radius: 5px;
}

.unread-status.disable {
  background-color: #cccccc;
}
</style>
