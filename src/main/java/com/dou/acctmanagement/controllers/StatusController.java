package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.services.StatusServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/status")
public class StatusController {

    @Autowired
    private StatusServiceInterface statusServiceInterface;

    @RequestMapping("/getAllStatus")
    public ResponseObject getAllStatus() {
        return statusServiceInterface.getAllStatus();
    }
}
