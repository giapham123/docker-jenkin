<template>
  <v-container class="home-app-wrapper sale_main_ui" grid-list-md fluid>
    <v-expansion-panel :value="[true]" expand>
      <v-expansion-panel-content expand-icon>
        <template slot="header">
          <v-card class="delimiter_row_bottom">
            <v-card-title
              style="color:#02786B"
              class="delimiter_row_bottom title font-italic"
            >
              Upload File
            </v-card-title>
          </v-card>
        </template>
        <v-card>
          <v-subheader class="header sub-header-session">
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
                  :disabled="disableButtonBank"
                  small
                  color="#00695c"
                  class="white--text mt-3"
                  @click="Uploadfile"
                >
                  Upload
                </v-btn>
              </v-flex>
              <v-flex xs5>
                <input
                  ref="uploadFile"
                  type="file"
                  accept="application/pdf"
                  @change="enableButtonUploadBank"
                />
              </v-flex>
            </v-layout>
          </v-card-text>
          <v-subheader class="header mt-4 sub-header-session">
            Other Document
          </v-subheader>
          <v-card-text>
            <v-layout wrap row>
              <v-flex xs5>
                <v-select
                  v-model="other_template.letter"
                  :items="otherTemplates"
                  label="Document Name"
                  item-value="docCode"
                  item-text="docName"
                />
              </v-flex>
              <v-flex xs7>
                <v-btn
                  :disabled="disableButtonOther"
                  small
                  color="#00695c"
                  class="white--text mt-3"
                  @click="UploadfileOther"
                >
                  Upload
                </v-btn>
              </v-flex>
              <v-flex xs5>
                <v-text-field
                  v-model="other_template.content"
                  label="Content"
                />
              </v-flex>
              <v-flex xs7 />
              <v-flex xs5>
                <input
                  ref="uploadFileOther"
                  type="file"
                  accept="application/pdf"
                  @change="enableButtonUploadOther"
                />
              </v-flex>
              <v-flex xs7 />
            </v-layout>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>

    <v-expansion-panel :value="[false]" expand class="mt-3">
      <v-expansion-panel-content expand-icon>
        <template slot="header">
          <v-card>
            <v-card-title
              style="color:#02786B"
              class="delimiter_row_bottom title font-italic"
            >
              View Document
            </v-card-title>
          </v-card>
        </template>
        <v-card>
          <v-card-text class="no_padding">
            <v-layout wrap row>
              <v-flex xs12>
                <v-data-table
                  :headers="headers"
                  :items="table_data"
                  :rows-per-page-items="page_size"
                >
                  <template slot="items" slot-scope="props">
                    <tr>
                      <td>{{ props.item.id }}</td>
                      <td class="text-no-wrap">
                        {{ props.item.bankName }}
                      </td>
                      <td>{{ props.item.docName }}</td>
                      <td class="text-no-wrap">
                        {{ props.item.createdDate }}
                      </td>
                      <td class="text-no-wrap">{{ props.item.createdBy }}</td>
                      <td class="text-no-wrap">
                        <v-icon @click="viewFile(props.item.filename)">
                          cloud_download
                        </v-icon>
                      </td>
                    </tr>
                  </template>
                </v-data-table>
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
import { TABLE_PAGE_SIZES } from 'core/constants';
import { viewPDFFile } from 'core/util';

