import OutNetReportService from 'modules/out-net-report/store/service';

const service = new OutNetReportService();

export const getOutNetReportData = async (dispatch, param) => {
  const result = await service.getOutNetReportData(param);

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
