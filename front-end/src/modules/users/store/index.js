/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 10:02:25
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-02 10:24:21
 */
import * as getters from './getters';
import * as actions from './actions';
import { SET_USER } from './types';

const state = {
  userList: []
};
const mutations = {
  [SET_USER](state, data) {
    state.userList = data;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
