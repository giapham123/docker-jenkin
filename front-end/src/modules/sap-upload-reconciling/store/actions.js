import uploadReconciling from 'modules/sap-upload-reconciling/store/service';

const service = new uploadReconciling();

export const uploadFile = async (dispatch, param) => {
  const result = await service.uploadFile(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getInitData = async (dispatch, param) => {
  const result = await service.getInitData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getDataUploadReconciling = async (dispatch, param) => {
  const result = await service.getDataUploadReconciling(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const deteleDataImportSap = async (dispatch, param) => {
  const result = await service.deteleDataImportSap(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
export const exportFunc = async (dispatch, param) => {
  const result = await service.export(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportErr = async (dispatch, param) => {
  const result = await service.exportErr(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
