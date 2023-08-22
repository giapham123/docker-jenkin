// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class viewSapEntries extends Service {
  async getDataViewSapEntries(param) {
    return await this.post('api/view-sap-entries/get-data-view-sap', param);
  }
  async export() {
    return await this.get('api/view-sap-entries/export');
  }

  async getIsProcess() {
    return await this.get('api/view-sap-entries/is-process');
  }

  async putSap() {
    return await this.get('api/view-sap-entries/put-sap');
  }
}

export default viewSapEntries;
