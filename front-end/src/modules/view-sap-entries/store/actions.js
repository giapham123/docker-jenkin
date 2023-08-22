import viewSapEntries from 'modules/view-sap-entries/store/service';

const service = new viewSapEntries();

export const getDataViewSapEntries = async (dispatch, param) => {
  const result = await service.getDataViewSapEntries(param);

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

export const putSap = async dispatch => {
  const result = await service.putSap();

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
