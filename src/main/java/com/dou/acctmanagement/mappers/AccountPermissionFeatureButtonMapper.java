package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.AccountPermissionFeatureButton;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountPermissionFeatureButtonMapper {
    List<AccountPermissionFeatureButton> getAccountPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    List<AccountPermissionFeatureButton> getAccountPermissionFeatureButtonByAcct(String accountId);
    void insAccountPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    void updAccountPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    void delAccountPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton);
    void delAccountPermissionFeatureButton_01(String accountId);
}
