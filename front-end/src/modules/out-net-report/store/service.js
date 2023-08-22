// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class OutNetReportService extends Service {
  async getOutNetReportData(param) {
    return await this.post('api/out-net-report/get-out-net-report', param);
  }
  async export() {
    return await this.get('api/out-net-report/export');
  }
}

export default OutNetReportService;
