import DetailDisbursalReport from 'modules/detail-disbursal-report/store/service';

const service = new DetailDisbursalReport();

export const getDataDetailDisbursalReport = async (dispatch, param) => {
  const result = await service.getDataDetailDisbursalReport(param);

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
