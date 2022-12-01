package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoAnti;
import com.dou.staff_info.services.AccountInfoAntiServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoAnti")
public class AccountInfoAntiController {

    @Autowired
    private AccountInfoAntiServiceInterface AccountInfoAntiServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoAnti>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoAntiServiceInterface.getAll(id);
    }

}
