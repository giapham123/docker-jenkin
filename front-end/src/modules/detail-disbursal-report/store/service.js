// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class DetailDisbursalReport extends Service {
  async getDataDetailDisbursalReport(param) {
    return await this.post(
      'api/detail-disbursal-report/get-data-detail-disbursal-report',
      param
    );
  }

  async exportExcelFile(param) {
    return await this.post('api/detail-disbursal-report/exportExcel', param);
  }
}

export default DetailDisbursalReport;
