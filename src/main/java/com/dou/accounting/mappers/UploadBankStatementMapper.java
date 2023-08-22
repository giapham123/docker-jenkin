package com.dou.accounting.mappers;

import com.dou.accounting.models.UploadBankStatementModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UploadBankStatementMapper {

    int insertDataUploadBankStatement(UploadBankStatementModel uploadBankStatementModel);

    int deleteDataInBankStatement(@Param("statementDate") Date statementDate);

    int isExistTxnNo(@Param("txnNo")String txnNo, @Param("statementDate")Date statementDate);

    List<UploadBankStatementModel> getDataBankStatement(UploadBankStatementModel uploadBankStatementModel);

    int isExistAgreementId(@Param("appId")String appId);

    int countAllData(UploadBankStatementModel uploadBankStatementModel);

    List<UploadBankStatementModel> getDataBankStatementForExport(UploadBankStatementModel uploadBankStatementModel);


}
