import DRSStaffInfoService from 'modules/staff_info/so_staff/store/service';

const serviceAssignment = new DRSStaffInfoService();

export const getInfo = async (dispatch, params) => {
  const result = await serviceAssignment.getInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const insertInfo = async (dispatch, params) => {
  const result = await serviceAssignment.insertInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const updateInfo = async (dispatch, params) => {
  const result = await serviceAssignment.updateInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const deleteInfo = async (dispatch, params) => {
  const result = await serviceAssignment.deleteInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const importData = async (dispatch, params) => {
  const result = await serviceAssignment.importDataDrs(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const updateData = async (dispatch, params) => {
  const result = await serviceAssignment.updateDataDrs(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
