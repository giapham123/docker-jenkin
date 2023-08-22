// import qs from 'qs';
import Service from 'core/service';
import {} from 'core/constants';

class returnBookingCase extends Service {
  async getReturnBookingCase(param) {
    return await this.post(
      'api/return-booking-case/get-data-return-booking',
      param
    );
  }
  async export() {
    return await this.get('api/return-booking-case/export');
  }
}
export default returnBookingCase;
