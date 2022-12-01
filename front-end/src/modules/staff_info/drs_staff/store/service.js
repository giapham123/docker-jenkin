import Service from 'core/service';
import {} from 'core/constants';

class DRSStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoDrs/getInfo', params);
  }
  getInfoAccount(params) {
    return this.post('api/account/AccountInfoDrs/getInfoAccount', params);
  }
  getInfo1(params) {
    return this.post('api/account/AccountInfoDrsDetail/getInfo', params);
  }
  insertInfo(params) {
    return this.post('api/account/AccountInfoDrs/insertInfo', params);
  }
  updateInfo(params) {
    return this.put('api/account/AccountInfoDrs/updateInfo', params);
  }
  deleteInfo(params) {
    return this.put('api/account/AccountInfoDrs/deleteInfo', params);
  }
  importDataDrs(params) {
    return this.post('api/account/AccountInfoDrs/importList', params);
  }
  updateDataDrs(params) {
    return this.put('api/account/AccountInfoDrs/updateList', params);
  }
}
export default DRSStaffInfoService;
