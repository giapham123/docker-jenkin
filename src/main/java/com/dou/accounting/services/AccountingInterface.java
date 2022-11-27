package com.dou.accounting.services;

import com.dou.accounting.models.AccountingModel;

import java.util.List;
import java.util.Map;

public interface AccountingInterface {
    Map getListDataAccounting(AccountingModel accountingModel);

    byte[] exportAccountingData(AccountingModel accountingModel);

}
