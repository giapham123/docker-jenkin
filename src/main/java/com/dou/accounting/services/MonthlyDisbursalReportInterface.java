package com.dou.accounting.services;

import com.dou.accounting.models.MonthlyDisbursalReportModel;
import com.dou.adm.shared.ResponseObject;

public interface MonthlyDisbursalReportInterface {

    ResponseObject getDataMonthlyDisbursalReport(MonthlyDisbursalReportModel monthlyDisbursalReportModel);

    byte[] exportExcel(MonthlyDisbursalReportModel monthlyDisbursalReportModel);
}
