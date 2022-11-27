/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:30
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-03 16:24:34
 */
import ResetStatusService from 'modules/reset-status/store/service';
import { SET_MESSAGE, SET_SUCCESS, SET_USER_ID } from './types';
const service = new ResetStatusService();
import router from 'router';
import ServiceToken from 'core/service';

export const resetStatus = async ({ commit }, param) => {
  const res = await service.resetStatus(param);
  commit(SET_SUCCESS, res.data.success);
  commit(SET_MESSAGE, res.data.message);
  commit(SET_USER_ID, res.data.dto.userId);
  ServiceToken.setToken(res.headers.authorization);
};

export const changePasswords = async ({ commit }, param) => {
  const res = await service.changePassword(param);
  commit(SET_SUCCESS, res.data.success);
  commit(SET_MESSAGE, res.data.message);
  if (res.data.success) {
    router.push('/login');
  }
};
