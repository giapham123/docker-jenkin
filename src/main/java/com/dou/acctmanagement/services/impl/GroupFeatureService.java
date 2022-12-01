package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.GroupFeatureMapper;
import com.dou.acctmanagement.models.GroupFeature;
import com.dou.acctmanagement.services.GroupFeatureServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupFeatureService implements GroupFeatureServiceInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupFeatureService.class);

    @Autowired
    private GroupFeatureMapper groupFeatureMapper;

    public ResponseObject<List<GroupFeature>> getGroupFeatureByGroupId(String groupId) {
        List<GroupFeature> listGroupFeature = groupFeatureMapper.getGroupFeatureByGroupId(groupId);
        return new ResponseObject(listGroupFeature);
    }

    public ResponseObject<List<GroupFeature>> getGroupFeatureById(GroupFeature groupFeature) {
        List<GroupFeature> listGroupFeature = groupFeatureMapper.getGroupFeatureById(groupFeature);
        return new ResponseObject(listGroupFeature);
    }

    public ResponseObject<List<GroupFeature>> loadGroupFeature(GroupFeature groupFeature) {
        List<GroupFeature> listGroupFeature = groupFeatureMapper.loadGroupFeature(groupFeature);
        return new ResponseObject(listGroupFeature);
    }

    @Transactional
    public ResponseObject insGroupFeature(GroupFeature groupFeature) {
        try {
            List<GroupFeature> listGroupFeature = groupFeatureMapper.getGroupFeatureById(groupFeature);
            if (listGroupFeature.size() != 0) {
                return new ResponseObject(listGroupFeature);
            }
            groupFeatureMapper.insGroupFeature(groupFeature);
            return ResponseObject.INSERT_DATA_SUCCESS;
        }
        catch (Exception e) {
            LOGGER.error("Error when insert:" + e.toString());
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject delGroupFeature(GroupFeature groupFeature) {
        try {
            groupFeatureMapper.delGroupFeature(groupFeature);
            return ResponseObject.DELETE_DATA_SUCCESS;
        }
        catch (Exception e) {
            LOGGER.error("Error when delete:" + e.toString());
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
}
