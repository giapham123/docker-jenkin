// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class uploadBankStatement extends Service {
  async uploadBankStatementFile(param) {
    return await this.post(
      'api/upload-bank-statement/upload-bank-statement',
      param
    );
  }

  async getDataBankStatement(param) {
    return await this.post(
      'api/upload-bank-statement/get-data-bank-statement',
      param
    );
  }

  async exportExcelData(param) {
    return await this.post('api/upload-bank-statement/exportExcel', param);
  }
}

export default uploadBankStatement;
