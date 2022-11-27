/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 10:02:20
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-04 09:11:37
 */
import UserService from 'modules/users/store/service';
import { SET_USER } from './types';
const service = new UserService();

export const getAllUser = async ({ commit }) => {
  const res = await service.getAllUser();
  commit(SET_USER, res.data);
};
