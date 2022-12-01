package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoUnd;
import com.dou.staff_info.services.AccountInfoUndServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoUnd")
public class AccountInfoUndController {

    @Autowired
    private AccountInfoUndServiceInterface AccountInfoUndServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoUnd>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoUndServiceInterface.getAll(id);
    }

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoUnd>> getInfo(@RequestBody AccountInfoUnd AccountInfoUnd)  {
        return AccountInfoUndServiceInterface.getInfo(AccountInfoUnd);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoUnd AccountInfoUnd)  {
        return AccountInfoUndServiceInterface.insertInfo(AccountInfoUnd);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestBody AccountInfoUnd AccountInfoUnd)  {
        return AccountInfoUndServiceInterface.updateInfo(AccountInfoUnd);
    }

    @PutMapping("/deleteInfo")
    public ResponseObject deleteInfo(@RequestBody AccountInfoUnd AccountInfoUnd)  {
        return AccountInfoUndServiceInterface.deleteInfo(AccountInfoUnd);
    }

    @PostMapping("/importList")
    public ResponseObject importList(@RequestBody List<AccountInfoUnd> AccountInfoUnd)  {
        return AccountInfoUndServiceInterface.importListAccountInfoUnd(AccountInfoUnd);
    }

    @PutMapping("/updateList")
    public ResponseObject updateList(@RequestBody List<AccountInfoUnd> AccountInfoUnd)  {
        return AccountInfoUndServiceInterface.updateListAccountInfoUnd(AccountInfoUnd);
    }

}
