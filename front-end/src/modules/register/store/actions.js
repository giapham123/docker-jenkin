/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:58:27
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-03 16:23:40
 */
import RegisterService from 'modules/register/store/service';
import { SET_MESSAGE, SET_SUCCESS } from './types';
import router from 'router';
const service = new RegisterService();

export const registerUser = async ({ commit }, param) => {
  const res = await service.registerUser(param);
  commit(SET_SUCCESS, res.data.success);
  commit(SET_MESSAGE, res.data.message);
  if (res.data.success) {
    router.push('/login');
  }
};
