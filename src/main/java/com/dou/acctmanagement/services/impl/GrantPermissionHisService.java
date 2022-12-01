package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.GrantPermissionHisMapper;
import com.dou.acctmanagement.models.GrantPermissionHis;
import com.dou.acctmanagement.services.GrantPermissionHisServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrantPermissionHisService implements GrantPermissionHisServiceInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(GrantPermissionHisService.class);

    @Autowired
    private GrantPermissionHisMapper grantPermissionHisMapper;

    @Transactional
    public ResponseObject insGrantPermissionHis(GrantPermissionHis grantPermissionHis){
        try {
            grantPermissionHisMapper.insGrantPermissionHis(grantPermissionHis);
            return ResponseObject.INSERT_DATA_SUCCESS;
        }
        catch(Exception e) {
            LOGGER.error("Error insert: " + e.toString());
            return ResponseObject.INSERT_DATA_FAIL;
        }

    }
}
