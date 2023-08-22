import collTrans from 'modules/coll-trans/store/service';

const service = new collTrans();

export const getCollTransData = async (dispatch, param) => {
  const result = await service.getCollTransData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportColl = async (dispatch, param) => {
  const result = await service.export(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getTransactions = async (dispatch, param) => {
  const result = await service.getTransactions(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getIsTrans = async (dispatch, param) => {
  const result = await service.getIsTrans(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
