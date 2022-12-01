package com.dou.acctmanagement.services;

import com.dou.acctmanagement.models.AccountPermissionGroupFeature;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;

public interface AcctPermissionGroupFeatureServiceInterface {
    ResponseObject getAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature);
    ResponseObject loadAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature);
    ResponseObject insAccountPermissionGroupFeature(JwtUser user, AccountPermissionGroupFeature accountPermissionGroupFeature);
    ResponseObject delAccountPermissionGroupFeature(JwtUser user, AccountPermissionGroupFeature accountPermissionGroupFeature);
}
