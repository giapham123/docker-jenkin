// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class viewFinnoneEntries extends Service {
  async getDataViewFinnoneEntries(param) {
    return await this.post(
      'api/view-finnone-entries/get-data-view-finnone',
      param
    );
  }
  async exportExcelFile(param) {
    return await this.post('api/view-finnone-entries/exportExcel', param);
  }
}

export default viewFinnoneEntries;
