import Service from 'core/service';
import {} from 'core/constants';

class AccountManageService extends Service {
  getAllDepartment() {
    return this.rest('api/department/getAll');
  }
  getAllGroupPermission() {
    return this.rest('api/group/getAll');
  }
  getAccountInfoSearch(params) {
    return this.post('api/acctinfo/getAccountInfoSearch', params);
  }
  insUserAccount(params) {
    return this.post('api/acctinfo/insAcctInfo', params);
  }
  delUserAccount(params) {
    return this.post('api/acctinfo/delAcctInfo', params);
  }
  resetPassAcctInfo(params) {
    return this.post('api/acctinfo/resetPass', params);
  }
  getFeatureInfo() {
    return this.rest('api/featureinfo/getAll');
  }
  async getAcctPermissionFeatureButton(params) {
    let url = 'api/acctpermissionfeaturebutton/getAcctPermissionFeatureButton';
    return this.readRestResponse(await this.post(url, params));
  }
  async addAcctPermissionFeatureButton(params) {
    let url = 'api/acctpermissionfeaturebutton/insAcctPermissionFeatureButton';
    return this.readRestResponse(await this.post(url, params));
  }
  async delAcctPermissionFeatureButton(params) {
    let url = 'api/acctpermissionfeaturebutton/delAcctPermissionFeatureButton';
    return this.readRestResponse(await this.post(url, params));
  }
  readRestResponse(resp) {
    switch (resp.status) {
      case 102:
        return {
          success: false,
          data: null,
          message: 'Can not connect to Server API.',
          http_status: 102
        };

      case 200:
        return resp;
      case 304:
        return {
          success: false,
          data: null,
          message: 'Server API do not accept CORS from this address.',
          http_status: 304
        };

      case 401:
        return {
          success: false,
          data: null,
          message: 'You do not have permision on API.',
          http_status: 401
        };

      case 403:
        return {
          success: false,
          data: null,
          message: 'Must be authenticate befor accessing this API.',
          http_status: 403
        };

      case 404:
        return {
          success: false,
          data: null,
          message: 'The request URL do not exist on server API.',
          http_status: 404
        };

      case 500:
        return {
          success: false,
          data: null,
          message: 'Has an error for this request. Refer admin to resolve.',
          http_status: 500
        };
    }
  }
}

export default AccountManageService;
