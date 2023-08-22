import Vue from 'vue';
import axios from 'axios';
import Vuetify from 'vuetify';
import VeeValidate from 'vee-validate';
import colors from 'vuetify/es5/util/colors';
import VueGAPI from 'vue-gapi';

import router from 'router';
import Filters from 'core/filters';
import store from 'store';
import App from './App';
import updateData from 'core/mixins/updateData';
import message from './message';
import { ACCESS_TOKEN_KEY, AUTH_HEADER_KEY } from 'core/constants';
import { i18n, loadLanguageAsync } from 'locales';
import 'core/theme/default.styl';
import Service from 'core/service';
import { TIME_FOR_KEEP_LOGIN } from 'core/constants';
import moment from 'moment';

Vue.config.productionTip = false;

Vue.use(Filters);
Vue.mixin(updateData);
Vue.use(VeeValidate, { fieldsBagName: 'formFields' });
Vue.use(Vuetify, {
  iconfont: 'md',
  theme: {
    primary: colors.indigo.base,
    secondary: colors.indigo.lighten4,
    accent: colors.indigo.base,
    error: colors.red.accent3
  },
  options: {
    themeVariations: ['primary', 'secondary', 'accent'],
    extra: {
      mainToolbar: {
        color: 'primary'
      },
      sideToolbar: {},
      sideNav: 'red',
      mainNav: 'red',
      bodyBg: '',
      dark: true
    }
  }
});

Vue.use(VueGAPI, {});

axios.defaults.headers.post['Content-Type'] = 'application/json'; //application/x-www-form-urlencoded; charset=UTF-8
axios.defaults.headers.put['Content-Type'] = 'application/json';
const token = localStorage.getItem(ACCESS_TOKEN_KEY);
if (token) {
  axios.defaults.headers.common[AUTH_HEADER_KEY] = `${token}`;
}

store.dispatch('login/setExpires');
// var Clock = {
//   totalSeconds: 0,
//   startTimer: function() {
//     if (!this.interval) {
//       var self = this;
//       function pad(val) {
//         return val > 9 ? val : '0' + val;
//       }
//       this.interval = setInterval(function() {
//         self.totalSeconds += 1;
//         localStorage.setItem('countLogin', self.totalSeconds);
//         if (self.totalSeconds >= TIME_FOR_KEEP_LOGIN) {
//           store.dispatch('login/updateTime', {
//             accountId: localStorage.getItem('userId'),
//             timeKeepAccount: TIME_FOR_KEEP_LOGIN
//           });
//           store.dispatch('login/logout');
//         }
//       }, 1000);
//     }
//   },
//   resetTimer: function() {
//     Clock.totalSeconds = null;
//     clearInterval(this.interval);
//     delete this.interval;
//   },
//   pauseTimer: function() {
//     clearInterval(this.interval);
//     delete this.interval;
//   },
//   resumeTimer: function() {
//     this.startTimer();
//   }
// };

// window.onbeforeunload = function(e) {
//   localStorage.setItem(
//     'userId',
//     JSON.parse(localStorage.getItem('user')).account_id
//   );
//   // localStorage.setItem('APP_EXP_AT', new Date().getTime());
// };
// const expired = store.getters['login/expired'];
// if (expired) {
//   store.dispatch('login/updateTime', {
//     accountId: localStorage.getItem('userId'),
//     timeKeepAccount: TIME_FOR_KEEP_LOGIN
//   });
//   store.dispatch('login/logout');
//   localStorage.removeItem('userId');
// }
Service.interceptors({
  request: request => {
    const diff = moment(moment(), 'x').diff(
      parseInt(localStorage.getItem('APP_EXP_AT')),
      'seconds'
    );
    if (diff >= TIME_FOR_KEEP_LOGIN) {
      window.location.reload();
    } else {
      localStorage.setItem('APP_EXP_AT', new Date().getTime());
    }
    const expired = store.getters['login/expired'];
    // if (Clock.totalSeconds >= TIME_FOR_KEEP_LOGIN) {
    //   store.dispatch('login/logout');
    //   if (typeof InstallTrigger !== 'undefined') {
    //     window.alert('Please reload page!');
    //   } else {
    //     location.reload();
    //   }
    // } else {
    //   store.dispatch('login/updateStateCallServer', true);
    //   Clock.resetTimer();
    // }

    if (expired) {
      store.dispatch('login/updateTime', {
        accountId: localStorage.getItem('userId'),
        timeKeepAccount: TIME_FOR_KEEP_LOGIN
      });
      return null;
    } else {
      store.dispatch('login/updateStateCallServer', true);
      // Clock.startTimer();
      return request;
    }
  }
});

// loadLanguageAsync
router.beforeEach((to, from, next) => {
  const lang = to.params.lang || 'en';
  loadLanguageAsync(lang).then(() => next());
});

const BUS = new Vue();
// Notifications
Vue.prototype.$bus = BUS;
Vue.prototype.$message = message;

new Vue({
  // eslint-disable-line no-new
  el: '#app',

  components: {
    App
  },

  router,
  store,
  i18n,
  render: h => h('app')
});
