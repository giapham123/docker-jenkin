import DrsDetailStaffInfoService from 'modules/staff_info/drs_detail/store/service';
const serviceAssignment = new DrsDetailStaffInfoService();

export const getInfo = async (dispatch, params) => {
  const result = await serviceAssignment.getInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAccount = async (dispatch, params) => {
  const result = await serviceAssignment.getAccount(params);
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
