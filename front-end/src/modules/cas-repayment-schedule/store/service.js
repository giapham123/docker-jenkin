// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class CasRepaymentScheduleService extends Service {
  async getDataCasRepayment(param) {
    return await this.post('api/casRepaymentSchedule/getDataCasRepayment', param);
  }
  async export(param)  {
    return await this.post('api/casRepaymentSchedule/export', param);
  }
}

export default CasRepaymentScheduleService;
