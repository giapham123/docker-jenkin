package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoTeleSale;

import java.util.List;

public interface AccountInfoTeleSaleServiceInterface {
    ResponseObject getAll(String supervisorId);
    ResponseObject getInfo(AccountInfoTeleSale AccountInfoTeleSale);
    ResponseObject insertInfo(AccountInfoTeleSale AccountInfoTeleSale);
    ResponseObject updateInfo(AccountInfoTeleSale AccountInfoTeleSale);
    ResponseObject deleteInfo(AccountInfoTeleSale AccountInfoTeleSale);

    ResponseObject deleteAccountInfoTeleSale();
    ResponseObject insertAccountInfoTeleSale_LastBackUp();
    ResponseObject deleteAccountInfoTeleSale_LastBackUp();
    ResponseObject importListAccountInfoTeleSale(List<AccountInfoTeleSale> accountInfoDrs);
    ResponseObject updateListAccountInfoTeleSale(List<AccountInfoTeleSale> accountInfoDrs);
}
