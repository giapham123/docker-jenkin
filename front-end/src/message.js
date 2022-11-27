import store from './store';

class Message {
  constructor(options) {
    this._options = options;
  }

  success(msg) {
    store.dispatch('global/showSuccessMsg', msg, { root: true });
  }

  warning(msg) {
    store.dispatch('global/showWarningMsg', msg, { root: true });
  }

  error(msg) {
    store.dispatch('global/showErrorMsg', msg, { root: true });
  }
}

export default new Message();
