/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:42
 * @Last Modified by:   Khang.Dong
 * @Last Modified time: 2018-10-02 09:57:42
 */
import * as getters from './getters';
import * as actions from './actions';
import { SET_MESSAGE, SET_SUCCESS } from './types';

const state = {
  message: '',
  success: false
};
const mutations = {
  [SET_MESSAGE](state, message) {
    state.message = message;
  },

  [SET_SUCCESS](state, res) {
    state.success = res;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
