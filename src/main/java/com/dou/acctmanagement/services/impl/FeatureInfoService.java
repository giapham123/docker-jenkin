package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.FeatureInfoMapper;
import com.dou.acctmanagement.models.FeatureInfo;
import com.dou.acctmanagement.services.FeatureInfoServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureInfoService implements FeatureInfoServiceInterface {

    @Autowired
    private FeatureInfoMapper featureInfoMapper;

    public ResponseObject<List<FeatureInfo>> getAllFeatureInfo( ) {
        List<FeatureInfo> listFeatureInfo = featureInfoMapper.getAllFeatureInfo();
        return new ResponseObject(listFeatureInfo);
    }
}
