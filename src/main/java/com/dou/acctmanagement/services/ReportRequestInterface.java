package com.dou.acctmanagement.services;

import com.dou.acctmanagement.models.AccountInfo;
import com.dou.acctmanagement.models.FilterSim_VerifyTicket;
import com.dou.adm.shared.ResponseObject;

public interface ReportRequestInterface {
    ResponseObject getAccountInfoSearch(AccountInfo accountInfo);

    ResponseObject getAccountRequestSummaryReport(FilterSim_VerifyTicket accountInfo);

}
