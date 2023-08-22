package com.dou.accounting.controllers;

import com.dou.accounting.models.ReturnBookingCaseModel;
import com.dou.accounting.services.ReturnBookingCaseInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/return-booking-case/")
@CrossOrigin
public class ReturnBookingCaseController {

    @Autowired
    ReturnBookingCaseInterface getReduceInterest;

    @PostMapping("get-data-return-booking")
    public ResponseObject getReturnBookingCase(@RequestBody ReturnBookingCaseModel model) {
        return getReduceInterest.getReturnBookingCase(model);
    }
    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }
}
