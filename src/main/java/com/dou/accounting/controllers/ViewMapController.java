package com.dou.accounting.controllers;

import com.dou.accounting.models.ViewMapModel;
import com.dou.accounting.services.ViewMapInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/view-map/")
public class ViewMapController {

    @Autowired
    ViewMapInterface viewMapInterface;


    @PostMapping("get-data-view-map")
    public ResponseObject getDataViewMap(@RequestBody ViewMapModel model){
        return viewMapInterface.getDataViewMap(model);
    }
    @GetMapping("export")
    public ResponseObject export(){
        ResponseObject rs = new ResponseObject();
        return rs;
    }

}
