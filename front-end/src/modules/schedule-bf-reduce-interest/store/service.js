// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class ScheduleBfReduceInterestService extends Service {
  async getReduceInterest(param) {
    return await this.post(
      'api/schedule-bf-reduce-interest/get-reduce-interest',
      param
    );
  }

  async getRepayment(param) {
    return await this.post(
      'api/schedule-bf-reduce-interest/get-repaymnet',
      param
    );
  }

  async exportExcelFile(param) {
    return await this.post(
      'api/schedule-bf-reduce-interest/exportExcel',
      param
    );
  }
}

export default ScheduleBfReduceInterestService;
