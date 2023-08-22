package com.dou.accounting.controllers;

import com.dou.accounting.models.CollectorCompareModel;
import com.dou.accounting.services.CollectorCompareInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/coll-compare/")
public class CollectorCompareController {

    @Autowired
    CollectorCompareInterface collectorCompareInterface;


    @GetMapping("collector")
    public ResponseObject collectorCompare(@RequestParam ("compareDt") String compareDt) throws ParseException {
        return collectorCompareInterface.collectorCompare(compareDt);
    }

    @PostMapping("get-collector-compare")
    public ResponseObject getCollectorCompare(@RequestBody CollectorCompareModel model){
        ResponseObject rs = collectorCompareInterface.getCollectorCompare(model);
        return rs;
    }

    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }

    @GetMapping("is-process")
    public ResponseObject getIsTrans(){
        return collectorCompareInterface.getProcess();
    }
}
