import Service from 'core/service';
import {} from 'core/constants';

class DRSStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoThirdParty/getInfo', params);
  }
  insertInfo(params) {
    return this.post('api/account/AccountInfoThirdParty/insertInfo', params);
  }
  updateInfo(params) {
    return this.put('api/account/AccountInfoThirdParty/updateInfo', params);
  }
  deleteInfo(params) {
    return this.put('api/account/AccountInfoThirdParty/deleteInfo', params);
  }
  importDataDrs(params) {
    return this.post('api/account/AccountInfoThirdParty/importList', params);
  }
  updateDataDrs(params) {
    return this.put('api/account/AccountInfoThirdParty/updateList', params);
  }
}
export default DRSStaffInfoService;
