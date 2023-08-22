import ScheduleBfIntAdvanceBookingService from 'modules/schedule-bf-int-advance-booking/store/service';

const service = new ScheduleBfIntAdvanceBookingService();

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
