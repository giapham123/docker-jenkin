package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.AccountInfoMapper;
import com.dou.acctmanagement.mappers.AccountPermissionFeatureButtonMapper;
import com.dou.acctmanagement.mappers.AccountPermissionGroupFeatureMapper;
import com.dou.acctmanagement.mappers.GrantPermissionHisMapper;
import com.dou.acctmanagement.models.*;
import com.dou.acctmanagement.services.AccountInfoServiceInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountInfoService implements AccountInfoServiceInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountInfoService.class);

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private AccountPermissionFeatureButtonMapper accountPermissionFeatureButtonMapper;

    @Autowired
    private AccountPermissionGroupFeatureMapper accountPermissionGroupFeatureMapper;

    @Autowired
    private GrantPermissionHisMapper grantPermissionHisMapper;

    public ResponseObject<List<AccountInfo>> getAccountInfo(AccountInfo accountInfo) {
        List<AccountInfo> listAcctInfo = accountInfoMapper.getAccountInfo(accountInfo);
        return new ResponseObject(listAcctInfo);
    }

    public ResponseObject<List<AccountInfoSearch>> getAccountInfoSearch(AccountInfo accountInfo) {
        if (accountInfo.getDepartmentId() != null && !accountInfo.getDepartmentId().equals("")) {
            List<AccountInfoSearch> listAcctInfoSearch = accountInfoMapper.getAccountInfoSearch(accountInfo);
            return new ResponseObject(listAcctInfoSearch);
        } else {
            List<AccountInfo> listAcctInfo = accountInfoMapper.getAccountInfoByAcctId(accountInfo);
            return new ResponseObject(listAcctInfo);
        }
    }

    public ResponseObject<List<AccountInfo>> getAccountInfoByAcctId(AccountInfo accountInfo) {
        List<AccountInfo> listAcctInfo = accountInfoMapper.getAccountInfoByAcctId(accountInfo);
        return new ResponseObject(listAcctInfo);
    }


    @Transactional
    public ResponseObject insAccountInfo(AccountInfo accountInfo) {
        accountInfo.setPassword("$2a$12$390d4WyJbVCWsblrOcpe.Oyocnj2rIm1adFRdCeKTA39MAS0x5Owm");
        List<AccountInfo> listAcctInfo = accountInfoMapper.getAccountInfoByAcctId(accountInfo);
        if (listAcctInfo.size() != 0) {
            return new ResponseObject(listAcctInfo);
        } else {
            try {
                accountInfoMapper.insAccountInfo(accountInfo);
                return ResponseObject.INSERT_DATA_SUCCESS;
            } catch (Exception e) {
                LOGGER.error("Error insert: " + e.toString());
                return ResponseObject.INSERT_DATA_FAIL;
            }
        }
    }

    @Transactional
    public ResponseObject updAccountInfo(AccountInfo accountInfo) {
        try {
            accountInfoMapper.updAccountInfo(accountInfo);
            return ResponseObject.UPDATE_DATA_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error update: " + e.toString());
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject delAccountInfo(JwtUser user, AccountInfo accountInfo) {
        try {
            accountInfoMapper.delAccountInfo(accountInfo);
            List<AccountPermissionFeatureButton> listAcctPermissionFeature = accountPermissionFeatureButtonMapper.getAccountPermissionFeatureButtonByAcct(accountInfo.getAccountId());
            if (listAcctPermissionFeature.size() != 0) {
                accountPermissionFeatureButtonMapper.delAccountPermissionFeatureButton_01(accountInfo.getAccountId());
                GrantPermissionHis grantPermissionHis = new GrantPermissionHis();
                grantPermissionHis.setUserCreate(user.getUsername());
                grantPermissionHis.setAccountId(accountInfo.getAccountId());
                grantPermissionHis.setAction("DELETE");
                for (AccountPermissionFeatureButton i : listAcctPermissionFeature) {
                    grantPermissionHis.setFeatureCodeId((i.getFeatureCodeId()));
                    grantPermissionHisMapper.insGrantPermissionHis(grantPermissionHis);
                }
            }
            return ResponseObject.DELETE_DATA_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error delete: " + e.toString());
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject delAccountInfo_GroupPermission(JwtUser user, AccountInfo accountInfo) {
        try {
            accountInfoMapper.delAccountInfo(accountInfo);
            AccountPermissionGroupFeature accountPermissionGroupFeature = new AccountPermissionGroupFeature();
            accountPermissionGroupFeature.setAccountId(accountInfo.getAccountId());
            List<AccountPermissionGroupFeature> listAcctGroupPermissFeat = accountPermissionGroupFeatureMapper.loadAccountPermissionGroupFeature(accountPermissionGroupFeature);
            if (listAcctGroupPermissFeat.size() != 0) {
                accountPermissionGroupFeatureMapper.delAccountPermissionGroupFeatureByAcct(accountInfo.getAccountId());
                for (AccountPermissionGroupFeature i : listAcctGroupPermissFeat) {
                    GrantPermissionHis grantPermissionHis = new GrantPermissionHis();
                    grantPermissionHis.setUserCreate(user.getUsername());
                    grantPermissionHis.setAccountId(accountInfo.getAccountId());
                    grantPermissionHis.setAction("DELETE");
                    grantPermissionHis.setFeatureCodeId((i.getFeatureCodeId()));
                    grantPermissionHisMapper.insGrantPermissionHis(grantPermissionHis);
                }
            }
            return ResponseObject.DELETE_DATA_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error delete: " + e.toString());
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject resetPasswordAcctInfo(AccountInfo accountInfo) {
        List<AccountInfo> listAcctInfo = accountInfoMapper.getAccountInfoByAcctId(accountInfo);
        if (listAcctInfo.size() > 0) {
            try {
                accountInfo.setPassword("$2a$12$390d4WyJbVCWsblrOcpe.Oyocnj2rIm1adFRdCeKTA39MAS0x5Owm");
                accountInfoMapper.resetPasswordAcctInfo(accountInfo);
                return ResponseObject.UPDATE_DATA_SUCCESS;
            } catch (Exception e) {
                LOGGER.error("Error reset password: " + e.toString());
                return ResponseObject.UPDATE_DATA_FAIL;
            }
        } else {
            LOGGER.error("No accountId for resetting password");
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }
}
