/*
 * @Author: Khang.Dong
 * @Email: khang.dong@dounets.com
 * @Date: 2018-10-02 09:58:10
 * @Last Modified by:   Khang.Dong
 * @Last Modified time: 2018-10-02 09:58:10
 */
import Service from 'core/service';

class LoginService extends Service {
  /**
   * Login with emaill & password
   *
   * @param {email: String, password: String} params
   */
  async login(params) {
    let result = await this.post('/login/auth', params);
    if (result.status == 200) {
      return result.data;
    }
    return null;
  }

  async changePassword(params) {
    return await this.put('/user-password', params);
  }

  authToken(token) {
    return this.get('/authenticate', { token: token });
  }
}

export default LoginService;
