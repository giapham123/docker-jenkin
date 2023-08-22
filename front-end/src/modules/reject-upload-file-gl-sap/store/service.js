// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class RejectUploadFileGLSAPService extends Service {
  async getRejectUploadFileGLSAPData(param) {
    return await this.post(
      'api/reject-upload-file-gl-sap/get-data-reject-upload-file-gl-sap',
      param
    );
  }
  async export() {
    return await this.get('api/reject-upload-file-gl-sap/export');
  }
}

export default RejectUploadFileGLSAPService;
