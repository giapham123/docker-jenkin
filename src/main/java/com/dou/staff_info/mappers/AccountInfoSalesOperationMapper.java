package com.dou.staff_info.mappers;

import com.dou.staff_info.models.AccountInfoSalesOperation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountInfoSalesOperationMapper {
    List<AccountInfoSalesOperation> getAll(String supervisorId);
    List<AccountInfoSalesOperation> getInfo(AccountInfoSalesOperation AccountInfoSalesOperation);
    List<AccountInfoSalesOperation> checkExistAccountId(String accountID);
    void insertInfo(AccountInfoSalesOperation AccountInfoSalesOperation);
    void updateInfo(AccountInfoSalesOperation AccountInfoSalesOperation);
    void delete(AccountInfoSalesOperation AccountInfoSalesOperation);

    void deleteAccountInfoSalesOperation();
    void insertAccountInfoSalesOperation_LastBackUp();
    void deleteAccountInfoSalesOperation_LastBackUp();
    void importListAccountInfoSalesOperation(List<AccountInfoSalesOperation> accountInfoDrs);


}
