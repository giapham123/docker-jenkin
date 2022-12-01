package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.GroupMapper;
import com.dou.acctmanagement.models.Group;
import com.dou.acctmanagement.services.GroupServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements GroupServiceInterface {

    @Autowired
    private GroupMapper groupMapper;

    public ResponseObject<List<Group>> getAllGroup() {
        List<Group> listGroup = groupMapper.getAllGroup();
        return new ResponseObject(listGroup);
    }
}
