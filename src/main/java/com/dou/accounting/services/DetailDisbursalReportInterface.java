package com.dou.accounting.services;

import com.dou.accounting.models.DetailDisbursalReportModel;
import com.dou.adm.shared.ResponseObject;

public interface DetailDisbursalReportInterface {

    ResponseObject getDataDetailDisbursalReport(DetailDisbursalReportModel detailDisbursalReportModel);

    byte[] exportExcel(DetailDisbursalReportModel detailDisbursalReportModel);
}
