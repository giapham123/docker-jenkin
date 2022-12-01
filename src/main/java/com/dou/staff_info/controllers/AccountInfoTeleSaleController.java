package com.dou.staff_info.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.models.AccountInfoTeleSale;
import com.dou.staff_info.services.AccountInfoTeleSaleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/account/AccountInfoTeleSale")
public class AccountInfoTeleSaleController {

    @Autowired
    private AccountInfoTeleSaleServiceInterface AccountInfoTeleSaleServiceInterface;

    @RequestMapping("/getBySupervisorId/{supervisorId}")
    public ResponseObject<List<AccountInfoTeleSale>> get(@PathVariable("supervisorId") String id)  {
        return AccountInfoTeleSaleServiceInterface.getAll(id);
    }

    @PostMapping("/getInfo")
    public ResponseObject<List<AccountInfoTeleSale>> getInfo(@RequestBody AccountInfoTeleSale AccountInfoTeleSale)  {
        return AccountInfoTeleSaleServiceInterface.getInfo(AccountInfoTeleSale);
    }

    @PostMapping("/insertInfo")
    public ResponseObject insertInfo(@RequestBody AccountInfoTeleSale AccountInfoTeleSale)  {
        return AccountInfoTeleSaleServiceInterface.insertInfo(AccountInfoTeleSale);
    }

    @PutMapping("/updateInfo")
    public ResponseObject updateInfo(@RequestBody AccountInfoTeleSale AccountInfoTeleSale)  {
        return AccountInfoTeleSaleServiceInterface.updateInfo(AccountInfoTeleSale);
    }

    @PutMapping("/deleteInfo")
    public ResponseObject deleteInfo(@RequestBody AccountInfoTeleSale AccountInfoTeleSale)  {
        return AccountInfoTeleSaleServiceInterface.deleteInfo(AccountInfoTeleSale);
    }

    @PostMapping("/importList")
    public ResponseObject importList(@RequestBody List<AccountInfoTeleSale> AccountInfoTeleSale)  {
        return AccountInfoTeleSaleServiceInterface.importListAccountInfoTeleSale(AccountInfoTeleSale);
    }

    @PutMapping("/updateList")
    public ResponseObject updateList(@RequestBody List<AccountInfoTeleSale> AccountInfoTeleSale)  {
        return AccountInfoTeleSaleServiceInterface.updateListAccountInfoTeleSale(AccountInfoTeleSale);
    }

}
