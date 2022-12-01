package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.AccountPermissionGroupFeature;
import com.dou.acctmanagement.services.AcctPermissionGroupFeatureServiceInterface;
import com.dou.acctmanagement.services.GrantPermissionHisServiceInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("api/acctpermissiongroupfeature")
public class AcctPermissionGroupFeatureController {

    @Autowired
    private AcctPermissionGroupFeatureServiceInterface acctPermissionGroupFeatureServiceInterface;

    @Autowired
    private GrantPermissionHisServiceInterface grantPermissionHisServiceInterface;

    @PostMapping("/loadAcctPermissGroupFeat")
    public ResponseObject loadAcctPermissGroupFeat(@RequestBody AccountPermissionGroupFeature accountPermissionGroupFeature) {
        return  acctPermissionGroupFeatureServiceInterface.loadAccountPermissionGroupFeature(accountPermissionGroupFeature);
    }

    @PostMapping("/insAcctPermissGroupFeat")
    public ResponseObject insAcctPermissGroupFeat(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountPermissionGroupFeature accountPermissionGroupFeature) {
        return acctPermissionGroupFeatureServiceInterface.insAccountPermissionGroupFeature(user, accountPermissionGroupFeature);
    }

    @PostMapping("/delAcctPermissGroupFeat")
    public ResponseObject delAcctPermissGroupFeat(@RequestAttribute(value = REQ_USR, required = false) JwtUser user, @RequestBody AccountPermissionGroupFeature accountPermissionGroupFeature) {
        return acctPermissionGroupFeatureServiceInterface.delAccountPermissionGroupFeature(user, accountPermissionGroupFeature);
    }
}
