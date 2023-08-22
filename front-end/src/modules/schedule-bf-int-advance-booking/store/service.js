// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class ScheduleBfIntAdvanceBookingService extends Service {
  async getReduceInterest(param) {
    return await this.post(
      'api/schedule-bf-int-advance-booking/get-reduce-interest',
      param
    );
  }

  async getRepayment(param) {
    return await this.post(
      'api/schedule-bf-int-advance-booking/get-repaymnet',
      param
    );
  }

  async exportExcelFile(param) {
    return await this.post(
      'api/schedule-bf-int-advance-booking/exportExcel',
      param
    );
  }
}

export default ScheduleBfIntAdvanceBookingService;
