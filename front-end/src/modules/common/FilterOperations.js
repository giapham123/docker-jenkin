import _ from 'lodash';
import moment from 'moment';

const FILTER_OPERATIONS = {
  STRING: {
    START_WITH: {
      code: 'START_WITH',
      text: 'Start with',
      icon: 'fa-hashtag',
      exec: (before, after) => _.startsWith(before, after)
    },
    NOT_START_WITH: {
      code: 'NOT_START_WITH',
      text: 'Not start with',
      icon: 'fa-hashtag',
      exec: (before, after) => !_.startsWith(before, after)
    },
    END_WITH: {
      code: 'END_WITH',
      text: 'End with',
      icon: 'fa-hashtag',
      exec: (before, after) => _.endsWith(before, after)
    },
    NOT_END_WITH: {
      code: 'NOT_END_WITH',
      text: 'Not end with',
      icon: 'fa-hashtag',
      exec: (before, after) => !_.endsWith(before, after)
    },
    INCLUDES: {
      code: 'INCLUDES',
      text: 'Includes',
      icon: 'fa-equals',
      exec: (before, after) => _.includes(before, after)
    },
    NOT_INCLUDES: {
      code: 'NOT_INCLUDES',
      text: 'Not includes',
      icon: 'fa-not-equal',
      exec: (before, after) => !_.includes(before, after)
    },
    EQUALS: {
      code: 'EQUALS',
      text: 'Equals',
      icon: 'fa-equals',
      exec: (before, after) => before === after
    },
    NOT_EQUALS: {
      code: 'NOT_EQUALS',
      text: 'Not equals',
      icon: 'fa-not-equal',
      exec: (before, after) => before !== after
    }
  },

  NUMBER: {
    START_WITH: {
      code: 'START_WITH',
      text: 'Start with',
      icon: 'fa-hashtag',
      exec: (before, after) => _.startsWith(before, after)
    },
    NOT_START_WITH: {
      code: 'NOT_START_WITH',
      text: 'Not start with',
      icon: 'fa-hashtag',
      exec: (before, after) => !_.startsWith(before, after)
    },
    END_WITH: {
      code: 'END_WITH',
      text: 'End with',
      icon: 'fa-hashtag',
      exec: (before, after) => _.endsWith(before, after)
    },
    NOT_END_WITH: {
      code: 'NOT_END_WITH',
      text: 'Not end with',
      icon: 'fa-hashtag',
      exec: (before, after) => !_.endsWith(before, after)
    },
    INCLUDES: {
      code: 'INCLUDES',
      text: 'Includes',
      icon: 'fa-equals',
      exec: (before, after) => _.includes(before, after)
    },
    NOT_INCLUDES: {
      code: 'NOT_INCLUDES',
      text: 'Not includes',
      icon: 'fa-not-equal',
      exec: (before, after) => !_.includes(before, after)
    },
    EQUALS: {
      code: 'EQUALS',
      text: 'Equals',
      icon: 'fa-equals',
      exec: (before, after) => before === after
    },
    NOT_EQUALS: {
      code: 'NOT_EQUALS',
      text: 'Not equals',
      icon: 'fa-not-equal',
      exec: (before, after) => before !== after
    },
    LESS_THAN: {
      code: 'LESS_THAN',
      text: 'Less than',
      icon: 'fa-less-than',
      exec: (before, after) => before < after
    },
    LESS_THAN_EQUAL: {
      code: 'LESS_THAN_EQUAL',
      text: 'Less than or equals',
      icon: 'fa-less-than-equal',
      exec: (before, after) => before <= after
    },
    GREATER_THAN: {
      code: 'GREATER_THAN',
      text: 'Greater than',
      icon: 'fa-greater-than',
      exec: (before, after) => before > after
    },
    GREATER_THAN_EQUAL: {
      code: 'GREATER_THAN_EQUAL',
      text: 'Greater than or equals',
      icon: 'fa-greater-than-equal',
      exec: (before, after) => before >= after
    }
  },

  DATE: {
    EQUALS: {
      code: 'EQUALS',
      text: 'Equals',
      icon: 'fa-equals',
      exec: (before, after) => before.diff(after, 'days') == 0
    },
    NOT_EQUALS: {
      code: 'NOT_EQUALS',
      text: 'Not equals',
      icon: 'fa-not-equal',
      exec: (before, after) => before.diff(after, 'days') != 0
    },
    BEFORE: {
      code: 'BEFORE',
      text: 'Before date',
      icon: 'fa-not-equal',
      exec: (before, after) => before.diff(after, 'days') < 0
    },
    AFTER: {
      code: 'AFTER',
      text: 'After date',
      icon: 'fa-not-equal',
      exec: (before, after) => before.diff(after, 'days') > 0
    }
  }
};

export const FILTER_ULTIS = {
  filter: (condition, record) => {
    let prepare = FILTER_ULTIS.prepare[condition.type];
    let search = prepare(condition.value);
    let base = prepare(record[condition.column]);
    if (
      FILTER_ULTIS.compare[condition.type](base, search, condition.operation)
    ) {
      condition.results.push(record[condition.key]);
    }
  },

  compare: {
    ['STRING'](base, search, operation) {
      return operation.exec(base, search);
    },

    ['DATE'](base, search, operation) {
      if (base == null || search == null) {
        return false;
      }
      return operation.exec(base, search);
    },

    ['NUMBER'](base, search, operation) {
      let asStringCompare =
        FILTER_OPERATIONS.STRING[operation.code] !== undefined;
      if (asStringCompare) {
        return operation.exec(_.toString(base), _.toString(search));
      } else {
        if (Number.isNaN(base) || Number.isNaN(search)) {
          return false;
        }
        return operation.exec(base, search);
      }
    },

    ['BOOL'](base, search) {
      return base == search;
    }
  },

  prepare: {
    ['STRING'](val) {
      return _.toLower(val);
    },

    ['DATE'](val) {
      let date = moment(val, SUPPORT_DATE_FORMAT);
      return date.isValid() ? date : null;
    },

    ['NUMBER'](val) {
      if (_.isNil(val) || _.trim(val) == '') {
        return Number.NaN;
      }
      return Number(val);
    },

    ['BOOL'](val) {
      if (_.isBoolean(val)) {
        return val;
      } else if (_.isString(val)) {
        return val == 'checked';
      }
      return false;
    }
  },

  ignore: {
    ['STRING'](val) {
      return val == '';
    },

    ['DATE'](val) {
      return val == '';
    },

    ['NUMBER'](val) {
      return Number.isNaN(FILTER_ULTIS.prepare.NUMBER(val));
    },

    ['BOOL'](val) {
      return val == '';
    }
  }
};

const SUPPORT_DATE_FORMAT = ['YYYY-MM-DD', 'MM/DD/YYYY'];
export const SUPPORT_TYPE = ['STRING', 'DATE', 'NUMBER', 'BOOL'];

FILTER_OPERATIONS.DEFAULTS = {
  STRING: FILTER_OPERATIONS.STRING.INCLUDES,
  NUMBER: FILTER_OPERATIONS.NUMBER.INCLUDES,
  DATE: FILTER_OPERATIONS.DATE.EQUALS
};

export default FILTER_OPERATIONS;
