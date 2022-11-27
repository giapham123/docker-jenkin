package com.dou.accounting.services;

import com.dou.accounting.models.AccountingHisModel;
import com.dou.accounting.models.AccountingModel;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;

import java.util.List;
import java.util.Map;

public interface AccountingHisInterface {
    Map getListDataAccountingHis(AccountingHisModel accountingHisModel);

    byte[] exportAccountingHisData(AccountingHisModel accountingHisModel);
}