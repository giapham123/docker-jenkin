package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoSalesOperation;
import com.dou.staff_info.services.AccountInfoSalesOperationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoSalesOperation")
public class AccountInfoSalesOperationController {

    @Autowired
    private AccountInfoSalesOperationServiceInterface AccountInfoSalesOperationServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoSalesOperation>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoSalesOperationServiceInterface.getAll(id);
    }

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoSalesOperation>> getInfo(@RequestBody AccountInfoSalesOperation AccountInfoSalesOperation)  {
        return AccountInfoSalesOperationServiceInterface.getInfo(AccountInfoSalesOperation);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoSalesOperation AccountInfoSalesOperation)  {
        return AccountInfoSalesOperationServiceInterface.insertInfo(AccountInfoSalesOperation);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestBody AccountInfoSalesOperation AccountInfoSalesOperation)  {
        return AccountInfoSalesOperationServiceInterface.updateInfo(AccountInfoSalesOperation);
    }

    @PutMapping("/deleteInfo")
    public ResponseObject deleteInfo(@RequestBody AccountInfoSalesOperation AccountInfoSalesOperation)  {
        return AccountInfoSalesOperationServiceInterface.deleteInfo(AccountInfoSalesOperation);
    }

    @PostMapping("/importList")
    public ResponseObject importList(@RequestBody List<AccountInfoSalesOperation> AccountInfoSalesOperation)  {
        return AccountInfoSalesOperationServiceInterface.importListAccountInfoSalesOperation(AccountInfoSalesOperation);
    }

    @PutMapping("/updateList")
    public ResponseObject updateList(@RequestBody List<AccountInfoSalesOperation> AccountInfoSalesOperation)  {
        return AccountInfoSalesOperationServiceInterface.updateListAccountInfoSalesOperation(AccountInfoSalesOperation);
    }

}
