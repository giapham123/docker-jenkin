package com.dou.accounting.controllers;

import com.dou.accounting.models.AccountingModel;
import com.dou.accounting.services.AccountingInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounting/")
@CrossOrigin
public class AccountingController {
    @Autowired
    private AccountingInterface _accountingInterface;



    @PostMapping("getAmount")
    public ResponseObject getDataAccounting(@RequestBody AccountingModel accountingModel){
        ResponseObject rs = new ResponseObject();
        try{
            Map result = _accountingInterface.getListDataAccounting(accountingModel);
            if(result != null){
                rs.setData(result);
                rs.setMessage("Get Data Success");
                rs.setSuccess(true);
            }else{
                rs.setData(result);
                rs.setMessage("The report is not ready, Please come back in 9 A.M");
                rs.setSuccess(false);
            }
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Get Data Fail");
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody AccountingModel accountingModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = _accountingInterface.exportAccountingData(accountingModel);
            if(dataBaseExcel != null){
                rs.setData(dataBaseExcel);
                rs.setMessage("Export Success");
                rs.setSuccess(true);
            }else{
                rs.setData(dataBaseExcel);
                rs.setMessage("Export Fails");
                rs.setSuccess(false);
            }
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Export Fails");
            rs.setSuccess(false);
        }
        return rs;
    }
}
