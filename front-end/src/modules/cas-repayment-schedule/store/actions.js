import CasRepaymentScheduleService from 'modules/cas-repayment-schedule/store/service';

const service = new CasRepaymentScheduleService();

export const getDataCasRepayment = async (dispatch, param) => {
  const result = await service.getDataCasRepayment(param);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
export const exportData = async (dispatch, param) => {
  const result = await service.export(param);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
