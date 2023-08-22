package com.dou.accounting.controllers;

import com.dou.accounting.models.DetailDisbursalReportModel;
import com.dou.accounting.services.DetailDisbursalReportInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detail-disbursal-report/")
@CrossOrigin
public class DetailDisbursalReportController {

    @Autowired
    DetailDisbursalReportInterface detailDisbursalReportInterface;

    @PostMapping("get-data-detail-disbursal-report")
    public ResponseObject getDataDetailDisbursalReport(@RequestBody DetailDisbursalReportModel uploadBankStatementModel){
        ResponseObject rs = detailDisbursalReportInterface.getDataDetailDisbursalReport(uploadBankStatementModel);
        return rs;
    }
    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody DetailDisbursalReportModel uploadBankStatementModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = detailDisbursalReportInterface.exportExcel(uploadBankStatementModel);
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
