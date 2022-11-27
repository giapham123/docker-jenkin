/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:58:36
 * @Last Modified by:   Khang.Dong
 * @Last Modified time: 2018-10-02 09:58:36
 */
import Service from 'core/service';

class RegisterService extends Service {
  registerUser(params) {
    return this.rest('registerUser', params);
  }
}

export default RegisterService;
