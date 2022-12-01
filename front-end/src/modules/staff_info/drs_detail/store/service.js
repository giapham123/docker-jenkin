import Service from 'core/service';
import {} from 'core/constants';

class DrsDetailStaffInfoService extends Service {
  getInfo(params) {
    return this.post('api/account/AccountInfoDrsDetail/getInfo', params);
  }

  getAccount(params) {
    return this.post('api/account/AccountInfoDrsDetail/getAccount', params);
  }

  insertInfo(params) {
    return this.post('api/account/AccountInfoDrsDetail/insertInfo', params);
  }

  updateInfo(params) {
    return this.put('api/account/AccountInfoDrsDetail/updateInfo', params);
  }
}
export default DrsDetailStaffInfoService;
