import accountInfoReview from 'modules/staff_info/account_info_review/store/service';

const serviceAssignment = new accountInfoReview();

export const getAllDepartment = async () => {
  const result = await serviceAssignment.getAllDepartment();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllBranch = async () => {
  const result = await serviceAssignment.getAllBranch();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getListAccountInfoByDepartment = async (dispatch, params) => {
  const result = await serviceAssignment.getListAccountInfoByDepartment(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllDepartmentByUser = async () => {
  const result = await serviceAssignment.getAllDepartmentByUser();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
