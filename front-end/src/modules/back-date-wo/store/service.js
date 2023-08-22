// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class backDateWOService extends Service {
  async getDataBackDate(param) {
    return await this.get('api/back-date-wo/get-data-back-date', param);
  }

  async processPendingBackDate(param) {
    return await this.get(
      'api/back-date-wo/process-pending-back-date/' + param.backDateType,
      param
    );
  }

  async export() {
    return await this.get('api/back-date-wo/export');
  }
}

export default backDateWOService;
