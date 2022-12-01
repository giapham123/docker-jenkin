package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoCallCenter;
import com.dou.staff_info.services.AccountInfoCallCenterServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoCallCenter")
public class AccountInfoCallCenterController {

    @Autowired
    private AccountInfoCallCenterServiceInterface AccountInfoCallCenterServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoCallCenter>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoCallCenterServiceInterface.getAll(id);
    }

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoCallCenter>> getInfo(@RequestBody AccountInfoCallCenter AccountInfoCallCenter)  {
        return AccountInfoCallCenterServiceInterface.getInfo(AccountInfoCallCenter);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoCallCenter AccountInfoCallCenter)  {
        return AccountInfoCallCenterServiceInterface.insertInfo(AccountInfoCallCenter);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestBody AccountInfoCallCenter AccountInfoCallCenter)  {
        return AccountInfoCallCenterServiceInterface.updateInfo(AccountInfoCallCenter);
    }

    @PutMapping("/deleteInfo")
    public ResponseObject deleteInfo(@RequestBody AccountInfoCallCenter AccountInfoCallCenter)  {
        return AccountInfoCallCenterServiceInterface.deleteInfo(AccountInfoCallCenter);
    }

    @PostMapping("/importList")
    public ResponseObject importList(@RequestBody List<AccountInfoCallCenter> AccountInfoCallCenter)  {
        return AccountInfoCallCenterServiceInterface.importListAccountInfoCallCenter(AccountInfoCallCenter);
    }

    @PutMapping("/updateList")
    public ResponseObject updateList(@RequestBody List<AccountInfoCallCenter> AccountInfoCallCenter)  {
        return AccountInfoCallCenterServiceInterface.updateListAccountInfoCallCenter(AccountInfoCallCenter);
    }

}
