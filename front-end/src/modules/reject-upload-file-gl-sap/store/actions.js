import RejectUploadFileGLSAPService from 'modules/reject-upload-file-gl-sap/store/service';

const service = new RejectUploadFileGLSAPService();

export const getRejectUploadFileGLSAPData = async (dispatch, param) => {
  const result = await service.getRejectUploadFileGLSAPData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportData = async dispatch => {
  const result = await service.export();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
