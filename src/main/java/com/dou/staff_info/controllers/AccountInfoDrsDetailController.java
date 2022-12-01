package com.dou.staff_info.controllers;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoDrsDetail;
import com.dou.staff_info.services.AccountInfoDrsDetailServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("api/account/AccountInfoDrsDetail")
public class AccountInfoDrsDetailController {

    @Autowired
    private AccountInfoDrsDetailServiceInterface accountInfoDrsDetailServiceInterface;

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoDrsDetail>> getInfo(@RequestBody AccountInfoDrsDetail accountInfoDrsDetail)  {
        return accountInfoDrsDetailServiceInterface.getInfo(accountInfoDrsDetail);
    }

    @PostMapping("/getAccount")
    public ResponseObject<List<AccountInfoDrsDetail>> getAccount(@RequestBody AccountInfoDrsDetail accountInfoDrsDetail)  {
        return accountInfoDrsDetailServiceInterface.getAccount(accountInfoDrsDetail);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestAttribute(value = REQ_USR, required = false) JwtUser user,@RequestBody AccountInfoDrsDetail accountInfoDrsDetail)  {
        return accountInfoDrsDetailServiceInterface.updateInfo(user ,accountInfoDrsDetail);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoDrsDetail accountInfoDrsDetail)  {
        return accountInfoDrsDetailServiceInterface.insertInfo(accountInfoDrsDetail);
    }


}
