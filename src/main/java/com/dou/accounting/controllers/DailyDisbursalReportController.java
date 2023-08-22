package com.dou.accounting.controllers;

import com.dou.accounting.models.DailyDisbursalReportModel;
import com.dou.accounting.services.DailyDisbursalReportInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/daily-disbursal-report/")
@CrossOrigin
public class DailyDisbursalReportController {

    @Autowired
    DailyDisbursalReportInterface dailyDisbursalReportInterface;

    @PostMapping("get-data-daily-disbursal-report")
    public ResponseObject getDataDailyDisbursalReport(@RequestBody DailyDisbursalReportModel dailyDisbursalReportModel){
        ResponseObject rs = dailyDisbursalReportInterface.getDataDetailDisbursalReport(dailyDisbursalReportModel);
        return rs;
    }
    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody DailyDisbursalReportModel dailyDisbursalReportModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = dailyDisbursalReportInterface.exportExcel(dailyDisbursalReportModel);
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
