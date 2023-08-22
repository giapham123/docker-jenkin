package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.ViewMapMapper;
import com.dou.accounting.models.ViewMapModel;
import com.dou.accounting.services.ViewMapInterface;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewMapService implements ViewMapInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewMapService.class);

    @Autowired
    ViewMapMapper viewMapMapper;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataViewMap(ViewMapModel model) {
        ResponseObject rs = new ResponseObject();
        rs.setData(viewMapMapper.getDataViewMap(model));
        rs.setSuccess(true);
        return rs;
    }
}
