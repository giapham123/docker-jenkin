import Service from 'core/service';
import {} from 'core/constants';

class DRSStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoSalesOperation/getInfo', params);
  }
  insertInfo(params) {
    return this.post(
      'api/account/AccountInfoSalesOperation/insertInfo',
      params
    );
  }
  updateInfo(params) {
    return this.put('api/account/AccountInfoSalesOperation/updateInfo', params);
  }
  deleteInfo(params) {
    return this.put('api/account/AccountInfoSalesOperation/deleteInfo', params);
  }
  importDataDrs(params) {
    return this.post(
      'api/account/AccountInfoSalesOperation/importList',
      params
    );
  }
  updateDataDrs(params) {
    return this.put('api/account/AccountInfoSalesOperation/updateList', params);
  }
}
export default DRSStaffInfoService;
