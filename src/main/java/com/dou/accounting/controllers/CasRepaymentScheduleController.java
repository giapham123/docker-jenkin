package com.dou.accounting.controllers;

import com.dou.accounting.models.CasRepaymentScheduleModel;
import com.dou.accounting.services.CasRepaymentScheduleInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/casRepaymentSchedule/")
@CrossOrigin
public class CasRepaymentScheduleController {
    @Autowired
    private CasRepaymentScheduleInterface casRepaymentScheduleInterface;


    @PostMapping("getDataCasRepayment")
    public ResponseObject getDataCasRepayment(@RequestBody CasRepaymentScheduleModel casRepaymentScheduleModel){
        ResponseObject rs = new ResponseObject();
        try{
            Map result = casRepaymentScheduleInterface.getDataCasRepayment(casRepaymentScheduleModel);
            List<CasRepaymentScheduleModel> checkData = (List<CasRepaymentScheduleModel>) result.get("data");
            if(checkData.size() <= 0){
                rs.setData(null);
                rs.setMessage("Have No Data");
                rs.setSuccess(false);
            }else {
                rs.setData(result);
                rs.setMessage("Get Data Success");
                rs.setSuccess(true);
            }
        }catch (Exception e){
            rs.setData(null);
            rs.setMessage("Get Data Fail");
            rs.setSuccess(false);
        }
        return rs;
    }

    @PostMapping("export")
    public ResponseObject exportDataCas(@RequestBody CasRepaymentScheduleModel casRepaymentScheduleModel){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = casRepaymentScheduleInterface.exportCasRepaymentData(casRepaymentScheduleModel);
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
