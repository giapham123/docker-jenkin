package com.dou.accounting.services;

import com.dou.accounting.models.OutNetReportModel;

import java.util.List;

public interface OutNetReportInterface {

    List<OutNetReportModel> getOutNetReportData(OutNetReportModel outNetReportModel);

}
