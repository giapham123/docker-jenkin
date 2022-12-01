<template>
  <v-dialog v-model="dialog" width="50%">
    <v-card
      style=" background-color: #FFFFFF; height: 100%"
      class="overflow_scroll_vertical"
    >
      <v-card class="home-app-wrapper popup max_content">
        <v-container class="no_padding max_content" fluid grid-list-md>
          <v-layout class="header white--text no_margin">
            <v-flex md12>
              <v-card-title class="white--text title">
                Import file excel for updating information
              </v-card-title>
            </v-flex>
          </v-layout>
          <v-container grid-list-md fluid>
            <v-card>
              <v-layout row wrap justify-center style="margin:5px">
                <v-flex sm12 md4>
                  <p>
                    <input ref="attachment" type="file" accept=".xls, .xlsx" />
                  </p>
                </v-flex>
                <v-flex sm12 md4>
                  <v-btn
                    color="#088e7d"
                    class="white--text"
                    style="margin-top: 0px;"
                    @click="importDataListDrs"
                  >
                    Upload File
                  </v-btn>
                </v-flex>
                <v-flex sm12 md4>
                  <v-btn
                    color="#088e7d"
                    class="white--text"
                    style="margin-top: 0px;"
                    @click="handleBtnExportClick"
                  >
                    Export Excel
                  </v-btn>
                </v-flex>
              </v-layout>
              <v-layout row wrap justify-center>
                <v-flex xs4 style="text-align:center;">
                  <v-text-field v-model="txtFile" label="Import Result" />
                </v-flex>
              </v-layout>
            </v-card>
            <v-card-actions>
              <v-layout row wrap justify-center style="margin:5px">
                <v-btn
                  :disabled="disableAdd"
                  color="#088e7d"
                  class="white--text"
                  @click="handleBtnAddClick"
                >
                  Update
                </v-btn>
                <v-btn
                  color="#088e7d"
                  class="white--text"
                  @click="handleBtnCancelClick"
                >
                  Cancel
                </v-btn>
              </v-layout>
            </v-card-actions>
          </v-container>
        </v-container>
      </v-card>
    </v-card>
  </v-dialog>
</template>

