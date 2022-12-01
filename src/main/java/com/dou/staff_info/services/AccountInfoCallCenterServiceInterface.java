package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoCallCenter;

import java.util.List;

public interface AccountInfoCallCenterServiceInterface {
    ResponseObject getAll(String supervisorId);
    ResponseObject getInfo(AccountInfoCallCenter AccountInfoCallCenter);
    ResponseObject insertInfo(AccountInfoCallCenter AccountInfoCallCenter);
    ResponseObject updateInfo(AccountInfoCallCenter AccountInfoCallCenter);
    ResponseObject deleteInfo(AccountInfoCallCenter AccountInfoCallCenter);

    ResponseObject deleteAccountInfoCallCenter();
    ResponseObject insertAccountInfoCallCenter_LastBackUp();
    ResponseObject deleteAccountInfoCallCenter_LastBackUp();
    ResponseObject importListAccountInfoCallCenter(List<AccountInfoCallCenter> accountInfoDrs);
    ResponseObject updateListAccountInfoCallCenter(List<AccountInfoCallCenter> accountInfoDrs);
}
