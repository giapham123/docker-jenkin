// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class WriteOffService extends Service {
  async getDataWriteOff(param) {
    return await this.post('api/writeoff/getDataWriteOff', param);
  }

  async exportDataWriteOff(param) {
    return await this.post('api/writeoff/exportExcelWriteOff', param);
  }
}

export default WriteOffService;
