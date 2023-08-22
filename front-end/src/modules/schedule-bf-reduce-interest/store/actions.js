import ScheduleBfReduceInterestService from 'modules/schedule-bf-reduce-interest/store/service';

const service = new ScheduleBfReduceInterestService();

export const getReduceInterest = async (dispatch, param) => {
  const result = await service.getReduceInterest(param);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getRepayment = async (dispatch, param) => {
  const result = await service.getRepayment(param);
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
