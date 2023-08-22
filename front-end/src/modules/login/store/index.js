/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:58:06
 * @Last Modified by:   Khang.Dong
 * @Last Modified time: 2018-10-02 09:58:06
 */
import {
  IS_LOG_OUT,
  SET_USER,
  AUTH_SUCCESS,
  AUTH_ERROR,
  AUTH_LOGOUT,
  SET_EXPIRES,
  SET_MESSAGE,
  USER_PERMISSION,
  NEED_CHANGE_PASS,
  CALL_SERVER_ACTION
} from './types';

// import menus from './menus';

import * as getters from './getters';
import * as actions from './actions';
import Menu from './menus';
import { ACCESS_TOKEN_KEY, PERMISSIONS, USER } from 'core/constants';

const state = {
  menus: Menu,
  user: JSON.parse(localStorage.getItem(USER)),
  token: localStorage.getItem(ACCESS_TOKEN_KEY) || '',
  hasLoadedOnce: true,
  expires_at: Date.now(),
  expires_in: 3600,
  message: '',
  user_permission: JSON.parse(localStorage.getItem(PERMISSIONS)),
  is_default_password: false,
  is_log_out: false,
  is_active_call_server: false
};

const mutations = {
  [SET_USER](state, user) {
    state.user = user || {};
  },

  [AUTH_SUCCESS](state, resp) {
    state.token = resp.token;
    state.hasLoadedOnce = true;
  },

  [AUTH_ERROR](state, resp) {
    state.hasLoadedOnce = resp;
  },

  [AUTH_LOGOUT](state) {
    state.token = '';
  },

  [SET_EXPIRES](state, { expires_in, expires_at }) {
    state.expires_in = expires_in;
    state.expires_at = expires_at;
  },

  [SET_MESSAGE](state, message) {
    state.message = message;
  },

  [USER_PERMISSION](state, user_permission) {
    state.user_permission = user_permission;
  },

  [NEED_CHANGE_PASS](state, flag) {
    state.is_default_password = flag;
  },
  [IS_LOG_OUT](state, flag) {
    state.is_log_out = flag;
  },
  [CALL_SERVER_ACTION](state, flag) {
    state.is_active_call_server = flag;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
