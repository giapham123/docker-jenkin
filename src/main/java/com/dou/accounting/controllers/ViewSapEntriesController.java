package com.dou.accounting.controllers;

import com.dou.accounting.models.ViewSapEntriesModel;
import com.dou.accounting.services.ViewSapEntriesInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("/api/view-sap-entries/")
public class ViewSapEntriesController {

    @Autowired
    ViewSapEntriesInterface viewSapEntriesInterface;


    @PostMapping("get-data-view-sap")
    public ResponseObject getDataViewSapEntries(@RequestBody ViewSapEntriesModel model){
        return viewSapEntriesInterface.getDataViewSapEntries(model);
    }
    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }

    @GetMapping("put-sap")
    public ResponseObject putSap(@RequestAttribute(REQ_USR) JwtUser authUser) throws ParseException {
        return viewSapEntriesInterface.pushSap(authUser.getUsername());
    }

    @GetMapping("is-process")
    public ResponseObject getIsProcess(){
        return viewSapEntriesInterface.getProcess();
    }

}
