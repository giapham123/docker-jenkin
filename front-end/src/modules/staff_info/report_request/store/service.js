import Service from 'core/service';
import {} from 'core/constants';

class reportRequest extends Service {
  getAllDepartment() {
    return this.rest('api/department/getAll');
  }

  getAllRequestType() {
    return this.rest('api/branch/getAllRequestType');
  }

  getAllBranch() {
    return this.rest('api/branch/getAllBranch');
  }

  getAllGroupPermission() {
    return this.rest('api/group/getAll');
  }
  getAccountInfoInRequestSummary(params) {
    return this.post('api/report-request/getListRequestSummaryReport', params);
  }

  getRequestDetailByRequestId(params) {
    return this.post('api/acctinfo/sim/getRequestDetailByRequestId', params);
  }

  verifyRegisterResult(params) {
    return this.post('api/acctinfo/sim/verifyAccountRegister', params);
  }

  getListResetPassRequest(params) {
    return this.get('api/acctinfo/sim/getListResetPassRequest/' + params);
  }

  updVerifyResetPassResult(params) {
    return this.post('api/acctinfo/sim/verifyAccountResetPassword', params);
  }

  getListActiveInactiveRequest(params) {
    return this.get('api/acctinfo/sim/getListActiveInactiveRequest/' + params);
  }

  updVerifyActiveInactiveRequest(params) {
    return this.post('api/acctinfo/sim/verifyAccountActiveInactive', params);
  }

  verifyUpdateInfoResult(params) {
    return this.post('api/acctinfo/sim/verifyAccountUpdateInfo', params);
  }

  getGroupFeatureCurrentPermission(params) {
    return this.post(
      'api/acctinfo/sim/getGroupIdPermissionCurrentByAcct',
      params
    );
  }

  updVerifyUpdatePermissionRequest(params) {
    return this.post('api/acctinfo/sim/verifyUpdatePermission', params);
  }

  getListRemoveAccountRequest(params) {
    return this.get('api/acctinfo/sim/getListRemoveAccountRequest/' + params);
  }

  updVerifyremoveAccountResult(params) {
    return this.post('api/acctinfo/sim/verifyRemoveAccount', params);
  }
}

export default reportRequest;
