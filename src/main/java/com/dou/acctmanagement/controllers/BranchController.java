package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.services.BranchServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/branch")
public class BranchController {

    @Autowired
    private BranchServiceInterface branchServiceInterface;

    @RequestMapping("/getAllBranch")
    public ResponseObject getAllBranch() {
        return branchServiceInterface.getAllBranch();
    }

    @RequestMapping("/getAllRequestType")
    public ResponseObject getAllRequestType() {
        return branchServiceInterface.getAllRequestType();
    }
}
