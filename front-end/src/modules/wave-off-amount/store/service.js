// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class WaveOffAmountService extends Service {
  async uploadWaveOffAmount(param) {
    return await this.post(
      'api/wave-off-amount/uploadFileWaveOffAmount',
      param
    );
  }

  async getWaveOffAmountData(param) {
    return await this.post('api/wave-off-amount/getWaveOffAmountData', param);
  }

  async updateWaveOffAmount(param) {
    return await this.post('api/wave-off-amount/updateWaveOffAmount', param);
  }

  async uploadFileStatus() {
    return await this.get('api/wave-off-amount/uploadFileStatus');
  }

  async export() {
    return await this.get('api/wave-off-amount/export');
  }
}

export default WaveOffAmountService;
