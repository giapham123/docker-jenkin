import { SET_LOADING } from './types';

import * as getters from './getters';
import * as actions from './actions';

const state = {
  loading: false
};

const mutations = {
  [SET_LOADING](state, flag) {
    state.loading = flag;
  }
};

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
};
