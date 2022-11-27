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

  async saveUserLogin(params) {
    let result = await this.post('/login/save-user-login', params);
    return result;
  }

  async removeUserLogin(param) {
    let result = await this.get('/login/remove-user-login', param);
    return result;
  }

  async checkUserIsLogin(params) {
    let result = await this.post('/login/check-user-islogin', params);
    return result;
  }

  async updateTime(params) {
    let result = await this.post('/login/update-timeout', params);
    return result;
  }
}

export default LoginService;
