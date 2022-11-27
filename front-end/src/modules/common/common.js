import numeral from 'numeral';
export const formatNumber = (value, fixed, separator) => {
  if (value === null || value === '') return '';
  if (value == undefined) return null;
  let val = value;
  if (fixed != undefined && fixed != null) {
    val = (value / 1).toFixed(fixed);
  }
  if (separator == undefined) separator = ',';
  return val
    .toString()
    .replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1' + separator);
};

export const retrieveOnlyDate = val => {
  if (val === undefined || val == null) {
    return null;
  }

  let parts = val.split(' ');
  let date = new Date(parts[0]);
  if (validDate(date) || validDate(getDateFormatDDMMYYYY(parts[0]))) {
    return parts[0];
  }
  return null;
};

export const getDateFormatDDMMYYYY = val => {
  let date = new Date(val.replace(/(\d{2})\/(\d{2})\/(\d{4})/, '$2/$1/$3'));
  if (validDate(date)) {
    return date;
  }
  return null;
};

export const convertToUsDateFormat = val => {
  if (validDate(getDateFormatDDMMYYYY(val))) {
    return val.replace(/(\d{2})\/(\d{2})\/(\d{4})/, '$2/$1/$3');
  }
  return null;
};

export const validDate = val => {
  try {
    val.getTime();
  } catch (e) {
    return false;
  }

  return true;
};

export const getDistinctArray = (listError, separator) => {
  var v_error = [];
  if (listError.length > 0) {
    v_error = listError.filter(function onlyUnique(value, index, self) {
      return self.indexOf(value) === index;
    });
  }
  return v_error.join(separator);
};

export const format_level_staff = level_staff => {
  if (level_staff == 1) {
    return 'Fulltime';
  } else if (level_staff == 2) {
    return 'Partime';
  } else {
    return 'Không xác định';
  }
};

export const format_number = money => {
  var result = '0';

  if (money > 0) {
    result = format_number_globals(money);
  }
  return result;
};

export const format_number_globals = money => {
  if (money == null || money == undefined) return money;
  return numeral(money.toString().split('.')[0])
    .format('0,0')
    .replace(/,/g, '.');
};

function getDatePeriod(date) {
  var v_date = new Date(date);
  var v_month = v_date.getMonth() + 2;
  if (v_month > 12) {
    return v_date.getFullYear() + 1 + '-01';
  } else {
    return v_date.getFullYear() + '-' + v_month.toString().padStart(2, '0');
  }
}

export const getCommissionPeriod = noAll => {
  var v_date = new Date();
  var v_year = v_date.getFullYear();
  var v_month = v_date.getMonth() + 1;
  if (v_month == 12) {
    v_month = 1;
    v_year = v_year + 1;
  } else {
    v_month = v_month + 1;
  }
  var tmp = 0,
    aray_tmp = [],
    v_month_af = '';
  for (var i = 0; i < 5; i++) {
    tmp = v_month - i;
    if (tmp == 0) {
      aray_tmp.unshift({ text: v_year - 1 + '-12', value: v_year + '-01' });
    }
    if (tmp < 0) {
      v_month_af = v_year - 1 + '-' + (12 + tmp + '').padStart(2, '0');
      aray_tmp.unshift({
        text: v_month_af,
        value: getDatePeriod(v_month_af)
      });
    }
    if (tmp > 0 && tmp != 0) {
      v_month_af = v_year + '-' + (tmp + '').padStart(2, '0');
      getDatePeriod(v_year - 1 + '-' + v_month_af);
      aray_tmp.unshift({
        text: v_month_af,
        value: getDatePeriod(v_month_af)
      });
    }
    if (!noAll) {
      aray_tmp.unshift({ value: '0', text: '- All -' });
    }
  }
  return aray_tmp;
};
import moment from 'moment';
export const DRSCommissionDraft_FormatStatusFollowContract = (
  INSTLAMT_EMI,
  RECDAMT,
  DPD,
  DUEDATE
) => {
  var result = 'trễ'; //ConstantsHandler.MONEY_MAX
  var MONEY_MAX = 100000;
  var v_date = moment(DUEDATE, 'DD/MM/YYYY  HH:mm:ss').format(
    'YYYY-MM-DD HH:mm:ss'
  );
  var v_DUEDATE = new Date(v_date);
  var dateNow = new Date();
  if (DPD <= 15 && INSTLAMT_EMI - RECDAMT <= MONEY_MAX) {
    result = '<span style="color:green;font-weight:bold">OK</span>';
  } else if (v_DUEDATE.getTime() < dateNow.getTime() && RECDAMT == 0) {
    result = '<span style="color:red;font-weight:bold">Chưa đóng tiền</span>';
  } else if (v_DUEDATE.getTime() >= dateNow.getTime() && RECDAMT == 0) {
    // chưa tới ngày
    result = '<span style="color:black;font-weight:bold">-</span>';
  } else if (INSTLAMT_EMI > RECDAMT && INSTLAMT_EMI - RECDAMT > MONEY_MAX) {
    result = '<span style="color:blue;font-weight:bold">Đóng thiếu</span>';
  }
  return result;
};

export const formatDate = (val, _formatOriginal, _formatd) => {
  if (val == null || val == '') return null;
  if (!moment(val, _formatOriginal).isValid()) {
    return val;
  }
  return moment(val, _formatOriginal).format(_formatd);
};
