// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class viewMap extends Service {
  async getDataViewMap(param) {
    return await this.post('api/view-map/get-data-view-map', param);
  }
  async export() {
    return await this.get('api/view-map/export');
  }
}

export default viewMap;
