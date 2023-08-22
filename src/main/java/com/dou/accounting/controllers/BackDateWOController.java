package com.dou.accounting.controllers;

import com.dou.accounting.services.BackDateWOInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("/api/back-date-wo/")
public class BackDateWOController {

    @Autowired
    BackDateWOInterface backDateWOInterface;

    @GetMapping("get-data-back-date")
    public ResponseObject getDataBackDate(@RequestParam ("date")String date, @RequestParam ("type")String type) throws ParseException {
        return backDateWOInterface.getDataBackDate(date, type);
    }
    @GetMapping("process-pending-back-date/{id}")
    public ResponseObject processPendingBackDate(@RequestParam("type") String type, @RequestParam("date") String date,@RequestAttribute(REQ_USR) JwtUser authUser) throws ParseException {
        return backDateWOInterface.processPendingBackDate(type, date,authUser.getUsername());
    }

    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }
}
