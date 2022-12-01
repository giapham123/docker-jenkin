/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:58:02
 * @Last Modified by:   Khang.Dong
 * @Last Modified time: 2018-10-02 09:58:02
 */
import moment from 'moment';

export const me = state => state.user;
export const isAuthenticated = state => !!state.token;
export const token = state => state.token;
export const menus = state => state.menus;
export const instances = state => {
  if (!state.user.instances) return [];
  let idx = 1;
  return state.user.instances.map(i => ({ ...i, id: idx++ }));
};
export const expired = state => {
  const { expires_at, expires_in } = state;
  const diff = moment(moment(), 'x').diff(expires_at, 'seconds');
  return diff >= expires_in;
};
export const hasLoadedOnce = state => state.hasLoadedOnce;
export const message = state => state.message;
export const permissions = state => state.user_permission;
export const needChangePass = state => state.is_default_password;
