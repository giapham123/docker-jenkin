package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoRisk;
import com.dou.staff_info.services.AccountInfoRiskServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoRisk")
public class AccountInfoRiskController {

    @Autowired
    private AccountInfoRiskServiceInterface AccountInfoRiskServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoRisk>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoRiskServiceInterface.getAll(id);
    }

}
