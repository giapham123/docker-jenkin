import TerminationReportApp from 'modules/termination-report-app/store/service';

const service = new TerminationReportApp();

export const getTerminationReportApp = async (dispatch, param) => {
  const result = await service.getDataTerminationReportApp(param);

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
