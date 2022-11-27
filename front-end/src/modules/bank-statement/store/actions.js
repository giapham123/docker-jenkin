import BankStatement from 'modules/bank-statement/store/service';

const service = new BankStatement();

export const getDataBank = async (dispatch, param) => {
  const result = await service.getDataBank(param);

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

export const SyncFTP = async (dispatch) => {
  const result = await service.SyncFTP();

  if (result.status == 200) {
    return result.data;
  }
  return null;
};
