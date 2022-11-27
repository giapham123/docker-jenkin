package com.dou.accounting.services;

import com.dou.accounting.models.CasRepaymentScheduleModel;
import java.util.Map;

public interface CasRepaymentScheduleInterface {
    Map getDataCasRepayment(CasRepaymentScheduleModel casRepaymentScheduleModel);

    byte[] exportCasRepaymentData(CasRepaymentScheduleModel casRepaymentScheduleModel);
}
