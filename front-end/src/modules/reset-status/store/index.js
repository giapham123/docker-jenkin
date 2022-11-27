/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:42
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-03 13:21:07
 */
import * as getters from './getters';
import * as actions from './actions';
import { SET_MESSAGE, SET_SUCCESS, SET_USER_ID } from './types';

const state = {
  message: '',
  success: false,
  signal: false,
  userId: ''
};
const mutations = {
  [SET_MESSAGE](state, message) {
    state.message = message;
    state.signal = !state.signal;
  },

  [SET_SUCCESS](state, res) {
    state.success = res;
  },

  [SET_USER_ID](state, userId) {
    if (state.success) {
      state.userId = userId;
    }
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
