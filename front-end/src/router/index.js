'use strict';

/* eslint-disable */
import Vue from 'vue';
import Router from 'vue-router';
import NProgress from 'nprogress';

import store from 'store';
import {BASE_ROUTES, TERMI_ROUTES} from './routes';
import { IS_DEFAULT_PASSWORD } from 'core/constants';

Vue.use(Router);

const router = new Router({
  mode: 'history',
  linkActiveClass: 'open active',
  routes: [...BASE_ROUTES, ...TERMI_ROUTES]
});

router.beforeEach(async (to, from, next) => {
  document.title = to.meta.title || 'Accounting Module';
  NProgress.start();
  const isLoginPage = to.matched.some(p => p.path.indexOf('/login') === 0);

  try {
    let localVal = localStorage.getItem(IS_DEFAULT_PASSWORD);
    let isDefaultPass = localVal != null && Boolean(localVal) == true;
    store.dispatch('login/needChangePassword', isDefaultPass);

    let isAuthenticated = await store.dispatch('login/validateToken', {}, {root:true});
    let requiresAuth = to.matched.some(record => record.meta.requiresAuth);

    if (isAuthenticated && isLoginPage) {
      return next('/');
    }

    if (requiresAuth && !isAuthenticated) {
      if (isLoginPage) return next();
      return next('login');
    } else next();
  } catch (err) {
    if (isLoginPage) return next();
    next('login');
  }
});

router.afterEach(() => {
  NProgress.done();
});

export default router;
