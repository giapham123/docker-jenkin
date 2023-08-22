package com.dou.accounting.controllers;

import com.dou.accounting.models.OutNetReportModel;
import com.dou.accounting.services.OutNetReportInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/out-net-report/")
@CrossOrigin
public class OutNetReportController {
    @Autowired
    OutNetReportInterface outNetReportInterface;

    @PostMapping("get-out-net-report")
    public ResponseObject getOutNetReportData(@RequestBody OutNetReportModel outNetReportModel){
        ResponseObject rs = new ResponseObject();
        try{
            rs.setData(outNetReportInterface.getOutNetReportData(outNetReportModel));
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
