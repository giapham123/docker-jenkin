import AccoutingHisService from 'modules/accounting-his/store/service';

const service = new AccoutingHisService();

export const getDataAccountingHis = async (dispatch, param) => {
  const result = await service.getDataAccountingHis(param);

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
