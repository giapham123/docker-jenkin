import CheckTerminationDailyRPTService from 'modules/check-termination-daily-report/store/service';

const service = new CheckTerminationDailyRPTService();

export const getTerminationDailyReport = async (dispatch, param) => {
  const result = await service.getTerminationDailyReport(param);

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
