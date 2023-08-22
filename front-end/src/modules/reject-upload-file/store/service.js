// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class RejectUploadFileService extends Service {
  async getRejectUploadFileData(param) {
    return await this.post(
      'api/reject-upload-file/get-data-reject-upload-file',
      param
    );
  }
  async exportExcelFile(param) {
    return await this.post('api/reject-upload-file/exportExcel-reject', param);
  }
}

export default RejectUploadFileService;
