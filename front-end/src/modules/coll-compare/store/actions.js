import collCompare from 'modules/coll-compare/store/service';

const service = new collCompare();

export const collectorCompare = async (dispatch, param) => {
  const result = await service.collectorCompare(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getCollectorCompare = async (dispatch, param) => {
  const result = await service.getCollectorCompare(param);

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

export const exportFunc = async (dispatch, param) => {
  const result = await service.export(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
