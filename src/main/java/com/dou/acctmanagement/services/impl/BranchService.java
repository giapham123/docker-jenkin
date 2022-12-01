package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.BranchMapper;
import com.dou.acctmanagement.models.Branch;
import com.dou.acctmanagement.models.RequestType;
import com.dou.acctmanagement.services.BranchServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService implements BranchServiceInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(BranchService.class);

    @Autowired
    private BranchMapper branchMapper;

    public ResponseObject<List<Branch>> getAllBranch() {
        List<Branch> listBranch = branchMapper.getAllBranch();
        return new ResponseObject(listBranch);
    }

    public ResponseObject<List<RequestType>> getAllRequestType() {
        List<RequestType> requestTypes = branchMapper.getAllRequestType();
        return new ResponseObject(requestTypes);
    }
}
