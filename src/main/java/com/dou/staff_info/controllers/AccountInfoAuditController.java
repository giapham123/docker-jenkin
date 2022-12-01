package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoAudit;
import com.dou.staff_info.services.AccountInfoAuditServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoAudit")
public class AccountInfoAuditController {

    @Autowired
    private AccountInfoAuditServiceInterface AccountInfoAuditServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoAudit>> get(@PathVariable("supervisorId") String id) {
        return AccountInfoAuditServiceInterface.getAll(id);
    }

}
