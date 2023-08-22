import uploadBankStatement from 'modules/upload-bank-statement/store/service';

const service = new uploadBankStatement();

export const uploadBankStatementFile = async (dispatch, param) => {
  const result = await service.uploadBankStatementFile(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getDataBankStatement = async (dispatch, param) => {
  const result = await service.getDataBankStatement(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const exportExcel = async (dispatch, param) => {
  const result = await service.exportExcelData(param);

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
