import * as actions from './actions';
import * as getters from './getters';
import { TEMPLATE_INFO } from './types';

const state = {
  print_templates: []
};

const mutations = {
  [TEMPLATE_INFO](state, payload = []) {
    state.print_templates = payload;
  }
};

export default {
  namespaced: true,
  state,
  actions,
  mutations,
  getters
};
