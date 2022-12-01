import Service from 'core/service';
import {} from 'core/constants';

class DRSStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoBranchNetwork/getInfo', params);
  }
  insertInfo(params) {
    return this.post('api/account/AccountInfoBranchNetwork/insertInfo', params);
  }
  updateInfo(params) {
    return this.put('api/account/AccountInfoBranchNetwork/updateInfo', params);
  }
  deleteInfo(params) {
    return this.put('api/account/AccountInfoBranchNetwork/deleteInfo', params);
  }
  importDataDrs(params) {
    return this.post('api/account/AccountInfoBranchNetwork/importList', params);
  }
  updateDataDrs(params) {
    return this.put('api/account/AccountInfoBranchNetwork/updateList', params);
  }
}
export default DRSStaffInfoService;
