// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class UploadFileService extends Service {
  async getUploadFileData(param) {
    return await this.post(
      'api/receipt-upload-file/get-data-upload-file',
      param
    );
  }
  async exportExcelFile(param) {
    return await this.post('api/receipt-upload-file/exportExcel-upload', param);
  }

  async getTotalRows(param) {
    return await this.post('api/receipt-upload-file/get-total-rows', param);
  }
}

export default UploadFileService;
