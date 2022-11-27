package com.dou.accounting.services;

import com.dou.accounting.models.OutNetReportModel;
import com.dou.accounting.models.RejectUploadFileModel;

import java.util.List;
import java.util.Map;

public interface OutNetReportInterface {

    List<OutNetReportModel> getOutNetReportData(OutNetReportModel outNetReportModel);

}
