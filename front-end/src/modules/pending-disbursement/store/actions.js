import pendingDisbursement from 'modules/pending-disbursement/store/service';

const service = new pendingDisbursement();

export const uploadFile = async (dispatch, param) => {
  const result = await service.uploadFile(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const insertSigAgre = async (dispatch, param) => {
  const result = await service.insertSigAgre(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getDataPendingDisbursement = async (dispatch, param) => {
  const result = await service.getDataPendingDisbursement(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const deleteAgreementId = async (dispatch, param) => {
  const result = await service.deleteAgreementId(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const processPending = async dispatch => {
  const result = await service.processPending();

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

export const getIsProcess = async dispatch => {
  const result = await service.getIsProcess();

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
