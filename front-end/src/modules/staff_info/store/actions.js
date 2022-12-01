import StaffInfoService from 'modules/staff_info/store/service';

const serviceAssignment = new StaffInfoService();

export const getAllBranch = async () => {
  const result = await serviceAssignment.getAllBranch();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllStatus = async () => {
  const result = await serviceAssignment.getAllStatus();
  if (result.status == 200) {
    return result.data;
  }
  return null;
};
