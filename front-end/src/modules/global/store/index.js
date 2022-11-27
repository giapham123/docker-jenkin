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

const state = {
  drawerToggled: true,
  loading: false,
  snackbar: { show: false }
};

const MSG_TYPE_COLORS = {
  ERROR: 'red',
  SUCCESS: 'green',
  WARNING: 'orange'
};

const showMsgByOption = (state, { color, payload }) => {
  let options = {
    ...{ color, timeout: 5000, 'multi-line': true },
    ...(payload.options || {})
  };
  let { x = 'right', y = 'top' } = options.position || {};

  state.snackbar = {
    show: true,
    text: payload.message || payload,
    options: {
      ...options,
      ...{
        top: y === 'top',
        bottom: y === 'bottom',
        left: x === 'left',
        right: x === 'right'
      }
    }
  };
};

const mutations = {
  [TOGGLE_APP_DRAWER](state, flag) {
    state.drawerToggled = flag;
  },

  [SET_LOADING](state, flag) {
    state.loading = flag;
  },

  [SHOW_SUCCESS_MSG](state, payload) {
    showMsgByOption(state, { color: MSG_TYPE_COLORS.SUCCESS, payload });
  },

  [SHOW_ERROR_MSG](state, payload) {
    showMsgByOption(state, { color: MSG_TYPE_COLORS.ERROR, payload });
  },

  [SHOW_WARNING_MSG](state, payload) {
    showMsgByOption(state, { color: MSG_TYPE_COLORS.WARNING, payload });
  },

  [RESET_SNACKBAR](state) {
    state.snackbar = { show: false };
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
