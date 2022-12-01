package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.AccountInfoMapper;
import com.dou.acctmanagement.mappers.AccountPermissionFeatureButtonMapper;
import com.dou.acctmanagement.models.AccountInfo;
import com.dou.acctmanagement.models.AccountPermissionFeatureButton;
import com.dou.acctmanagement.services.AcctPermissionFeatureButtonServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcctPermissionFeatureButtonService implements AcctPermissionFeatureButtonServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(AcctPermissionFeatureButtonService.class);

    @Autowired
    private AccountPermissionFeatureButtonMapper accountPermissionFeatureButtonMapper;

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    public ResponseObject<List<AccountPermissionFeatureButton>> getAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton) {
        List<AccountPermissionFeatureButton> listAcctPermissFeatButton = accountPermissionFeatureButtonMapper.getAccountPermissionFeatureButton(acctPermissFeatButton);
        return new ResponseObject(listAcctPermissFeatButton);
    }

    @Transactional
    public ResponseObject insAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton) {
        List<AccountPermissionFeatureButton> listAcctPermissFeatButton = accountPermissionFeatureButtonMapper.getAccountPermissionFeatureButton(acctPermissFeatButton);
        if (listAcctPermissFeatButton.size() != 0) {
            return new ResponseObject(listAcctPermissFeatButton);
        }
        else {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountId(acctPermissFeatButton.getAccountId());
            List<AccountInfo> listAcctInfo = accountInfoMapper.getAccountInfoByAcctId(accountInfo);
            if (listAcctInfo.size() == 0) {
                return ResponseObject.INSERT_DATA_FAIL;
            }
            else {
                try {
                    accountPermissionFeatureButtonMapper.insAccountPermissionFeatureButton(acctPermissFeatButton);
                    return ResponseObject.INSERT_DATA_SUCCESS;
                } catch (Exception e) {
                    LOGGER.error("Error insert: " + e.toString());
                    return ResponseObject.INSERT_DATA_FAIL;
                }
            }
        }
    }

    @Transactional
    public ResponseObject updAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton) {
        try {
            accountPermissionFeatureButtonMapper.updAccountPermissionFeatureButton(acctPermissFeatButton);
            return ResponseObject.UPDATE_DATA_SUCCESS;
        }
        catch (Exception e) {
            LOGGER.error("Error update: " + e.toString());
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject delAcctPermissionFeatureButton(AccountPermissionFeatureButton acctPermissFeatButton) {
        try {
            accountPermissionFeatureButtonMapper.delAccountPermissionFeatureButton(acctPermissFeatButton);
            return ResponseObject.DELETE_DATA_SUCCESS;
        }
        catch (Exception e) {
            LOGGER.error("Error delete: " + e.toString());
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject delAccountPermissionFeatureButton_01(String accountId) {
        try {
            accountPermissionFeatureButtonMapper.delAccountPermissionFeatureButton_01(accountId);
            return ResponseObject.DELETE_DATA_SUCCESS;
        }
        catch (Exception e) {
            LOGGER.error("Error delete: " + e.toString());
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
}
