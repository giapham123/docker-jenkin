package com.dou.accounting.mappers;

import com.dou.accounting.models.AccountingModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountingMapper {

    List<AccountingModel> getAccountingData(AccountingModel accountingModel);

    int getProcTermiFinish();

    int countAllDataInTerminal(AccountingModel accountingModel);

    List<AccountingModel> getAccountingDataForExport(AccountingModel accountingModel);
}
