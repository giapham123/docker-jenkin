// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class pendingDisbursement extends Service {
  async uploadFile(param) {
    return await this.post('api/pending-disbur/upload-file-agre', param);
  }

  async insertSigAgre(param) {
    return await this.get('api/pending-disbur/insert-sig-agree', param);
  }

  async getDataPendingDisbursement(param) {
    return await this.get('api/pending-disbur/get-data-pending-disbur', param);
  }

  async deleteAgreementId(param) {
    return await this.post('api/pending-disbur/delete-agree', param);
  }

  async processPending() {
    return await this.get('api/pending-disbur/process-pending-disbur');
  }

  async export() {
    return await this.get('api/pending-disbur/export');
  }

  async getIsProcess() {
    return await this.get('api/pending-disbur/is-process');
  }
}

export default pendingDisbursement;
