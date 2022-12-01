package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.Group;
import com.dou.acctmanagement.services.GroupServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private GroupServiceInterface groupServiceInterface;

    @RequestMapping("/getAll")
    public ResponseObject<List<Group>> getAllGroup()
    {
        return groupServiceInterface.getAllGroup();
    }
}
