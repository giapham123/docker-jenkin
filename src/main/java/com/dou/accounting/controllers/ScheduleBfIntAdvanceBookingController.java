package com.dou.accounting.controllers;

import com.dou.accounting.models.ScheduleBfIntAdvanceBookingModel;
import com.dou.accounting.services.ScheduleBfIntAdvanceBookingInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule-bf-int-advance-booking/")
@CrossOrigin
public class ScheduleBfIntAdvanceBookingController {

    @Autowired
    ScheduleBfIntAdvanceBookingInterface scheduleBfIntAdvanceBookingInterface;

    @PostMapping("get-reduce-interest")
    public ResponseObject getReduceInterest(@RequestBody ScheduleBfIntAdvanceBookingModel model) {
        return scheduleBfIntAdvanceBookingInterface.getReduceInterest(model);
    }

    @PostMapping("get-repaymnet")
    public ResponseObject getRepayment(@RequestBody ScheduleBfIntAdvanceBookingModel model) {
        return scheduleBfIntAdvanceBookingInterface.getRepayment(model);
    }

    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody ScheduleBfIntAdvanceBookingModel model){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = scheduleBfIntAdvanceBookingInterface.exportRepayment(model);
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
