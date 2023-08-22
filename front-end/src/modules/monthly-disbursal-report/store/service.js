// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class MonthlyDisbursalReport extends Service {
  async getDataMonthlyDisbursalReport(param) {
    return await this.post(
      'api/monthly-disbursal-report/get-data-monthly-disbursal-report',
      param
    );
  }

  async exportExcelFile(param) {
    return await this.post('api/monthly-disbursal-report/exportExcel', param);
  }
}

export default MonthlyDisbursalReport;
