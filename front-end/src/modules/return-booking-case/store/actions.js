import returnBookingCase from 'modules/return-booking-case/store/service';

const service = new returnBookingCase();

export const getReturnBookingCase = async (dispatch, param) => {
  const result = await service.getReturnBookingCase(param);
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
