import RejectUploadFileService from 'modules/reject-upload-file/store/service';

const service = new RejectUploadFileService();

export const getRejectUploadFileData = async (dispatch, param) => {
  const result = await service.getRejectUploadFileData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportExcelFile = async (dispatch, param) => {
  const result = await service.exportExcelFile(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
