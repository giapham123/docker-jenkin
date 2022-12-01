package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.FeatureInfo;
import com.dou.acctmanagement.services.FeatureInfoServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/featureinfo")
public class FeatureInfoController {

    @Autowired
    private FeatureInfoServiceInterface featureInfoServiceInterface;

    @RequestMapping("/getAll")
    public ResponseObject<List<FeatureInfo>> getAllFeatureInfo()
    {
        return featureInfoServiceInterface.getAllFeatureInfo();
    }
}
