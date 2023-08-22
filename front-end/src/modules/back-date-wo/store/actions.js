import backDateWOService from 'modules/back-date-wo/store/service';

const service = new backDateWOService();

export const getDataBackDate = async (dispatch, param) => {
  const result = await service.getDataBackDate(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const processPendingBackDate = async (dispatch, param) => {
  const result = await service.processPendingBackDate(param);

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
