import AccountManageService from 'modules/accountmanagement/store/service';

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

export const resetPass = async (dispatch, params) => {
  const result = await serviceAssignment.resetPassAcctInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAcctPermissionFeatureButton = async (dispatch, params) => {
  const result = await serviceAssignment.getAcctPermissionFeatureButton(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
export const addAcctPermissionFeatureButton = async (dispatch, params) => {
  const result = await serviceAssignment.addAcctPermissionFeatureButton(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const delAcctPermissionFeatureButton = async (dispatch, params) => {
  const result = await serviceAssignment.delAcctPermissionFeatureButton(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getFeatureInfo = async () => {
  const result = await serviceAssignment.getFeatureInfo();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
