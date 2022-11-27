// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class BankStatement extends Service {
  async getDataBank(param) {
    return await this.get('api/bank-statement/get-bank', param);
  }
  async getDataBankStatement(param) {
    return await this.post('api/bank-statement/get-bank-statement', param);
  }
  async SyncFTP() {
    return await this.get('api/ftp/SyncFTP');
  }
}

export default BankStatement;
