/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:45
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-03 13:44:39
 */
import Service from 'core/service';

class ResetStatusService extends Service {
  resetStatus(params) {
    return this.rest('reset-password', params);
  }

  changePassword(params) {
    return this.rest('api/change-password', params);
  }
}

export default ResetStatusService;
