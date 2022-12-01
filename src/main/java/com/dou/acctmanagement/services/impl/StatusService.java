package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.StatusMapper;
import com.dou.acctmanagement.models.Status;
import com.dou.acctmanagement.services.StatusServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService implements StatusServiceInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    @Autowired
    private StatusMapper statusMapper;

    public ResponseObject<List<Status>> getAllStatus() {
        List<Status> listStatus = statusMapper.getAllStatus();
        return new ResponseObject(listStatus);
    }
}
