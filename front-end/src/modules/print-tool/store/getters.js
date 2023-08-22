import { isArray, isEmpty } from 'lodash';
import { GROUP_TEMPLATE } from '../constants';

export const allTemplates = state => {
  const { print_templates } = state;
  if (isArray(print_templates)) return print_templates;
  return [];
};

export const bankTemplates = state => {
  const templates = allTemplates(state).filter(
    template => template.group == GROUP_TEMPLATE.BANK
  );
  let banks = new Map();
  templates.forEach(template => {
    banks.set(template.bankCode, {
      value: template.bankCode,
      text: `${template.bankCode} - ${template.bankName}`
    });
  });
  return Array.from(banks.values());
};

export const loanTemplates = state => {
  return allTemplates(state).filter(
    template => template.group == GROUP_TEMPLATE.LOAN
  );
};

export const otherTemplates = state => {
  return allTemplates(state).filter(
    template => template.group == GROUP_TEMPLATE.OTHE
  );
};

export const isLoaded = state => {
  const { print_templates } = state;
  if (isArray(print_templates)) return !isEmpty(print_templates);
  return false;
};
