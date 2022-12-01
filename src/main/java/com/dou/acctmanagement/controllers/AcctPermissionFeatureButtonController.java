package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.AccountPermissionFeatureButton;
import com.dou.acctmanagement.models.GrantPermissionHis;
import com.dou.acctmanagement.services.AcctPermissionFeatureButtonServiceInterface;
import com.dou.acctmanagement.services.GrantPermissionHisServiceInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("api/acctpermissionfeaturebutton")
public class AcctPermissionFeatureButtonController {

    @Autowired
    private AcctPermissionFeatureButtonServiceInterface acctPermissionFeatureButtonServiceInterface;

    @Autowired
    private GrantPermissionHisServiceInterface grantPermissionHisServiceInterface;

    @PostMapping("/getAcctPermissionFeatureButton")
    public ResponseObject<List<AccountPermissionFeatureButton>> getAcctPermissionFeatureButton(@RequestBody AccountPermissionFeatureButton searchVal) {
        return acctPermissionFeatureButtonServiceInterface.getAcctPermissionFeatureButton(searchVal);
    }

    @PostMapping("/insAcctPermissionFeatureButton")
    public ResponseObject insAcctPermissionFeatureButton(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountPermissionFeatureButton searchVal)  {
        ResponseObject responseObject = acctPermissionFeatureButtonServiceInterface.insAcctPermissionFeatureButton(searchVal);
        if (responseObject.isSuccess() == true) {
            GrantPermissionHis grantPermissionHis = new GrantPermissionHis();
            grantPermissionHis.setUserCreate(user.getUsername());
            grantPermissionHis.setAccountId(searchVal.getAccountId());
            grantPermissionHis.setFeatureCodeId((searchVal.getFeatureCodeId()));
            grantPermissionHis.setAction("INSERT");
            responseObject = grantPermissionHisServiceInterface.insGrantPermissionHis(grantPermissionHis);
        }
        return responseObject;
    }

    @PostMapping("/updAcctPermissionFeatureButton")
    public ResponseObject updAcctPermissionFeatureButton(@RequestBody AccountPermissionFeatureButton searchVal)  {
        return acctPermissionFeatureButtonServiceInterface.updAcctPermissionFeatureButton(searchVal);
    }

    @PostMapping("/delAcctPermissionFeatureButton")
    public ResponseObject delAcctPermissionFeatureButton(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountPermissionFeatureButton searchVal)  {
        ResponseObject responseObject = acctPermissionFeatureButtonServiceInterface.delAcctPermissionFeatureButton(searchVal);
        if (responseObject.isSuccess() == true) {
            GrantPermissionHis grantPermissionHis = new GrantPermissionHis();
            grantPermissionHis.setUserCreate(user.getUsername());
            grantPermissionHis.setAccountId(searchVal.getAccountId());
            grantPermissionHis.setFeatureCodeId((searchVal.getFeatureCodeId()));
            grantPermissionHis.setAction("DELETE");
            responseObject = grantPermissionHisServiceInterface.insGrantPermissionHis(grantPermissionHis);
        }
        return responseObject;
    }
}
