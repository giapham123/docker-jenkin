package com.dou.accounting.controllers;

import com.dou.accounting.models.CheckTerminationDailyRPTModel;
import com.dou.accounting.services.impl.CheckTerminationDailyRPTService;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/check-termination-daily-report/")
@CrossOrigin
public class CheckTerminationDailyRPTController {
    @Autowired
    CheckTerminationDailyRPTService checkTerminationDailyRPTService;

    @PostMapping("get-termination-daily-report")
    public ResponseObject getTerminationDailyReport(@RequestBody CheckTerminationDailyRPTModel checkTerminationDailyRPTModel){
        ResponseObject rs = new ResponseObject();
        try{
            rs.setData(checkTerminationDailyRPTService.getTerminationDailyReport(checkTerminationDailyRPTModel));
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

    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }
}
