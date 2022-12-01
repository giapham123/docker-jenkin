package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoThirdParty;

import java.util.List;

public interface AccountInfoThirdPartyServiceInterface {
    ResponseObject getAll(String supervisorId);
    ResponseObject getInfo(AccountInfoThirdParty AccountInfoThirdParty);
    ResponseObject insertInfo(AccountInfoThirdParty AccountInfoThirdParty);
    ResponseObject updateInfo(AccountInfoThirdParty AccountInfoThirdParty);
    ResponseObject deleteInfo(AccountInfoThirdParty AccountInfoThirdParty);

    ResponseObject deleteAccountInfoThirdParty();
    ResponseObject insertAccountInfoThirdParty_LastBackUp();
    ResponseObject deleteAccountInfoThirdParty_LastBackUp();
    ResponseObject importListAccountInfoThirdParty(List<AccountInfoThirdParty> accountInfoDrs);
    ResponseObject updateListAccountInfoThirdParty(List<AccountInfoThirdParty> accountInfoDrs);
}
