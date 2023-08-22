// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class CloseSoldoutService extends Service {
  async loadingDetailsData(param) {
    return await this.post('api/close-sold-out/loading-details', param);
  }

  async closeApp(param) {
    return await this.post('api/close-sold-out/close-app', param);
  }

  async getCloseSoldout(param) {
    return await this.post('api/close-sold-out/get-close-sold-out', param);
  }
}

export default CloseSoldoutService;
