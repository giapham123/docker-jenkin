package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.OutNetReportMapper;
import com.dou.accounting.models.OutNetReportModel;
import com.dou.accounting.services.OutNetReportInterface;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutNetReportService implements OutNetReportInterface {

    @Autowired
    OutNetReportMapper mapper;


    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public List<OutNetReportModel> getOutNetReportData(OutNetReportModel outNetReportModel) {
         List rs= mapper.getOutNetReportData(outNetReportModel);

        return rs;
    }

}
