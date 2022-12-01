package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoBussiness;
import com.dou.staff_info.services.AccountInfoBussinessServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoBussiness")
public class AccountInfoBussinessController {

    @Autowired
    private AccountInfoBussinessServiceInterface AccountInfoBussinessServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoBussiness>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoBussinessServiceInterface.getAll(id);
    }

}
