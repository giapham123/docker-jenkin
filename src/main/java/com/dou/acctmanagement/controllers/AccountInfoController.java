package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.AccountInfo;
import com.dou.acctmanagement.models.AccountInfoSearch;
import com.dou.acctmanagement.services.AccountInfoServiceInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("api/acctinfo")
public class AccountInfoController {

    @Autowired
    private AccountInfoServiceInterface accountInfoServiceInterface;

    @PostMapping("/getAcctInfo")
    public ResponseObject<List<AccountInfo>> getAcctInfo(@RequestBody AccountInfo accountInfo)  {
        return accountInfoServiceInterface.getAccountInfo(accountInfo);
    }

    @PostMapping("/getAccountInfoSearch")
    public ResponseObject<List<AccountInfoSearch>> getAccountInfoSearch(@RequestBody AccountInfo accountInfo) {
        return accountInfoServiceInterface.getAccountInfoSearch(accountInfo);
    }

    @PostMapping("/insAcctInfo")
    public ResponseObject insAcctInfo(@RequestBody AccountInfo accountInfo)  {
        return accountInfoServiceInterface.insAccountInfo(accountInfo);
    }

    @PostMapping("/updAcctInfo")
    public ResponseObject updAcctInfo(@RequestBody AccountInfo accountInfo)  {
        return accountInfoServiceInterface.updAccountInfo(accountInfo);
    }

    @PostMapping("/delAcctInfo")
    public ResponseObject delAcctInfo(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountInfo accountInfo)  {
        return accountInfoServiceInterface.delAccountInfo(user, accountInfo);
    }

    @PostMapping("/delAcctInfoGroup")
    public ResponseObject delAcctInfoGroup(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountInfo accountInfo)  {
        return accountInfoServiceInterface.delAccountInfo_GroupPermission(user, accountInfo);
    }

    @PostMapping("/resetPass")
    public ResponseObject resetPassAcctInfo(@RequestBody AccountInfo accountInfo) {
        return accountInfoServiceInterface.resetPasswordAcctInfo(accountInfo);
    }
}
