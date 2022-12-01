package com.dou.staff_info.controllers;

import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoDrs;
import com.dou.staff_info.services.AccountInfoDrsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/account/AccountInfoDrs")
public class AccountInfoDrsController {

    @Autowired
    private AccountInfoDrsServiceInterface AccountInfoDrsServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoDrs>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoDrsServiceInterface.getAll(id);
    }

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoDrs>> getInfo(@RequestBody AccountInfoDrs AccountInfoDrs)  {
        return AccountInfoDrsServiceInterface.getInfo(AccountInfoDrs);
    }

    @PostMapping("/getInfoAccount")
    public ResponseObject<List<AccountInfoDrs>> getInfoAccount(@RequestBody AccountInfoDrs AccountInfoDrs)  {
        return AccountInfoDrsServiceInterface.getInfoAccount(AccountInfoDrs);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoDrs AccountInfoDrs)  {
        return AccountInfoDrsServiceInterface.insertInfo(AccountInfoDrs);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestBody AccountInfoDrs AccountInfoDrs)  {
        return AccountInfoDrsServiceInterface.updateInfo(AccountInfoDrs);
    }

    @PutMapping("/deleteInfo")
    public ResponseObject deleteInfo(@RequestBody AccountInfoDrs AccountInfoDrs)  {
        return AccountInfoDrsServiceInterface.deleteInfo(AccountInfoDrs);
    }

    @PostMapping("/importList")
    public ResponseObject importList(@RequestBody List<AccountInfoDrs> AccountInfoDrs)  {
        return AccountInfoDrsServiceInterface.importListAccountInfoDrs(AccountInfoDrs);
    }

    @PutMapping("/updateList")
    public ResponseObject updateList(@RequestBody List<AccountInfoDrs> AccountInfoDrs)  {
        return AccountInfoDrsServiceInterface.updateListAccountInfoDrs(AccountInfoDrs);
    }

}
