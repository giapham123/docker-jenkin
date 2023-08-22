// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class genSAP extends Service {
  async getGenSAPData(param) {
    return await this.post('api/gen-sap/get-data-gen-sap', param);
  }

  async authorGenSap() {
    return await this.get('api/gen-sap/author-gen-sap');
  }

  async export() {
    return await this.get('api/gen-sap/export');
  }
  async getIsProcess() {
    return await this.get('api/gen-sap/is-process');
  }
}

export default genSAP;
