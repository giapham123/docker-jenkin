package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.AccountPermissionGroupFeature;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountPermissionGroupFeatureMapper {
    List<AccountPermissionGroupFeature> getAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature);
    List<AccountPermissionGroupFeature> getAccountPermissionGroupFeatureByAcct(String accountId);
    List<AccountPermissionGroupFeature> loadAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature);
    void insAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature);
    void delAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature);
    void delAccountPermissionGroupFeatureByAcct(String accountId);
}
