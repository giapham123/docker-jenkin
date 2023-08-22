package com.dou.accounting.services;

import com.dou.accounting.models.CheckTerminationDailyRPTModel;

import java.util.List;

public interface CheckTerminationDailyRPTInterface {

    List<CheckTerminationDailyRPTModel> getTerminationDailyReport(CheckTerminationDailyRPTModel checkTerminationDailyRPTModel);

}
