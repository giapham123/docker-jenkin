/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:57:45
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-02 11:09:25
 */
import Service from 'core/service';

class ForgotPassService extends Service {
  sentEmailForgotPass(params) {
    return this.rest('forgot', params);
  }
}

export default ForgotPassService;
