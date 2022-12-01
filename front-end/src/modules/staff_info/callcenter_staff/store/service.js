import Service from 'core/service';
import {} from 'core/constants';

class DRSStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoCallCenter/getInfo', params);
  }
  insertInfo(params) {
    return this.post('api/account/AccountInfoCallCenter/insertInfo', params);
  }
  updateInfo(params) {
    return this.put('api/account/AccountInfoCallCenter/updateInfo', params);
  }
  deleteInfo(params) {
    return this.put('api/account/AccountInfoCallCenter/deleteInfo', params);
  }
  importDataDrs(params) {
    return this.post('api/account/AccountInfoCallCenter/importList', params);
  }
  updateDataDrs(params) {
    return this.put('api/account/AccountInfoCallCenter/updateList', params);
  }
}
export default DRSStaffInfoService;
