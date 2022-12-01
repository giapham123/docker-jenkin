import Service from 'core/service';
import {} from 'core/constants';

class DRSStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoUnd/getInfo', params);
  }
  insertInfo(params) {
    return this.post('api/account/AccountInfoUnd/insertInfo', params);
  }
  updateInfo(params) {
    return this.put('api/account/AccountInfoUnd/updateInfo', params);
  }
  deleteInfo(params) {
    return this.put('api/account/AccountInfoUnd/deleteInfo', params);
  }
  importDataDrs(params) {
    return this.post('api/account/AccountInfoUnd/importList', params);
  }
  updateDataDrs(params) {
    return this.put('api/account/AccountInfoUnd/updateList', params);
  }
}
export default DRSStaffInfoService;
