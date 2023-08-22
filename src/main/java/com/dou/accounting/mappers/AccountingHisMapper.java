package com.dou.accounting.mappers;

import com.dou.accounting.models.AccountingHisModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountingHisMapper {

    List<AccountingHisModel> getAccountingData(AccountingHisModel accountingHisModel);

    int countAllDataInTerminal(AccountingHisModel accountingHisModel);

    List<AccountingHisModel> getAccountingDataForExport(AccountingHisModel accountingHisModel);
}
