import AccoutingService from 'modules/accounting/store/service';

const service = new AccoutingService();

export const getDataAccounting = async (dispatch, param) => {
  const result = await service.getDataAccounting(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportExcelFile = async (dispatch, param) => {
  const result = await service.exportExcelFile(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
