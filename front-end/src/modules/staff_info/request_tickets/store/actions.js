import requestTicket from 'modules/staff_info/request_tickets/store/service';

const serviceAssignment = new requestTicket();

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

export const getAllGroupPermission = async () => {
  const result = await serviceAssignment.getAllGroupPermission();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAccountInfoSearchRequestTicket = async (dispatch, params) => {
  const result = await serviceAssignment.getAccountInfoSearchRequestTicket(
    params
  );
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getListPermissionGroupFeatureByApp = async (dispatch, params) => {
  const result = await serviceAssignment.getListPermissionGroupFeatureByApp(
    params
  );
  return result.data;
};

export const getAppByDepartmentId = async (dispatch, params) => {
  const result = await serviceAssignment.getAppByDepartmentId(params);
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

export const convertStringToList = async (dispatch, data) => {
  let result = [];
  if (data == null || data == 'null') return null;
  if (!_.isArray(data)) {
    result.push(data);
  } else {
    result = data;
  }
  return result;
};

export const createAccountInfoDetail = async (dispatch, params) => {
  const result = await serviceAssignment.createAccountInfoDetail(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getInfoAccountInfoDetail = async (dispatch, params) => {
  const result = await serviceAssignment.getInfoAccountInfoDetail(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const createRequestUpdateAccountInfo = async (dispatch, params) => {
  const result = await serviceAssignment.createRequestUpdateAccountInfo(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getGroupFeatureCurrentPermission = async (dispatch, params) => {
  const result = await serviceAssignment.getGroupFeatureCurrentPermission(
    params
  );
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const createRequestUpdatePermission = async (dispatch, params) => {
  const result = await serviceAssignment.createRequestUpdatePermission(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const createRequestResetPassword = async (dispatch, params) => {
  const result = await serviceAssignment.createRequestResetPassword(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const createRequestActiveInactive = async (dispatch, params) => {
  const result = await serviceAssignment.createRequestActiveInactive(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const createRequestRemoveAccount = async (dispatch, params) => {
  const result = await serviceAssignment.createRequestRemoveAccount(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAccountInfoRequestExist = async (dispatch, params) => {
  const result = await serviceAssignment.getAccountInfoRequestExist(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
