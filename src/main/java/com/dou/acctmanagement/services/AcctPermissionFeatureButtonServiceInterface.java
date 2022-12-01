package com.dou.acctmanagement.services;

import com.dou.acctmanagement.models.AccountPermissionFeatureButton;
import com.dou.adm.shared.ResponseObject;

public interface AcctPermissionFeatureButtonServiceInterface {
    ResponseObject getAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    ResponseObject insAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    ResponseObject updAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    ResponseObject delAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    ResponseObject delAccountPermissionFeatureButton_01(String accountId);
}
