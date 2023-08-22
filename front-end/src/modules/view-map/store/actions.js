import viewMap from 'modules/view-map/store/service';

const service = new viewMap();

export const getDataViewMap = async (dispatch, param) => {
  const result = await service.getDataViewMap(param);

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
