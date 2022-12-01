package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportRequestMapper {
    List<AccountInfo> getAccountInfoSearch(@Param("accountId") String accountId, @Param("departmentId") String departmentId);

    List<RequestSummary> getAccountRequestSummaryReport(FilterSim_VerifyTicket accountInfo);

    List<RequestSummary> getAccountRequestSummaryScheduleMail();

    List<AccountInfoDetail> getAccountRequestDetailScheduleMail();

    List<AccountPermissionGroupSimFeature> getAccountRequestPermissionScheduleMail();
}
