// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class AccoutingHisService extends Service {
  async getDataAccountingHis(param) {
    return await this.post('api/accountingHis/getAccoutingHis', param);
  }

  async exportExcelFile(param) {
    return await this.post('api/accountingHis/exportExcel', param);
  }
}

export default AccoutingHisService;
