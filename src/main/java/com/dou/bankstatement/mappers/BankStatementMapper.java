package com.dou.bankstatement.mappers;

import com.dou.bankstatement.models.BankStatementModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BankStatementMapper {
    List<BankStatementModel> getDataBankStatement(BankStatementModel model);

    List<String> getBank();
}
