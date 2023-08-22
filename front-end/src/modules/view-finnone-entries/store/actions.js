import viewFinnoneEntries from 'modules/view-finnone-entries/store/service';

const service = new viewFinnoneEntries();

export const getDataViewFinnoneEntries = async (dispatch, param) => {
  const result = await service.getDataViewFinnoneEntries(param);

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
