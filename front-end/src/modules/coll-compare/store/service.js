// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class collCompare extends Service {
  async collectorCompare(param) {
    return await this.get('api/coll-compare/collector', param);
  }

  async getCollectorCompare(param) {
    return await this.post('api/coll-compare/get-collector-compare', param);
  }

  async export() {
    return await this.get('api/coll-compare/export');
  }

  async getIsProcess() {
    return await this.get('api/coll-compare/is-process');
  }
}

export default collCompare;
