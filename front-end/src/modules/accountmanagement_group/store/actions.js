import AccountManageService from 'modules/accountmanagement_group/store/service';

const serviceAssignment = new AccountManageService();

export const getAllDepartment = async () => {
  const result = await serviceAssignment.getAllDepartment();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllGroupPermission = async () => {
  const result = await serviceAssignment.getAllGroupPermission();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAccountInfoSearch = async (dispatch, params) => {
  const result = await serviceAssignment.getAccountInfoSearch(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const insUserAccount = async (dispatch, params) => {
  const result = await serviceAssignment.insUserAccount(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const delUserAccount = async (dispatch, params) => {
  const result = await serviceAssignment.delUserAccount(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const delUserAccountGroupPermiss = async (dispatch, params) => {
  const result = await serviceAssignment.delUserAccountGroupPermiss(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const resetPass = async (dispatch, params) => {
  const result = await serviceAssignment.resetPassAcctInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAcctPermissionGroupFeature = async (dispatch, params) => {
  const result = await serviceAssignment.getAcctPermissionGroupFeature(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
export const addAcctPermissionGroupFeature = async (dispatch, params) => {
  const result = await serviceAssignment.addAcctPermissionGroupFeature(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const delAcctPermissionGroupFeature = async (dispatch, params) => {
  const result = await serviceAssignment.delAcctPermissionGroupFeature(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllGroup = async () => {
  const result = await serviceAssignment.getAllGroup();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
