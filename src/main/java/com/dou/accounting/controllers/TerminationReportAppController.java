package com.dou.accounting.controllers;

import com.dou.accounting.models.TerminationReportAppModel;
import com.dou.accounting.services.TerminationReportAppInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/termination-report-app/")
@CrossOrigin
public class TerminationReportAppController {
    @Autowired
    private TerminationReportAppInterface terminationReportAppInterface;

    @PostMapping("getAmount")
    public ResponseObject getDataTerminationReportApp(@RequestBody TerminationReportAppModel terminationReportAppModel){
        ResponseObject rs = new ResponseObject();
        try{
            Map result = terminationReportAppInterface.getDataTerminationReportApp(terminationReportAppModel);
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
    public ResponseObject exportExcel(@RequestBody TerminationReportAppModel terminationReportAppModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = terminationReportAppInterface.exportTerminationReportAppData(terminationReportAppModel);
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
