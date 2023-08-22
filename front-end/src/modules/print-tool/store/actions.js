import __isString from 'lodash/isString';
import Service from 'core/service-v1';
import { TEMPLATE_INFO } from './types';

const REST_API = new Service();

export const fetchAllTemplates = async ({ commit }) => {
  const response = await REST_API.get('/api/print-management/templates');
  const { data, success } = response;
  if (success) {
    commit(TEMPLATE_INFO, data);
  } else {
    commit(TEMPLATE_INFO);
  }
};

export const printTemplate = async (dispatch, params) => {
  const { docCode, requestParams } = params || {};
  if (!__isString(docCode))
    return {
      success: false,
      message: 'Please provide a correct template type to print'
    };

  return await REST_API.get(
    `/api/print-management/templates/${docCode}`,
    requestParams
  );
};
export const uploadfiletoftp = async (dispatch, params) => {
  let options = {
    headers: { 'Content-Type': 'multipart/form-data' }
  };
  return await REST_API.post(
    '/api/print-management/templates/uploadfiletoftp/' + params.docCode,
    params.from_data,
    options
  );
};

export const retrieveNotifyHistories = async () => {
  return await REST_API.get('/api/print-management/notification');
};

export const viewTemplateFile = async (dispatch, params) => {
  const { filename, extension } = params || {};
  return await REST_API.get(
    `/api/print-management/templates/file/${filename}/${extension}`
  );
};

export const getAllHistories = async () => {
  return await REST_API.get('/api/print-management/templates/histories');
};

export const changeReadStatus = async (dispatch, id) => {
  return await REST_API.put(`/api/print-management/notification/${id}`);
};
