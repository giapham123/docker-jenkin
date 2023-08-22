package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.CheckTerminationDailyRPTMapper;
import com.dou.accounting.models.CheckTerminationDailyRPTModel;
import com.dou.accounting.services.CheckTerminationDailyRPTInterface;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckTerminationDailyRPTService implements CheckTerminationDailyRPTInterface {

    @Autowired
    CheckTerminationDailyRPTMapper mapper;


    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public List<CheckTerminationDailyRPTModel> getTerminationDailyReport(CheckTerminationDailyRPTModel checkTerminationDailyRPTModel) {
         List rs= mapper.getTerminationDailyReport(checkTerminationDailyRPTModel);

        return rs;
    }

}
