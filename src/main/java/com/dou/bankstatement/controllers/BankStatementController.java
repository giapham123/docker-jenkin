package com.dou.bankstatement.controllers;

import com.dou.accounting.models.AccountingModel;
import com.dou.accounting.services.AccountingInterface;
import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.models.BankStatementModel;
import com.dou.bankstatement.services.BankStatementInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bank-statement/")
public class BankStatementController {
    @Autowired
    private BankStatementInterface bankStatementInterface;

    @PostMapping("get-bank-statement")
    public ResponseObject getDataBankStatement(@RequestBody BankStatementModel model){
        return bankStatementInterface.getDataBankStatement(model);
    }

    @GetMapping("get-bank")
    public ResponseObject getDataBank(){
        return bankStatementInterface.getBank();
    }
}
