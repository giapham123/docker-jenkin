// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class AccoutingService extends Service {
  async getDataAccounting(param) {
    return await this.post('api/accounting/getAmount', param);
  }

  async exportExcelFile(param) {
    return await this.post('api/accounting/exportExcel', param);
  }
}

export default AccoutingService;
