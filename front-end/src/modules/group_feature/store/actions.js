import GroupFeatureService from 'modules/group_feature/store/service';

const serviceAssignment = new GroupFeatureService();

export const getAllGroup = async () => {
  const result = await serviceAssignment.getAllGroup();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllFeature = async () => {
  const result = await serviceAssignment.getAllFeature();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const loadGroupFeature = async (dispatch, params) => {
  const result = await serviceAssignment.loadGroupFeature(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const insGroupFeature = async (dispatch, params) => {
  const result = await serviceAssignment.insGroupFeature(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const delGroupFeature = async (dispatch, params) => {
  const result = await serviceAssignment.delGroupFeature(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
