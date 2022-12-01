package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.AccountInfo;
import com.dou.acctmanagement.models.AccountInfoSearch;
import com.dou.acctmanagement.models.FilterSim_VerifyTicket;
import com.dou.acctmanagement.services.ReportRequestInterface;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/report-request/")
public class ReportRequestController {

    @Autowired
    private ReportRequestInterface reportRequestInterface;

    @PostMapping("/getAccountInfoSearchRequestTicket")
    public ResponseObject<List<AccountInfoSearch>> getAccountInfoSearchRequestTicket(@RequestBody AccountInfo accountInfo) {
        return reportRequestInterface.getAccountInfoSearch(accountInfo);
    }

    // VERIFY
    @PostMapping("/getListRequestSummaryReport")
    public ResponseObject<List<AccountInfoSearch>> getAccountRequestSummaryReport(@RequestBody FilterSim_VerifyTicket accountInfo) {
        return reportRequestInterface.getAccountRequestSummaryReport(accountInfo);
    }

}
