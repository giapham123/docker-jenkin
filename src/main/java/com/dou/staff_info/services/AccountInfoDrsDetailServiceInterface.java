package com.dou.staff_info.services;


import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoDrsDetail;

public interface AccountInfoDrsDetailServiceInterface {
    ResponseObject getInfo(AccountInfoDrsDetail accountInfoDrsDetail);
    ResponseObject getAccount(AccountInfoDrsDetail accountInfoDrsDetail);
    ResponseObject updateInfo(JwtUser user, AccountInfoDrsDetail accountInfoDrsDetail);
    ResponseObject insertInfo(AccountInfoDrsDetail accountInfoDrsDetail);

}
