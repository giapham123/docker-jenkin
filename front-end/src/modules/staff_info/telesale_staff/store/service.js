import Service from 'core/service';
import {} from 'core/constants';

class DRSStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoTeleSale/getInfo', params);
  }
  insertInfo(params) {
    return this.post('api/account/AccountInfoTeleSale/insertInfo', params);
  }
  updateInfo(params) {
    return this.put('api/account/AccountInfoTeleSale/updateInfo', params);
  }
  deleteInfo(params) {
    return this.put('api/account/AccountInfoTeleSale/deleteInfo', params);
  }
  importDataDrs(params) {
    return this.post('api/account/AccountInfoTeleSale/importList', params);
  }
  updateDataDrs(params) {
    return this.put('api/account/AccountInfoTeleSale/updateList', params);
  }
}
export default DRSStaffInfoService;
