package com.dou.acctmanagement.services;

import com.dou.acctmanagement.models.AccountInfo;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;

public interface AccountInfoServiceInterface {
    ResponseObject getAccountInfo(AccountInfo accountInfo);
    ResponseObject getAccountInfoSearch(AccountInfo accountInfo);
    ResponseObject getAccountInfoByAcctId(AccountInfo accountInfo);
    ResponseObject insAccountInfo(AccountInfo accountInfo);
    ResponseObject updAccountInfo(AccountInfo accountInfo);
    ResponseObject delAccountInfo(JwtUser user, AccountInfo accountInfo);
    ResponseObject delAccountInfo_GroupPermission(JwtUser user, AccountInfo accountInfo);
    ResponseObject resetPasswordAcctInfo(AccountInfo accountInfo);
}
