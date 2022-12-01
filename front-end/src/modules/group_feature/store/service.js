import Service from 'core/service';
import {} from 'core/constants';

class GroupFeatureService extends Service {
  getAllGroup() {
    return this.rest('api/group/getAll');
  }
  getAllFeature() {
    return this.rest('api/featureinfo/getAll');
  }
  loadGroupFeature(params) {
    return this.rest('api/groupfeature/loadgroupfeature', params);
  }
  insGroupFeature(params) {
    return this.rest('api/groupfeature/insgroupfeature', params);
  }
  delGroupFeature(params) {
    return this.rest('api/groupfeature/delgroupfeature', params);
  }
}

export default GroupFeatureService;
