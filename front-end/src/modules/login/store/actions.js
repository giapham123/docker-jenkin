/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:56
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-03 16:22:29
 */
/* eslint-disable */
import { CALL_SERVER_ACTION,RE_LOAD_PAGE,IS_LOG_OUT,SET_USER, AUTH_LOGOUT, SET_EXPIRES, AUTH_SUCCESS, AUTH_ERROR, SET_MESSAGE, USER_PERMISSION, NEED_CHANGE_PASS } from './types';
import LoginService from 'modules/login/store/service';
import ServiceToken from 'core/service';
import router from 'router';
import store from 'store';
import _ from 'lodash';
import { ACCESS_TOKEN_KEY, EXPIRES_AT, EXPIRES_IN, PERMISSIONS, USER, IS_DEFAULT_PASSWORD } from 'core/constants';

const service = new LoginService();

export const login = async ({ commit }, payload) => {
  const resp = await service.login(payload);
  if(resp != null && resp.success){
    const userIsLogin = await service.checkUserIsLogin({
      accountId: resp.data.user_info.account_id,
      permissions:resp.data.user_permissions
    });
    if(!userIsLogin.data.data){

      let expired_minutes = resp.data.expires_in;
      let expired_seconds = expired_minutes*60;

      localStorage.removeItem(IS_DEFAULT_PASSWORD);
      if (_.isBoolean(resp.data.is_default_password) && resp.data.is_default_password) {
        localStorage.setItem(IS_DEFAULT_PASSWORD, true);
      }

      localStorage.setItem(ACCESS_TOKEN_KEY, resp.data.access_token);
      localStorage.setItem(EXPIRES_AT, Date.now());
      localStorage.setItem(EXPIRES_IN, expired_seconds);
      localStorage.setItem(RE_LOAD_PAGE, 'true');
      localStorage.setItem(PERMISSIONS, JSON.stringify(resp.data.user_permissions));
      localStorage.setItem(USER, JSON.stringify(resp.data.user_info));
      localStorage.setItem(
            'userId',
            JSON.parse(localStorage.getItem('user')).account_id
          );
      ServiceToken.setToken(resp.data.access_token);

      commit(SET_USER, resp.data.user_info);
      commit(USER_PERMISSION, resp.data.user_permissions);

      authSuccess({ commit }, resp.data.access_token);
      setExpires({ commit });

      router.push('/');  
      await service.saveUserLogin({
        accountId:payload.username,
        permissions:resp.data.user_permissions
      });
    }else{
      localStorage.removeItem(ACCESS_TOKEN_KEY);
      localStorage.removeItem(EXPIRES_AT);
      localStorage.removeItem(EXPIRES_IN);
      localStorage.removeItem(PERMISSIONS);
      localStorage.removeItem(USER);
      localStorage.removeItem(RE_LOAD_PAGE);
      ServiceToken.removeToken();
      commit(AUTH_ERROR, false);
      commit(SET_MESSAGE, userIsLogin.data.message);
    }

  } else {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(EXPIRES_AT);
    localStorage.removeItem(EXPIRES_IN);
    localStorage.removeItem(PERMISSIONS);
    localStorage.removeItem(USER);
    localStorage.removeItem(RE_LOAD_PAGE);
    ServiceToken.removeToken();
    commit(AUTH_ERROR, false);
    commit(SET_MESSAGE, resp == null ? 'Can not connect to server. Please check your network!' : resp.message);
  }
};

export const authSuccess = ({ commit }, token ) => {
  commit(AUTH_SUCCESS, { token });
};

export const setExpires = ({ commit }) => {
  const expires_at = parseInt(localStorage.getItem(EXPIRES_AT) || Date.now());
  const expires_in = parseInt(localStorage.getItem(EXPIRES_IN) || 3600);

  if (!expires_at || !expires_in) return router.push('/login');
  commit(SET_EXPIRES, { expires_in, expires_at });
  const expired = store.getters['login/expired'];
  if(expired){
    logout({ commit });
  }
};

export const logout = async ({ commit }) => {
  commit(IS_LOG_OUT,true)
  // 
  return new Promise(resolve => {
    commit(AUTH_LOGOUT);
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(EXPIRES_AT);
    localStorage.removeItem(EXPIRES_IN);
    localStorage.removeItem(IS_DEFAULT_PASSWORD);
    localStorage.removeItem(PERMISSIONS);
    localStorage.removeItem(USER);
    ServiceToken.removeToken();
    // localStorage.removeItem('countLogin');
    localStorage.removeItem(RE_LOAD_PAGE);
    resolve();
  });
};

export const hasAuthorized = async (dispatch, btn_name) => {
  let modularName = router.currentRoute.name;
  const permissions = store.getters['login/permissions'];
  if (permissions == null) {
    return null;
  }

  if (permissions[modularName]) {
    let modular = permissions[modularName];
    if (modular.btns.length == 0) {
      return true;
    } else {
      return modular.btns.includes(btn_name);
    }
  } 
  return false;
};

export const isHidden = async (dispatch, field_name) => {
  try {
    let modularName = router.currentRoute.name;
    const permissions = store.getters['login/permissions'];
    if (permissions == null) {
      return null;
    }

    if (permissions[modularName]) {
      let modular = permissions[modularName];
      if (modular.hiddenFields != undefined) {
        if (modular.hiddenFields.length == 0) {
          return false;
        } else {
          return modular.hiddenFields.includes(field_name);
        }
      }
    } 
  } catch (e) {}
  return false;
};

export const validateToken = async () => {
  let token = store.getters['login/token'];
  if (token == '') return false;

  let result = await service.authToken(token);
  if (result.status == 200 && result.data != null && result.data.success != undefined) {
    return result.data.success;
  }
  return false;
};

export const changePassword = async (dispatch, params) => {
  const result = await service.changePassword(params);
  if (result.status == 200) {
    let needChangePass = store.getters['login/needChangePass'];
    if (needChangePass) {
      localStorage.removeItem(IS_DEFAULT_PASSWORD);
    }
    return result.data;
  }
  return null;
};

export const needChangePassword = async ({ commit }, payload) => {
  if(_.isBoolean(payload)) {
    commit(NEED_CHANGE_PASS, payload);
  }
};
export const removeUserLogin = async (dispatch, param) => {
  const resp = await service.removeUserLogin(param);
  return resp.data
};

export const checkUserIsLogin = async (dispatch, payload) => {
  const resp = await service.checkUserIsLogin(payload);
  return resp.data
};

export const updateTime = async (dispatch, payload) => {
  const resp = await service.updateTime(payload);
  return resp.data
};

export const updateStateCallServer = ({ commit }, payload) => {
    commit(CALL_SERVER_ACTION, payload);
};

export const getKey = async (dispatch) => {
  const resp = await service.getKey();
  return resp.data
};