<script>
import Exporter from 'modules/common/JsonExporter.js';
import Importer from 'modules/common/JsonImporter';
import _ from 'lodash';
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
      dialog: false,
      disableAdd: true,
      txtFile: '',
      listDataCheck: [],
      listDataImp: []
    };
  },

  watch: {
    show(value) {
      this.dialog = value;
    },
    dialog() {
      if (!this.dialog) {
        this.emitShownState(false);
        this.disableAdd = true;
        this.txtFile = '';
      }
    }
  },

  created() {
    this.dialog = this.show;
  },

  methods: {
    ...mapActions('global', ['setLoading']),
    ...mapActions('branch_network_staff', ['updateData']),

    emitShownState(flag) {
      this.$emit('shown', flag);
    },

    async handleBtnAddClick() {
      let resultImport = await this.updateData(this.listDataImp);
      if (resultImport.success) {
        this.$message.success(resultImport.message);
        this.emitShownState(false);
      } else {
        this.$message.error(resultImport.message);
      }
    },

    handleBtnExportClick() {
      this.export_loading = true;
      let exporter = new Exporter('bnwList');
      exporter.setConfigurations({
        REPLACE_UNKNOWN_TO_EMPTY: true,
        DECORATE_BORDER: true,
        DECORATE_HEADER_COLOR: '#1f4e78'
      });
      let headers = {
        fullName: {
          text: 'FULLNAME',
          type: 'String',
          width: 80
        },
        accountId: {
          text: 'ACCOUNTID',
          type: 'String',
          width: 80
        },
        mafcCode: {
          text: 'MAFCCODE',
          type: 'String',
          width: 80
        },
        email: {
          text: 'EMAIL',
          type: 'String',
          width: 80
        },
        phone: {
          text: 'PHONE',
          type: 'String',
          width: 80
        },
        mobile: {
          text: 'MOBILE',
          type: 'String',
          width: 80
        },
        level: {
          text: 'LEVEL',
          type: 'String',
          width: 80
        },
        positionCompanyId: {
          text: 'POSITIONCOMPANYID',
          type: 'String',
          width: 80
        },
        isDeleted: {
          text: 'ISDELETED',
          type: 'String',
          width: 80
        },
        joiningDate: {
          text: 'JOININGDATE',
          type: 'String',
          width: 80
        },
        endDate: {
          text: 'ENDDATE',
          type: 'String',
          width: 80
        },
        status: {
          text: 'STATUS',
          type: 'String',
          width: 80
        },
        branchId: {
          text: 'BRANCHID',
          type: 'String',
          width: 80
        },
        departmentId: {
          text: 'DEPARTMENTID',
          type: 'String',
          width: 80
        },
        createDate: {
          text: 'CREATEDDATE',
          type: 'String',
          width: 80
        },
        modifiedDate: {
          text: 'MODIFIEDDATE',
          type: 'String',
          width: 80
        },
        createdBy: {
          text: 'CREATEDBY',
          type: 'String',
          width: 80
        },
        modifiedBy: {
          text: 'MODIFIEDBY',
          type: 'String',
          width: 80
        },
        deletedDate: {
          text: 'DELETEDDATE',
          type: 'String',
          width: 80
        },
        deletedBy: {
          text: 'DELETEDBY',
          type: 'String',
          width: 80
        },
        supervisorId: {
          text: 'SUPERVISORID',
          type: 'String',
          width: 80
        },
        isAdmin: {
          text: 'ISADMIN',
          type: 'String',
          width: 80
        },
        staffType: {
          text: 'STAFFTYPE',
          type: 'String',
          width: 80
        },
        group: {
          text: 'GROUP',
          type: 'String',
          width: 80
        },
        team: {
          text: 'TEAM',
          type: 'String',
          width: 80
        },
        area: {
          text: 'AREA',
          type: 'String',
          width: 80
        },
        personalId: {
          text: 'PERSONALID',
          type: 'String',
          width: 80
        },
        note: {
          text: 'NOTE',
          type: 'String',
          width: 80
        }
      };

      exporter.addSheet(headers, this.listDataCheck, 'Sheet0');
      let result = exporter.exportExcel();

      if (result.success) {
        this.$message.success(result.message);
      } else {
        this.$message.error(result.message);
      }
      this.export_loading = false;
    },

    async importDataListDrs() {
      this.disableAdd = true;
      this.setLoading(true);
      this.txtFile = '';
      this.listDataCheck = [];
      this.listDataImp = [];
      var impFile = true;
      let file = this.$refs.attachment.files[0];
      if (file == undefined) {
        this.$message.error('Please Choose File');
        this.setLoading(false);
        return;
      }
      this.$message.warning('read File');
      let result = await Importer.readXLSX(file, ['Sheet0']);

      var dataImport = Importer.correctJsonPropertyName(result.data['Sheet0'], {
        accountId: { name: 'ACCOUNTID', type: 'String' },
        mafcCode: { name: 'MAFCCODE', type: 'String' },
        fullName: { name: 'FULLNAME', type: 'String' },
        email: { name: 'EMAIL', type: 'String' },
        phone: { name: 'PHONE', type: 'String' },
        mobile: { name: 'MOBILE', type: 'String' },
        isAdmin: { name: 'ISADMIN', type: 'Integer' },
        joiningDate: { name: 'JOININGDATE', type: 'String' },
        endDate: { name: 'ENDDATE', type: 'String' },
        level: { name: 'LEVEL', type: 'String' },
        isDeleted: { name: 'ISDELETED', type: 'String' },
        departmentId: { name: 'DEPARTMENTID', type: 'String' },
        branchId: { name: 'BRANCHID', type: 'Integer' },
        supervisorId: { name: 'SUPERVISORID', type: 'String' },
        staffType: { name: 'STAFFTYPE', type: 'String' },
        group: { name: 'GROUP', type: 'String' },
        team: { name: 'TEAM', type: 'String' },
        area: { name: 'AREA', type: 'String' },
        status: { name: 'STATUS', type: 'String' },
        positionCompanyId: { name: 'POSITIONCOMPANYID', type: 'String' },
        personalId: { name: 'PERSONALID', type: 'String' }
      });
      var dataCheck = {
        accountId: { name: 'ACCOUNTID', type: 'String' },
        mafcCode: { name: 'MAFCCODE', type: 'String' },
        fullName: { name: 'FULLNAME', type: 'String' },
        email: { name: 'EMAIL', type: 'String' },
        phone: { name: 'PHONE', type: 'String' },
        mobile: { name: 'MOBILE', type: 'String' },
        isAdmin: { name: 'ISADMIN', type: 'Integer' },
        departmentId: { name: 'DEPARTMENTID', type: 'String' },
        branchId: { name: 'BRANCHID', type: 'Integer' },
        level: { name: 'LEVEL', type: 'Integer' },
        supervisorId: { name: 'SUPERVISORID', type: 'String' },
        staffType: { name: 'STAFFTYPE', type: 'String' },
        group: { name: 'GROUP', type: 'String' },
        team: { name: 'TEAM', type: 'String' },
        area: { name: 'AREA', type: 'String' },
        status: { name: 'STATUS', type: 'String' },
        positionCompanyId: { name: 'POSITIONCOMPANYID', type: 'String' },
        personalId: { name: 'PERSONALID', type: 'String' },
        note: { name: 'NOTE', type: 'String' }
      };

      if (!dataImport.success) {
        this.$message.warning(dataImport.message);
        this.setLoading(false);
        return;
      }
      if (dataImport.data != null && dataImport.data.length > 0) {
        dataImport.data.forEach(element => {
          dataCheck = {};
          var strNote = '';

          if (element.isAdmin == '' || element.isAdmin == null) {
            element.isAdmin = '0';
          }
          if (element.positionCompanyId.length > 5) {
            strNote = strNote + ', POSITION not more 5 letter';
            impFile = false;
          }
          if (
            element.joiningDate != '' &&
            element.joiningDate != null &&
            !_.isDate(new Date(element.joiningDate, 'MM/dd/yyyy'))
          ) {
            strNote = strNote + ', joiningDate is not Valid';
            impFile = false;
          }
          if (
            element.endDate != '' &&
            element.endDate != null &&
            !_.isDate(new Date(element.endDate, 'MM/dd/yyyy'))
          ) {
            strNote = strNote + ', endDate is not Valid';
            impFile = false;
          }

          dataCheck.accountId = element.accountId;
          dataCheck.email = element.email;
          dataCheck.phone = element.phone;
          dataCheck.mobile = element.mobile;
          dataCheck.isAdmin = element.isAdmin;
          dataCheck.departmentId = element.departmentId;
          dataCheck.level = element.level;
          dataCheck.supervisorId = element.supervisorId;
          dataCheck.branchId = element.branchId;
          dataCheck.staffType = element.staffType;
          dataCheck.group = element.group;
          dataCheck.team = element.team;
          dataCheck.area = element.area;
          dataCheck.status = element.status;
          dataCheck.personalId = element.personalId;
          dataCheck.positionCompanyId = element.positionCompanyId;
          dataCheck.mafcCode = element.mafcCode;
          dataCheck.joiningDate = element.joiningDate;
          dataCheck.endDate = element.endDate;
          dataCheck.note = strNote;

          if (element.fullName == '') {
            delete element.fullName;
          }
          if (element.mafcCode == '') {
            delete element.mafcCode;
          }
          if (element.email == '') {
            delete element.email;
          }
          if (element.phone == '') {
            delete element.phone;
          }
          if (element.mobile == '') {
            delete element.mobile;
          }
          this.listDataCheck.push(dataCheck);
          this.listDataImp.push(element);
        });
      } else {
        this.$message.success(
          'you can not update when data in file import is null'
        );
      }
      if (impFile == true) {
        this.txtFile = 'file ok';
        this.disableAdd = false;
      } else {
        this.txtFile = 'file is NOT ok';
      }
      this.$message.success('update file successful!');
      this.$refs.attachment.value = null;
      this.setLoading(false);
    },

    handleBtnCancelClick() {
      this.emitShownState(false);
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
</style>
