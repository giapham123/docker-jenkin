import CloseSoldoutService from 'modules/close-soldout/store/service';

const service = new CloseSoldoutService();

export const loadingDetailsData = async (dispatch, param) => {
  const result = await service.loadingDetailsData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const closeApp = async (dispatch, param) => {
  const result = await service.closeApp(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getCloseSoldout = async (dispatch, param) => {
  const result = await service.getCloseSoldout(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
