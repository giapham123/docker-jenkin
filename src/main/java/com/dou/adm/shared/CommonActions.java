package com.dou.adm.shared;

import java.util.*;

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
    List<List> lsApi = Arrays.asList(lsCloseSoldOut,lsAccount,lsAccountHis,lsCheckTerDaily,lsOutNetReport,
            lsRejectUploadFile,lsRejectUploadFileGLSAP,lsUploadFile,lsWaveOffAmount,lsWriteOff,lsScheduleReduce,lsCasRepayment);
}
