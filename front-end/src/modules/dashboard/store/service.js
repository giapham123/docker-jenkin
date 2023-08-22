// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class DashboardService extends Service {
  getCustomerStatus(params) {
    return this.rest('api/checkcust/getStatus/' + params);
  }
  getAllBlackListPartner() {
    return this.rest('api/checkcust/getAllBlackListPartner');
  }
  getScoreRangeCustomer(params) {
    return this.rest('api/checkcust/getTeleSaleLeads/' + params);
  }
  getCustomerFileScan(params) {
    return this.rest('api/checkcust/getCustomerFileScan/' + params);
  }
  viewDocumentFile(params) {
    return this.rest('api/checkcust/viewDocument/' + params);
  }

  getSummarySystem() {
    return this.get('/api/dbt-dashboard/get-summary-status');
  }

  getSummaryStatistic() {
    return this.get('/api/dbt-dashboard/get-summary-statistic');
  }

  getDashboardMaker(params) {
    return this.rest('/api/dbt-dashboard/get-maker/' + params);
  }

  getImportStatus() {
    return this.get('/api/dbt-dashboard/getImportStatus');
  }
}

export default DashboardService;
