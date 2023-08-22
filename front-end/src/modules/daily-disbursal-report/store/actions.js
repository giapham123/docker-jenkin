import DailyDisbursalReport from 'modules/daily-disbursal-report/store/service';

const service = new DailyDisbursalReport();

export const getDataDailyDisbursalReport = async (dispatch, param) => {
  const result = await service.getDataDailyDisbursalReport(param);

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
