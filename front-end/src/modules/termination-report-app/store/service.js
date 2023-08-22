// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class TerminationReportApp extends Service {
  async getDataTerminationReportApp(param) {
    return await this.post('api/termination-report-app/getAmount', param);
  }

  async exportExcelFile(param) {
    return await this.post('api/termination-report-app/exportExcel', param);
  }
}

export default TerminationReportApp;
