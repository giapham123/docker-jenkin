// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class CheckTerminationDailyRPTService extends Service {
  async getTerminationDailyReport(param) {
    return await this.post(
      'api/check-termination-daily-report/get-termination-daily-report',
      param
    );
  }

  async export() {
    return await this.get('api/check-termination-daily-report/export');
  }
}

export default CheckTerminationDailyRPTService;
