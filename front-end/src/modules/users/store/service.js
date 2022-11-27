/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 10:02:28
 * @Last Modified by: Khang.Dong
 * @Last Modified time: 2018-10-02 10:24:20
 */
import Service from 'core/service';

class UserService extends Service {
  getAllUser() {
    return this.rest('api/getAllUser');
  }
}

export default UserService;
