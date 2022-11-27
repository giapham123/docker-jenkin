import DashboardService from 'modules/dashboard/store/service';
import { SET_LIST_DATA } from './types';

const serviceAssignment = new DashboardService();

export const getCustomerStatus = async (dispatch, params) => {
  const result = await serviceAssignment.getCustomerStatus(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getAllBlackListPartner = async ({ commit }) => {
  const result = await serviceAssignment.getAllBlackListPartner();
  commit(SET_LIST_DATA, result.data);
};

export const getScoreRangeCustomer = async (dispatch, params) => {
  const result = await serviceAssignment.getScoreRangeCustomer(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getCustomerFileScan = async (dispatch, params) => {
  const result = await serviceAssignment.getCustomerFileScan(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const viewDocumentFile = async (dispatch, params) => {
  const result = await serviceAssignment.viewDocumentFile(params);
  if (result.status == 200) {
    return result.data;
  }
  return null;
};

export const getSummarySystem = async (dispatch, params) => {
  const result = await serviceAssignment.getSummarySystem();
  return result.data;
};

export const getSummaryStatistic = async (dispatch, params) => {
  const result = await serviceAssignment.getSummaryStatistic();
  return result.data;
};

export const getDashboardMaker = async (dispatch, params) => {
  const result = await serviceAssignment.getDashboardMaker(params);
  return result.data;
};

export const getImportStatus = async (dispatch, params) => {
  const result = await serviceAssignment.getImportStatus();
  return result.data;
};
