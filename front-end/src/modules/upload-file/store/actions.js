import UploadFileService from 'modules/upload-file/store/service';

const service = new UploadFileService();

export const getUploadFileData = async (dispatch, param) => {
  const result = await service.getUploadFileData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getTotalRows = async (dispatch, param) => {
  const result = await service.getTotalRows(param);

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
