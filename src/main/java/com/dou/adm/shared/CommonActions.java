package com.dou.adm.shared;

import java.util.Arrays;
import java.util.List;

public interface CommonActions {

    List<String> lsCloseSoldOut = Arrays.asList("/api/close-sold-out/loading-details:LOADING",
            "/api/close-sold-out/close-app:CLOSE_APP","/api/close-sold-out/get-close-sold-out:SEARCH");
    List<String> lsAccount = Arrays.asList("/api/accounting/getAmount:SEARCH","/api/accounting/exportExcel:EXPORT");
    List<String> lsAccountHis = Arrays.asList("/api/accountingHis/getAccoutingHis:SEARCH","/api/accountingHis/exportExcel:EXPORT");
    List<String> lsCheckTerDaily= Arrays.asList("/api/check-termination-daily-report/get-termination-daily-report:SEARCH","/api/check-termination-daily-report/export:EXPORT");
    List<String> lsOutNetReport= Arrays.asList("/api/out-net-report/get-out-net-report:SEARCH","/api/out-net-report/export:EXPORT");
    List<String> lsRejectUploadFile = Arrays.asList("/api/reject-upload-file/get-data-reject-upload-file:SEARCH","/api/receipt-upload-file/exportExcel-reject:EXPORT");
    List<String> lsRejectUploadFileGLSAP = Arrays.asList("/api/reject-upload-file-gl-sap/get-data-reject-upload-file-gl-sap:SEARCH","/api/reject-upload-file-gl-sap/export:EXPORT");
    List<String> lsUploadFile =Arrays.asList("/api/receipt-upload-file/get-data-upload-file:SEARCH","/api/receipt-upload-file/exportExcel-upload:EXPORT");
    List<String> lsWaveOffAmount = Arrays.asList("/api/wave-off-amount/uploadFileWaveOffAmount:UPLOAD_FILE","/api/wave-off-amount/getWaveOffAmountData:SEARCH","/api/wave-off-amount/updateWaveOffAmount:UPDATE",
            "/api/wave-off-amount/export:EXPORT");
    List<String> lsWriteOff = Arrays.asList("/api/writeoff/getDataWriteOff:SEARCH","/api/writeoff/exportExcelWriteOff:EXPORT");
    List<String> lsScheduleReduce = Arrays.asList("/api/schedule-bf-reduce-interest/get-reduce-interest:SEARCH",
            "/api/schedule-bf-reduce-interest/exportExcel:EXPORT");
    List<String> lsCasRepayment = Arrays.asList("/api/casRepaymentSchedule/getDataCasRepayment:SEARCH",
            "/api/casRepaymentSchedule/export:EXPORT");
    List<String> lsScheduleInterest = Arrays.asList("/api/schedule-bf-int-advance-booking/get-reduce-interest:SEARCH",
            "/api/schedule-bf-int-advance-booking/exportExcel:EXPORT");
    List<String> lsReturnBookingCase = Arrays.asList("/api/return-booking-case/get-data-return-booking:SEARCH",
            "/api/return-booking-case/export:EXPORT");
    List<String> lsTerminationReportApp = Arrays.asList("/api/termination-report-app/getAmount:SEARCH",
            "/api/termination-report-app/exportExcel:EXPORT");
    List<String> lsUploadBankStatement = Arrays.asList("/api/upload-bank-statement/get-data-bank-statement:SEARCH",
            "/api/upload-bank-statement/exportExcel:EXPORT","/api/upload-bank-statement/upload-bank-statement:UPLOAD");

    List<String> lsDetailDisbursalRpt = Arrays.asList("/api/detail-disbursal-report/get-data-detail-disbursal-report:SEARCH",
            "/api/detail-disbursal-report/exportExcel:EXPORT");
    List<String> lsDailyDisbursalRpt = Arrays.asList("/api/daily-disbursal-report/get-data-daily-disbursal-report:SEARCH",
            "/api/daily-disbursal-report/exportExcel:EXPORT");
    List<String> lsMonthlyDisbursalRpt = Arrays.asList("/api/monthly-disbursal-report/get-data-monthly-disbursal-report:SEARCH",
            "/api/monthly-disbursal-report/exportExcel:EXPORT");

    List<String> lsUploadReconcilingResult = Arrays.asList("/api/sap-manual/upload-reconciling:UPLOAD","/api/sap-manual/delete-data-reconciling-import:DELETE",
            "/api/sap-manual/export:EXPORT-RECONCILING","/api/sap-manual/export-err:EXPORT-ERROR","/api/sap-manual/get-data-reconciling-import:SEARCH");

    List<String> lsCollTrans = Arrays.asList("/api/coll-trans/export:EXPORT",
            "/api/coll-trans/get-coll-trans-data:SEARCH","/api/coll-trans/get-transactions:GET_TRANS");

    List<String> lsGenSAP = Arrays.asList("/api/gen-sap/get-data-gen-sap:SEARCH","/api/gen-sap/author-gen-sap:GEN_SAP",
            "/api/gen-sap/export:EXPORT");

    List<String> lsViewMap = Arrays.asList("/api/view-map/export:EXPORT",
            "/api/view-map/get-data-view-map:SEARCH");

    List<String> lsViewSap = Arrays.asList("/api/view-sap-entries/export:EXPORT",
            "/api/view-sap-entries/get-data-view-sap:SEARCH", "/api/view-sap-entries/put-sap:PUSH");

    List<String> lsViewFinnone = Arrays.asList("/api/view-finnone-entries/exportExcel:EXPORT",
            "/api/view-finnone-entries/get-data-view-finnone:SEARCH");

    List<String> lsPendingDisbur = Arrays.asList("/api/pending-disbur/export:EXPORT",
            "/api/pending-disbur/upload-file-agre:UPLOAD","/api/pending-disbur/insert-sig-agree:INSERT",
            "/api/pending-disbur/get-data-pending-disbur:SEARCH","/api/pending-disbur/delete-agree:DELETE",
            "/api/pending-disbur/process-pending-disbur:PROCESS_PD");
    List<String> lsCollectorCompare = Arrays.asList("/api/coll-compare/collector:COMPARE",
            "/api/coll-compare/get-collector-compare:SEARCH","/api/coll-compare/export:EXPORT");
    List<String> lsBackDate = Arrays.asList("/api/back-date-wo/process-pending-back-date/BDWO:PROCESS-WO",
            "/api/back-date-wo/process-pending-back-date/BDRWO:PROCESS-RWO",
            "/api/back-date-wo/get-data-back-date:SEARCH",
            "/api/back-date-wo/export:EXPORT");

    List<List> lsApi = Arrays.asList(lsCloseSoldOut,lsAccount,lsAccountHis,lsCheckTerDaily,lsOutNetReport,
            lsRejectUploadFile,lsRejectUploadFileGLSAP,lsUploadFile,lsWaveOffAmount,lsWriteOff,lsScheduleReduce,lsCasRepayment,
            lsScheduleInterest,lsReturnBookingCase,lsTerminationReportApp,lsUploadBankStatement,lsDetailDisbursalRpt,lsMonthlyDisbursalRpt,
            lsDailyDisbursalRpt,lsUploadReconcilingResult,lsGenSAP,lsCollTrans,lsViewMap,lsViewSap,lsViewFinnone,lsPendingDisbur,
            lsCollectorCompare,lsBackDate);
}
