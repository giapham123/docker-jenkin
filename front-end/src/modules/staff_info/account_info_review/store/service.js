import Service from 'core/service';
import {} from 'core/constants';

class accountInfoReview extends Service {
  getAllDepartment() {
    return this.rest('api/department/getAll');
  }

  getAllRequestType() {
    return this.rest('api/branch/getAllRequestType');
  }

  getAllBranch() {
    return this.rest('api/branch/getAllBranch');
  }

  getAllGroupPermission() {
    return this.rest('api/group/getAll');
  }

  getAllDepartmentByUser() {
    return this.get('api/department/getDepartmentNameByAccountId');
  }

  getListAccountInfoByDepartment(params) {
    return this.post('api/acctinfo/sim/getAccountInfoSearchReview', params);
  }
}

export default accountInfoReview;
