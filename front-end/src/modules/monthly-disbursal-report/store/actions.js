import MonthlyDisbursalReport from 'modules/monthly-disbursal-report/store/service';

const service = new MonthlyDisbursalReport();

export const getDataMonthlyDisbursalReport = async (dispatch, param) => {
  const result = await service.getDataMonthlyDisbursalReport(param);

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
