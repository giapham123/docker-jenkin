import genSAP from 'modules/gen-sap/store/service';

const service = new genSAP();

export const getGenSAPData = async (dispatch, param) => {
  const result = await service.getGenSAPData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const authorGenSap = async dispatch => {
  const result = await service.authorGenSap();

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
