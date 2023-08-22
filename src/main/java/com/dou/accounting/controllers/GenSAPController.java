package com.dou.accounting.controllers;

import com.dou.accounting.models.GenSAPModel;
import com.dou.accounting.services.GenSAPInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/gen-sap/")
public class GenSAPController {

    @Autowired
    GenSAPInterface genSAPInterface;


    @PostMapping("get-data-gen-sap")
    public ResponseObject getGenSAPData(@RequestBody GenSAPModel model){
        return genSAPInterface.getGenSAPData(model);
    }

    @GetMapping("author-gen-sap")
    public ResponseObject authorGenSap() throws ParseException {
        return genSAPInterface.authorGenSap();
    }

    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }

    @GetMapping("is-process")
    public ResponseObject getIsTrans(){
        return genSAPInterface.getProcess();
    }

}
