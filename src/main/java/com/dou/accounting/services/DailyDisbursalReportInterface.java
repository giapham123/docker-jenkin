package com.dou.accounting.services;

import com.dou.accounting.models.DailyDisbursalReportModel;
import com.dou.adm.shared.ResponseObject;

public interface DailyDisbursalReportInterface {

    ResponseObject getDataDetailDisbursalReport(DailyDisbursalReportModel dailyDisbursalReportModel);

    byte[] exportExcel(DailyDisbursalReportModel dailyDisbursalReportModel);
}
