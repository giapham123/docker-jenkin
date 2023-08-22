import WaveOffAmountService from 'modules/wave-off-amount/store/service';

const service = new WaveOffAmountService();

export const uploadWaveOffAmount = async (dispatch, param) => {
  const result = await service.uploadWaveOffAmount(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getWaveOffAmountData = async (dispatch, param) => {
  const result = await service.getWaveOffAmountData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const updateWaveOffAmount = async (dispatch, param) => {
  const result = await service.updateWaveOffAmount(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const uploadFileStatus = async dispatch => {
  const result = await service.uploadFileStatus();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportData = async dispatch => {
  const result = await service.export();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
