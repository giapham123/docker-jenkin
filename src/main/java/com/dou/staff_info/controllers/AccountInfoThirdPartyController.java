package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoThirdParty;
import com.dou.staff_info.services.AccountInfoThirdPartyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoThirdParty")
public class AccountInfoThirdPartyController {

    @Autowired
    private AccountInfoThirdPartyServiceInterface AccountInfoThirdPartyServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoThirdParty>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoThirdPartyServiceInterface.getAll(id);
    }

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoThirdParty>> getInfo(@RequestBody AccountInfoThirdParty AccountInfoThirdParty)  {
        return AccountInfoThirdPartyServiceInterface.getInfo(AccountInfoThirdParty);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoThirdParty AccountInfoThirdParty)  {
        return AccountInfoThirdPartyServiceInterface.insertInfo(AccountInfoThirdParty);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestBody AccountInfoThirdParty AccountInfoThirdParty)  {
        return AccountInfoThirdPartyServiceInterface.updateInfo(AccountInfoThirdParty);
    }

    @PutMapping("/deleteInfo")
    public ResponseObject deleteInfo(@RequestBody AccountInfoThirdParty AccountInfoThirdParty)  {
        return AccountInfoThirdPartyServiceInterface.deleteInfo(AccountInfoThirdParty);
    }

    @PostMapping("/importList")
    public ResponseObject importList(@RequestBody List<AccountInfoThirdParty> AccountInfoThirdParty)  {
        return AccountInfoThirdPartyServiceInterface.importListAccountInfoThirdParty(AccountInfoThirdParty);
    }

    @PutMapping("/updateList")
    public ResponseObject updateList(@RequestBody List<AccountInfoThirdParty> AccountInfoThirdParty)  {
        return AccountInfoThirdPartyServiceInterface.updateListAccountInfoThirdParty(AccountInfoThirdParty);
    }

}
