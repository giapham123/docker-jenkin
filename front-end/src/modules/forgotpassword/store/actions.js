/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:30
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-03 16:23:13
 */
import ForgotPassService from 'modules/forgotpassword/store/service';
import { SET_MESSAGE, SET_SUCCESS } from './types';
import router from 'router';
const service = new ForgotPassService();

export const sendForgotPassEmail = async ({ commit }, param) => {
  const res = await service.sentEmailForgotPass(param);
  commit(SET_SUCCESS, res.data.success);
  commit(SET_MESSAGE, res.data.message);
  if (res.data.success) {
    router.push('/login');
  }
};
