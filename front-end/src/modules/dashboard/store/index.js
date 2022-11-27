import {
  SET_LOADING,
  UNDER_WRITER,
  ALL_DATA_ASSIGNMENT,
  SET_LIST_DATA
} from './types';

import * as getters from './getters';
import * as actions from './actions';

const state = {
  loading: false,
  undewriter: [],
  allDataAssignment: [],
  blackList: []
};

const mutations = {
  [UNDER_WRITER](state, result) {
    state.undewriter = result;
  },

  [ALL_DATA_ASSIGNMENT](state, result) {
    state.allDataAssignment = result;
  },

  [SET_LOADING](state, flag) {
    state.loading = flag;
  },

  [SET_LIST_DATA](state, data) {
    state.blackList = data;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
