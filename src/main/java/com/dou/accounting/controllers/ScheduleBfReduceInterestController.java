package com.dou.accounting.controllers;

import com.dou.accounting.models.AccountingModel;
import com.dou.accounting.models.ScheduleBfReduceInterestModel;
import com.dou.accounting.services.ScheduleBfReduceInterestInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule-bf-reduce-interest/")
@CrossOrigin
public class ScheduleBfReduceInterestController {

    @Autowired
    ScheduleBfReduceInterestInterface scheduleBfReduceInterestInterface;

    @PostMapping("get-reduce-interest")
    public ResponseObject getReduceInterest(@RequestBody ScheduleBfReduceInterestModel model) {
        return scheduleBfReduceInterestInterface.getReduceInterest(model);
    }

    @PostMapping("get-repaymnet")
    public ResponseObject getRepayment(@RequestBody ScheduleBfReduceInterestModel model) {
        return scheduleBfReduceInterestInterface.getRepayment(model);
    }

    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody ScheduleBfReduceInterestModel model){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = scheduleBfReduceInterestInterface.exportRepayment(model);
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
