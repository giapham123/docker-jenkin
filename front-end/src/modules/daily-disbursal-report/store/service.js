// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class DailyDisbursalReport extends Service {
  async getDataDailyDisbursalReport(param) {
    return await this.post(
      'api/daily-disbursal-report/get-data-daily-disbursal-report',
      param
    );
  }

  async exportExcelFile(param) {
    return await this.post('api/daily-disbursal-report/exportExcel', param);
  }
}

export default DailyDisbursalReport;
