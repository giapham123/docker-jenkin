import WriteOffService from 'modules/writeoff-ext/store/service';

const service = new WriteOffService();

export const getDataWriteOff = async (dispatch, param) => {
  const result = await service.getDataWriteOff(param);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportDataWriteOff = async (dispatch, param) => {
  const result = await service.exportDataWriteOff(param);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
