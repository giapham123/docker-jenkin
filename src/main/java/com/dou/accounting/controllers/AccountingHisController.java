package com.dou.accounting.controllers;

import com.dou.accounting.models.AccountingHisModel;
import com.dou.accounting.services.AccountingHisInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accountingHis/")
@CrossOrigin
public class AccountingHisController {
    @Autowired
    private AccountingHisInterface _accountingHisInterface;



    @PostMapping("getAccoutingHis")
    public ResponseObject getDataAccountingHis(@RequestBody AccountingHisModel accountingHisModel){
        ResponseObject rs = new ResponseObject();
        try{
            Map result = _accountingHisInterface.getListDataAccountingHis(accountingHisModel);
            rs.setData(result);
            rs.setMessage("Get Data Success");
            rs.setSuccess(true);
        }catch (Exception e){
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Get Data Fail");
            rs.setSuccess(false);
        }
        return rs;
    }
    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody AccountingHisModel accountingHisModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = _accountingHisInterface.exportAccountingHisData(accountingHisModel);
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
