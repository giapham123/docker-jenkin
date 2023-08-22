package com.dou.accounting.services;

import com.dou.accounting.models.TerminationReportAppModel;

import java.util.Map;

public interface TerminationReportAppInterface {
    Map getDataTerminationReportApp(TerminationReportAppModel terminationReportAppModel);

    byte[] exportTerminationReportAppData(TerminationReportAppModel terminationReportAppModel);

}
