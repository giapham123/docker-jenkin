package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountAccountant;
import com.dou.staff_info.services.AccountAccountantServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountAccountant")
public class AccountAccountantController {

    @Autowired
    private AccountAccountantServiceInterface AccountAccountantServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountAccountant>> get(@PathVariable("supervisorId") String id)  {
        return AccountAccountantServiceInterface.getAll(id);
    }

}
