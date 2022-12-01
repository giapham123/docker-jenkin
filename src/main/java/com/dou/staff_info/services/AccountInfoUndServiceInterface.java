package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoUnd;

import java.util.List;

public interface AccountInfoUndServiceInterface {
    ResponseObject getAll(String supervisorId);
    ResponseObject getInfo(AccountInfoUnd AccountInfoUnd);
    ResponseObject insertInfo(AccountInfoUnd AccountInfoUnd);
    ResponseObject updateInfo(AccountInfoUnd AccountInfoUnd);
    ResponseObject deleteInfo(AccountInfoUnd AccountInfoUnd);

    ResponseObject deleteAccountInfoUnd();
    ResponseObject insertAccountInfoUnd_LastBackUp();
    ResponseObject deleteAccountInfoUnd_LastBackUp();
    ResponseObject importListAccountInfoUnd(List<AccountInfoUnd> accountInfoDrs);
    ResponseObject updateListAccountInfoUnd(List<AccountInfoUnd> accountInfoDrs);
}
