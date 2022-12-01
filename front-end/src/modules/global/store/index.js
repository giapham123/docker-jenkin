import {
  TOGGLE_APP_DRAWER,
  SET_LOADING,
  SHOW_SUCCESS_MSG,
  RESET_SNACKBAR,
  SHOW_ERROR_MSG,
  SHOW_WARNING_MSG
} from './types';

import * as getters from './getters';
import * as actions from './actions';

const DEFAULT_SNACKBAR = {
  show: false,
  color: '',
  text: ''
};

const state = {
  drawerToggled: true,
  loading: false,
  snackbar: { ...DEFAULT_SNACKBAR }
};

const MSG_TYPE_COLORS = {
  error: 'red',
  success: 'green',
  warning: 'orange'
};

const showMsg = (state, { type, message }) => {
  state.snackbar = {
    show: true,
    text: message,
    color: MSG_TYPE_COLORS[type]
  };
};

const mutations = {
  [TOGGLE_APP_DRAWER](state, flag) {
    state.drawerToggled = flag;
  },

  [SET_LOADING](state, flag) {
    state.loading = flag;
  },

  [SHOW_SUCCESS_MSG](state, message) {
    showMsg(state, { type: 'success', message });
  },

  [SHOW_ERROR_MSG](state, message) {
    showMsg(state, { type: 'error', message });
  },

  [SHOW_WARNING_MSG](state, message) {
    showMsg(state, { type: 'warning', message });
  },

  [RESET_SNACKBAR](state) {
    state.snackbar = { ...DEFAULT_SNACKBAR };
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
