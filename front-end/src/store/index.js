import Vue from 'vue';
import Vuex from 'vuex';
import _ from 'lodash';

import global from 'modules/global/store';
import login from 'modules/login/store';
import register from 'modules/register/store';
import users from 'modules/users/store';
import forgotpassword from 'modules/forgotpassword/store';
import resetstatus from 'modules/reset-status/store';
import dashboard from 'modules/dashboard/store';
import printTool from 'modules/print-tool/store';
import accounting from 'modules/accounting/store';
import accountinghis from 'modules/accounting-his/store';
import writeoff from 'modules/writeoff-ext/store';
import waveoffamount from 'modules/wave-off-amount/store';
import uploadFile from 'modules/upload-file/store';
import rejectUploadFile from 'modules/reject-upload-file/store';
import rejectUploadGLSAPFile from 'modules/reject-upload-file-gl-sap/store';
import outNetReport from 'modules/out-net-report/store';
import checkTerDailyReport from 'modules/check-termination-daily-report/store';
// import closeSoldout from 'modules/close-soldout/store';

Vue.use(Vuex);

const STORE_MODULE_POSTFIX = '/store/index.js';
const requireModule = require.context('modules', true, /\.js$/);
const modules = {
  global,
  login,
  register,
  users,
  forgotpassword,
  resetstatus,
  dashboard,
  printTool,
  accounting,
  writeoff,
  accountinghis,
  waveoffamount,
  uploadFile,
  rejectUploadFile,
  rejectUploadGLSAPFile,
  outNetReport,
  checkTerDailyReport
};
requireModule.keys().forEach(filename => {
  if (!_.endsWith(filename, STORE_MODULE_POSTFIX)) return;

  let folderPath = _.replace(filename, STORE_MODULE_POSTFIX, '');
  if (folderPath == '') return;

  let moduleName = _.camelCase(
    _.replace(_.last(_.split(folderPath, '/')), '/', '')
  );
  modules[moduleName] =
    requireModule(filename).default || requireModule(filename);
});
export default new Vuex.Store({
  modules,
  strict: process.env.NODE_ENV !== 'production'
});
