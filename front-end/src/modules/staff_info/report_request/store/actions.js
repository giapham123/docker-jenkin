import reportRequest from 'modules/staff_info/report_request/store/service';

const serviceAssignment = new reportRequest();

export const getAllDepartment = async () => {
  const result = await serviceAssignment.getAllDepartment();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllRequestType = async () => {
  const result = await serviceAssignment.getAllRequestType();
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

export const getAccountInfoInRequestSummary = async (dispatch, params) => {
  const result = await serviceAssignment.getAccountInfoInRequestSummary(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getRequestDetailByRequestId = async (dispatch, params) => {
  const result = await serviceAssignment.getRequestDetailByRequestId(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const verifyRegisterResult = async (dispatch, params) => {
  const result = await serviceAssignment.verifyRegisterResult(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getListResetPassRequest = async (dispatch, params) => {
  const result = await serviceAssignment.getListResetPassRequest(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const updVerifyResetPassResult = async (dispatch, params) => {
  const result = await serviceAssignment.updVerifyResetPassResult(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getListActiveInactiveRequest = async (dispatch, params) => {
  const result = await serviceAssignment.getListActiveInactiveRequest(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const updVerifyActiveInactiveRequest = async (dispatch, params) => {
  const result = await serviceAssignment.updVerifyActiveInactiveRequest(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const verifyUpdateInfoResult = async (dispatch, params) => {
  const result = await serviceAssignment.verifyUpdateInfoResult(params);
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

export const updVerifyUpdatePermissionRequest = async (dispatch, params) => {
  const result = await serviceAssignment.updVerifyUpdatePermissionRequest(
    params
  );
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getListRemoveAccountRequest = async (dispatch, params) => {
  const result = await serviceAssignment.getListRemoveAccountRequest(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const updVerifyremoveAccountResult = async (dispatch, params) => {
  const result = await serviceAssignment.updVerifyremoveAccountResult(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
