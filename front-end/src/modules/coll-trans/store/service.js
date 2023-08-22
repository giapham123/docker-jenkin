// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class collTrans extends Service {
  async getCollTransData(param) {
    return await this.post('api/coll-trans/get-coll-trans-data', param);
  }

  async export() {
    return await this.get('api/coll-trans/export');
  }

  async getTransactions() {
    return await this.get('api/coll-trans/get-transactions');
  }

  async getIsTrans() {
    return await this.get('api/coll-trans/is-get-trans');
  }
}

export default collTrans;
