import Service from 'core/service';
import {} from 'core/constants';

class StaffInfoService extends Service {
  getAllBranch() {
    return this.rest('api/branch/getAllBranch');
  }
  getAllStatus() {
    return this.rest('api/status/getAllStatus');
  }
}

export default StaffInfoService;
