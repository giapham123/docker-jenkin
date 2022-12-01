import Service from 'core/service';
import {} from 'core/constants';

class requestTicket extends Service {
  getAllDepartment() {
    return this.rest('api/department/getAll');
  }

  getAllBranch() {
    return this.rest('api/branch/getAllBranch');
  }

  getAllGroupPermission() {
    return this.rest('api/group/getAll');
  }
  getAccountInfoSearchRequestTicket(params) {
    return this.post(
      'api/acctinfo/sim/getAccountInfoSearchRequestTicket',
      params
    );
  }

  getListPermissionGroupFeatureByApp(params) {
    return this.post('api/acctinfo/sim/getGroupFeaturePermision', params);
  }

  getAppByDepartmentId(params) {
    return this.rest('api/acctinfo/sim/getApplicationByDepartment/' + params);
  }
  createAccountInfoDetail(params) {
    return this.post('api/acctinfo/sim/createRequestRegisterAccount', params);
  }

  createRequestUpdateAccountInfo(params) {
    return this.post('api/acctinfo/sim/createRequestUpdateInfoAccount', params);
  }

  getInfoAccountInfoDetail(params) {
    return this.post('api/acctinfo/sim/getAccountInfoDetail', params);
  }

  getGroupFeatureCurrentPermission(params) {
    return this.post(
      'api/acctinfo/sim/getGroupIdPermissionCurrentByAcct',
      params
    );
  }

  createRequestUpdatePermission(params) {
    return this.post(
      'api/acctinfo/sim/createRequestUpdatePermissionAccount',
      params
    );
  }

  createRequestResetPassword(params) {
    return this.post('api/acctinfo/sim/createRequestResetPassword', params);
  }

  createRequestActiveInactive(params) {
    return this.post('api/acctinfo/sim/createRequestActiveInactive', params);
  }
  createRequestRemoveAccount(params) {
    return this.post('api/acctinfo/sim/createRequestRemoveAccount', params);
  }

  getAccountInfoRequestExist(params) {
    return this.get('api/acctinfo/sim/getAccountRequestExist/' + params);
  }
}

export default requestTicket;
