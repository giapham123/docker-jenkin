package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.GroupFeature;
import com.dou.acctmanagement.services.GroupFeatureServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groupfeature")
@CrossOrigin
public class GroupFeatureController {

    @Autowired
    private GroupFeatureServiceInterface groupFeatureServiceInterface;

    @RequestMapping("/getbygroupid/{searchVal}")
    public ResponseObject<List<GroupFeature>> getGroupFeatureByGroupId(@PathVariable("searchVal") String searchVal ) {
        return groupFeatureServiceInterface.getGroupFeatureByGroupId(searchVal);
    }

    @PostMapping("/getbyid")
    public ResponseObject<List<GroupFeature>> getGroupFeatureById (@RequestBody GroupFeature groupFeature) {
        return groupFeatureServiceInterface.getGroupFeatureById(groupFeature);
    }

    @PostMapping("/loadgroupfeature")
    public ResponseObject<List<GroupFeature>> loadGroupFeature (@RequestBody GroupFeature groupFeature) {
        return groupFeatureServiceInterface.loadGroupFeature(groupFeature);
    }

    @PostMapping("/insgroupfeature")
    public ResponseObject insGroupFeature (@RequestBody GroupFeature groupFeature) {
        return groupFeatureServiceInterface.insGroupFeature(groupFeature);
    }

    @PostMapping("/delgroupfeature")
    public ResponseObject delGroupFeature (@RequestBody GroupFeature groupFeature) {
        return groupFeatureServiceInterface.delGroupFeature(groupFeature);
    }
}
