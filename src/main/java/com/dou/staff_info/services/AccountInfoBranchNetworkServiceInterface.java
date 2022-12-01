package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoBranchNetwork;

import java.util.List;

public interface AccountInfoBranchNetworkServiceInterface {
    ResponseObject getAll(String supervisorId);
    ResponseObject getInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork);
    ResponseObject insertInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork);
    ResponseObject updateInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork);
    ResponseObject deleteInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork);

    ResponseObject deleteAccountInfoBranchNetwork();
    ResponseObject insertAccountInfoBranchNetwork_LastBackUp();
    ResponseObject deleteAccountInfoBranchNetwork_LastBackUp();
    ResponseObject importListAccountInfoBranchNetwork(List<AccountInfoBranchNetwork> accountInfoDrs);
    ResponseObject updateListAccountInfoBranchNetwork(List<AccountInfoBranchNetwork> accountInfoDrs);
}
