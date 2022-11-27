import {
  TOGGLE_APP_DRAWER,
  SET_LOADING,
  SHOW_SUCCESS_MSG,
  RESET_SNACKBAR,
  SHOW_ERROR_MSG,
  SHOW_WARNING_MSG
} from 'modules/global/store/types';

export const toggleDrawer = ({ commit }, flag = true) => {
  commit(TOGGLE_APP_DRAWER, flag);
};

export const setLoading = ({ commit }, flag = true) => {
  commit(SET_LOADING, flag);
};

export const resetMsg = ({ commit }) => commit(RESET_SNACKBAR);

export const showSuccessMsg = ({ commit }, msg) => {
  commit(SHOW_SUCCESS_MSG, msg);
};

export const showErrorMsg = ({ commit }, msg) => {
  commit(SHOW_ERROR_MSG, msg);
};

export const showWarningMsg = ({ commit }, msg) => {
  commit(SHOW_WARNING_MSG, msg);
};
