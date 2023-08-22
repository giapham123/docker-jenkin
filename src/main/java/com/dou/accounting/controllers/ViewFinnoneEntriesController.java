package com.dou.accounting.controllers;

import com.dou.accounting.models.ViewFinnoneEntriesModel;
import com.dou.accounting.services.ViewFinnoneEntriesInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/view-finnone-entries/")
public class ViewFinnoneEntriesController {

    @Autowired
    ViewFinnoneEntriesInterface viewFinnoneEntriesInterface;


    @PostMapping("get-data-view-finnone")
    public ResponseObject getDataViewFinnoneEntries(@RequestBody ViewFinnoneEntriesModel model){
        return viewFinnoneEntriesInterface.getDataViewFinnoneEntries(model);
    }

    @PostMapping("exportExcel")
    public ResponseObject exportExcel(@RequestBody ViewFinnoneEntriesModel model){
        ResponseObject rs = new ResponseObject();
        try{
            byte[] dataBaseExcel = viewFinnoneEntriesInterface.dataForExport(model);
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
