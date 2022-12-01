package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoDrs;

import java.util.List;

public interface AccountInfoDrsServiceInterface {
    ResponseObject getAll(String supervisorId);
    ResponseObject getInfo(AccountInfoDrs AccountInfoDrs);
    ResponseObject getInfoAccount(AccountInfoDrs AccountInfoDrs);
    ResponseObject insertInfo(AccountInfoDrs AccountInfoDrs);
    ResponseObject updateInfo(AccountInfoDrs AccountInfoDrs);
    ResponseObject deleteInfo(AccountInfoDrs AccountInfoDrs);

    ResponseObject deleteAccountInfoDrs();
    ResponseObject insertAccountInfoDrs_LastBackUp();
    ResponseObject deleteAccountInfoDrs_LastBackUp();
    ResponseObject importListAccountInfoDrs(List<AccountInfoDrs> accountInfoDrs);
    ResponseObject updateListAccountInfoDrs(List<AccountInfoDrs> accountInfoDrs);
}
