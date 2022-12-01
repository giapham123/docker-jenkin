package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.AccountInfoMapper;
import com.dou.acctmanagement.mappers.AccountPermissionGroupFeatureMapper;
import com.dou.acctmanagement.mappers.GrantPermissionHisMapper;
import com.dou.acctmanagement.mappers.GroupFeatureMapper;
import com.dou.acctmanagement.models.AccountInfo;
import com.dou.acctmanagement.models.AccountPermissionGroupFeature;
import com.dou.acctmanagement.models.GrantPermissionHis;
import com.dou.acctmanagement.models.GroupFeature;
import com.dou.acctmanagement.services.AcctPermissionGroupFeatureServiceInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcctPermissionGroupFeatureService implements AcctPermissionGroupFeatureServiceInterface {

    private final static Logger LOGGER = LoggerFactory.getLogger(AcctPermissionGroupFeatureService.class);

    @Autowired
    private AccountPermissionGroupFeatureMapper accountPermissionGroupFeatureMapper;

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private GrantPermissionHisMapper grantPermissionHisMapper;

    @Autowired
    private GroupFeatureMapper groupFeatureMapper;

    public ResponseObject<List<AccountPermissionGroupFeature>> getAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature) {
        List<AccountPermissionGroupFeature> listAcctPermissGroupFeat = accountPermissionGroupFeatureMapper.getAccountPermissionGroupFeature(accountPermissionGroupFeature);
        return new ResponseObject(listAcctPermissGroupFeat);
    }

    public ResponseObject<List<AccountPermissionGroupFeature>> loadAccountPermissionGroupFeature(AccountPermissionGroupFeature accountPermissionGroupFeature) {
        List<AccountPermissionGroupFeature> listAcctPermissGroupFeat = accountPermissionGroupFeatureMapper.loadAccountPermissionGroupFeature(accountPermissionGroupFeature);
        return new ResponseObject(listAcctPermissGroupFeat);
    }

    @Transactional
    public ResponseObject insAccountPermissionGroupFeature(JwtUser user, AccountPermissionGroupFeature accountPermissionGroupFeature) {
        List<AccountPermissionGroupFeature> listAcctPermissGroupFeat = accountPermissionGroupFeatureMapper.getAccountPermissionGroupFeature(accountPermissionGroupFeature);
        if (listAcctPermissGroupFeat.size() != 0) {
            return new ResponseObject(listAcctPermissGroupFeat);
        } else {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountId(accountPermissionGroupFeature.getAccountId());
            List<AccountInfo> listAcctInfo = accountInfoMapper.getAccountInfoByAcctId(accountInfo);
            if (listAcctInfo.size() == 0) {
                LOGGER.error("This User Account is not existed.");
                return new ResponseObject(false, "This User Account is not existed");
            }
            try {
                accountPermissionGroupFeatureMapper.insAccountPermissionGroupFeature(accountPermissionGroupFeature);
                List<GroupFeature> listGroupFeature = groupFeatureMapper.getGroupFeatureByGroupId_01(accountPermissionGroupFeature.getGroupCodeId());
                if (listGroupFeature.size() == 0) {
                    LOGGER.error("Data error.");
                }
                for (GroupFeature i : listGroupFeature) {
                    GrantPermissionHis grantPermissionHis = new GrantPermissionHis();
                    grantPermissionHis.setUserCreate(user.getUsername());
                    grantPermissionHis.setAccountId(accountPermissionGroupFeature.getAccountId());
                    grantPermissionHis.setAction("INSERT");
                    grantPermissionHis.setFeatureCodeId(i.getFeatureId());
                    grantPermissionHisMapper.insGrantPermissionHis(grantPermissionHis);
                }
                return ResponseObject.INSERT_DATA_SUCCESS;
            } catch (Exception e) {
                LOGGER.error("Error when insert: " + e.toString());
                return ResponseObject.INSERT_DATA_FAIL;
            }
        }
    }

    @Transactional
    public ResponseObject delAccountPermissionGroupFeature(JwtUser user, AccountPermissionGroupFeature accountPermissionGroupFeature) {
        try {
            accountPermissionGroupFeatureMapper.delAccountPermissionGroupFeature(accountPermissionGroupFeature);
            List<GroupFeature> listGroupFeature = groupFeatureMapper.getGroupFeatureByGroupId_01(accountPermissionGroupFeature.getGroupCodeId());
            if (listGroupFeature.size() == 0) {
                LOGGER.error("Data error.");
            }
            for (GroupFeature i : listGroupFeature) {
                GrantPermissionHis grantPermissionHis = new GrantPermissionHis();
                grantPermissionHis.setUserCreate(user.getUsername());
                grantPermissionHis.setAccountId(accountPermissionGroupFeature.getAccountId());
                grantPermissionHis.setAction("DELETE");
                grantPermissionHis.setFeatureCodeId(i.getFeatureId());
                grantPermissionHisMapper.insGrantPermissionHis(grantPermissionHis);
            }
            return ResponseObject.DELETE_DATA_SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error when delete: " + e.toString());
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
}
