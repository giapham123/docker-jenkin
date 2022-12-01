package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoBranchNetwork;
import com.dou.staff_info.services.AccountInfoBranchNetworkServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoBranchNetwork")
public class AccountInfoBranchNetworkController {

    @Autowired
    private AccountInfoBranchNetworkServiceInterface AccountInfoBranchNetworkServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoBranchNetwork>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoBranchNetworkServiceInterface.getAll(id);
    }

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoBranchNetwork>> getInfo(@RequestBody AccountInfoBranchNetwork AccountInfoBranchNetwork)  {
        return AccountInfoBranchNetworkServiceInterface.getInfo(AccountInfoBranchNetwork);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoBranchNetwork AccountInfoBranchNetwork)  {
        return AccountInfoBranchNetworkServiceInterface.insertInfo(AccountInfoBranchNetwork);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestBody AccountInfoBranchNetwork AccountInfoBranchNetwork)  {
        return AccountInfoBranchNetworkServiceInterface.updateInfo(AccountInfoBranchNetwork);
    }

    @PutMapping("/deleteInfo")
    public ResponseObject deleteInfo(@RequestBody AccountInfoBranchNetwork AccountInfoBranchNetwork)  {
        return AccountInfoBranchNetworkServiceInterface.deleteInfo(AccountInfoBranchNetwork);
    }

    @PostMapping("/importList")
    public ResponseObject importList(@RequestBody List<AccountInfoBranchNetwork> AccountInfoBranchNetwork)  {
        return AccountInfoBranchNetworkServiceInterface.importListAccountInfoBranchNetwork(AccountInfoBranchNetwork);
    }

    @PutMapping("/updateList")
    public ResponseObject updateList(@RequestBody List<AccountInfoBranchNetwork> AccountInfoBranchNetwork)  {
        return AccountInfoBranchNetworkServiceInterface.updateListAccountInfoBranchNetwork(AccountInfoBranchNetwork);
    }

}
