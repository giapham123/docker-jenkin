package com.dou.accounting.controllers;

import com.dou.accounting.models.MonthlyDisbursalReportModel;
import com.dou.accounting.services.MonthlyDisbursalReportInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monthly-disbursal-report/")
@CrossOrigin
public class MonthlyDisbursalReportController {

    @Autowired
    MonthlyDisbursalReportInterface monthlyDisbursalReportInterface;

    @PostMapping("get-data-monthly-disbursal-report")
    public ResponseObject getDataMonthlyDisbursalReport(@RequestBody MonthlyDisbursalReportModel monthlyDisbursalReportModel){
        ResponseObject rs = monthlyDisbursalReportInterface.getDataMonthlyDisbursalReport(monthlyDisbursalReportModel);
        return rs;
    }
    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody MonthlyDisbursalReportModel monthlyDisbursalReportModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = monthlyDisbursalReportInterface.exportExcel(monthlyDisbursalReportModel);
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
