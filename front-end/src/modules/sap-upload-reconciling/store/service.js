// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class uploadReconciling extends Service {
  async uploadFile(param) {
    return await this.post('api/sap-manual/upload-reconciling', param);
  }

  async getInitData(param) {
    return await this.get('api/sap-manual/get-init-data', param);
  }

  async getDataUploadReconciling(param) {
    return await this.post('api/sap-manual/get-data-reconciling-import', param);
  }

  async deteleDataImportSap(param) {
    return await this.post(
      'api/sap-manual/delete-data-reconciling-import',
      param
    );
  }
  async export() {
    return await this.get('api/sap-manual/export');
  }

  async exportErr() {
    return await this.get('api/sap-manual/export-err');
  }
}

export default uploadReconciling;
