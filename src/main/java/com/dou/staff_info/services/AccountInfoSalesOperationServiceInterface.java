package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoSalesOperation;

import java.util.List;

public interface AccountInfoSalesOperationServiceInterface {
    ResponseObject getAll(String supervisorId);
    ResponseObject getInfo(AccountInfoSalesOperation AccountInfoSalesOperation);
    ResponseObject insertInfo(AccountInfoSalesOperation AccountInfoSalesOperation);
    ResponseObject updateInfo(AccountInfoSalesOperation AccountInfoSalesOperation);
    ResponseObject deleteInfo(AccountInfoSalesOperation AccountInfoSalesOperation);

    ResponseObject deleteAccountInfoSalesOperation();
    ResponseObject insertAccountInfoSalesOperation_LastBackUp();
    ResponseObject deleteAccountInfoSalesOperation_LastBackUp();
    ResponseObject importListAccountInfoSalesOperation(List<AccountInfoSalesOperation> accountInfoDrs);
    ResponseObject updateListAccountInfoSalesOperation(List<AccountInfoSalesOperation> accountInfoDrs);
}
