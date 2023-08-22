package com.dou.accounting.services;

import com.dou.accounting.models.AccountingHisModel;

import java.util.Map;

public interface AccountingHisInterface {
    Map getListDataAccountingHis(AccountingHisModel accountingHisModel);

    byte[] exportAccountingHisData(AccountingHisModel accountingHisModel);
}