export default {
  data() {
    return {
      letter_template: {
        bank: null,
        letter: null
      },
      other_template: {
        content: '',
        letter: null
      },
      disableButtonOther: true,
      disableButtonBank: true,
      table_data: [],
      headers: [
        { text: 'NO', sortable: false, class: 'font-weight-bold' },
        { text: 'BANK', sortable: false, class: 'font-weight-bold' },
        { text: 'DOCUMENT NAME', sortable: false, class: 'font-weight-bold' },
        { text: 'DATE LAST TIME', sortable: false, class: 'font-weight-bold' },
        { text: 'USER UPLOAD', sortable: false, class: 'font-weight-bold' },
        { text: 'VIEW', sortable: false, class: 'font-weight-bold' }
      ],
      page_size: TABLE_PAGE_SIZES
    };
  },

  computed: {
    ...mapGetters('printTool', [
      'allTemplates',
      'bankTemplates',
      'otherTemplates'
    ]),
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
    otherTemplates: 'selectDefaultOther'
  },

  beforeMount() {
    this.selectDefaultBank();
    // this.selectDefaultOther();
  },

  created() {
    this.firstLoad();
  },

  methods: {
    ...mapActions('printTool', [
      'fetchAllTemplates',
      'uploadfiletoftp',
      'getAllHistories',
      'viewTemplateFile'
    ]),
    ...mapActions('global', ['setLoading']),

    selectDefaultBank() {
      if (!__isEmpty(this.bankTemplates)) {
        this.letter_template.bank = __head(this.bankTemplates).value;
      }
    },

    selectDefaultOther() {
      if (!__isEmpty(this.otherTemplates)) {
        this.other_template.letter = __head(this.otherTemplates).docCode;
      }
    },

    async firstLoad() {
      this.fetchListDocument();
      this.setLoading(true);
      await this.fetchAllTemplates();
      this.setLoading(false);
    },

    selectLetter() {
      if (this.letter_template.bank != null && !__isEmpty(this.letterTypes)) {
        this.letter_template.letter = __head(this.letterTypes).docCode;
      }
    },

    async Uploadfile() {
      this.setLoading(true);
      try {
        if (this.letter_template.bank == null) {
          this.$message.warning('Please select a document before upload');
          return;
        }
        if (this.$refs.uploadFile.files[0].name.indexOf('.pdf') == -1) {
          this.$message.error('Please choose a PDF file');
          return;
        }
        var file = this.$refs.uploadFile.files[0];
        let formData = new FormData();
        var docCode = this.letter_template.letter;
        formData.append('file', file, docCode);
        let result = await this.uploadfiletoftp({
          docCode: docCode,
          from_data: formData
        });
        if (!result.success) {
          this.$message.error(result.message);
        } else {
          this.$message.success('Push successful');
          this.$refs.uploadFile.value = '';
          this.disableButtonBank = true;
        }
        this.fetchListDocument();
      } catch (e) {
        this.$message.error(
          'Unknown error from browser. Please try again later'
        );
      } finally {
        this.setLoading(false);
      }
    },

    async UploadfileOther() {
      this.setLoading(true);
      try {
        if (this.other_template.letter == null) {
          this.$message.warning('Please select a document before upload');
          return;
        }
        if (this.$refs.uploadFileOther.files[0].name.indexOf('.pdf') == -1) {
          this.$message.error('Please choose a PDF file');
          return;
        }
        var file = this.$refs.uploadFileOther.files[0];
        let formData = new FormData();
        var docCode = this.other_template.letter;
        formData.append('file', file, docCode);
        formData.append('content', this.other_template.content);
        let result = await this.uploadfiletoftp({
          docCode: docCode,
          from_data: formData
        });
        if (!result.success) {
          this.$message.error(result.message);
        } else {
          this.$message.success('Push successful');
          this.$refs.uploadFileOther.value = '';
          this.disableButtonOther = true;
          this.other_template.content = '';
        }
        this.fetchListDocument();
      } catch (e) {
        this.$message.error(
          'Unknown error from browser. Please try again later'
        );
      } finally {
        this.setLoading(false);
      }
    },

    async enableButtonUploadOther() {
      this.disableButtonOther = false;
    },

    async enableButtonUploadBank() {
      this.disableButtonBank = false;
    },

    async fetchListDocument() {
      let result = await this.getAllHistories();
      if (result.success) {
        if (result.data == null) {
          result.data = [];
        }
        this.table_data = result.data;
      } else {
        this.$message.warning(result.message);
      }
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
</style>
