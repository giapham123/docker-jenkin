package com.dou.bankstatement.mappers;

import com.dou.bankstatement.models.BankStatementModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FTPMapper {
    void bankStatement(BankStatementModel model);
}
