package com.dou.adm.shared;

import java.util.Arrays;
import java.util.List;

public interface CommonUICode {
    String ACCOUNTING = "acc01";

    String ACCOUNTING_HIS = "acc02";

    String CHECK_TER_RPT = "acc04";

    String WRITE_OFF = "w_ext";

    String UPLOAD_FILE = "re01";

    String REJECT_UPLOAD_FILE = "re02";

    String REJECT_UPLOAD_FILE_GL_SAP = "re03";

    String OUTNET_RPT = "acc03";

    String WAVE_OFF_AMOUNT = "wave";

    String CLOSE_SOLD_OUT = "acc05";

    String SCHEDULE_BF_REDUCE_INTEREST = "acc06";

    String CAS_REPAYMENT_SCHEDULE = "acc08";

    String SCHEDULE_BF_INT_ADVANCE_BOOKING = "acc09";

    String RETURN_BOOKING_CASE = "acc10";

    String TERMINATION_REPORT_APP = "acc11";

    String UPLOAD_BANK_STATEMENT = "acc12";

    String DETAIL_DISBURSAL_REPORT = "acc13";

    String DAILY_DISBURSAL_REPORT = "acc14";

    String MONTHLY_DISBURSAL_REPORT = "acc15";

    String SAP_UPLOAD_RECONCILING = "acc16";

    String GEN_SAP = "acc18";

    String VIEW_MAP_SAP_GL = "acc17";

    String COLL_TRANS = "acc19";

    String VIEW_FINNONE_ENTRIES = "acc20";

    String VIEW_SAP_ENTRIES = "acc21";

    String PENDING_DISBURSEMENT = "acc22";

    String COLLECTOR_COMPARE = "acc23";

    String BACK_DATE = "acc24";

    List<String> SCREEN_PATH = Arrays.asList(
            "close-sold-out/"+CLOSE_SOLD_OUT,"accounting/"+ACCOUNTING,
            "accountingHis/"+ACCOUNTING_HIS,"check-termination-daily-report/"+CHECK_TER_RPT,
            "out-net-report/"+OUTNET_RPT,"receipt-upload-file/"+UPLOAD_FILE,
            "reject-upload-file/"+REJECT_UPLOAD_FILE,"reject-upload-file-gl-sap/"+REJECT_UPLOAD_FILE_GL_SAP,
            "wave-off-amount/"+WAVE_OFF_AMOUNT,"writeoff/"+WRITE_OFF,"schedule-bf-reduce-interest/"+SCHEDULE_BF_REDUCE_INTEREST,
            "casRepaymentSchedule/"+CAS_REPAYMENT_SCHEDULE,
            "schedule-bf-int-advance-booking/"+SCHEDULE_BF_INT_ADVANCE_BOOKING,
            "return-booking-case/"+RETURN_BOOKING_CASE, "termination-report-app/"+TERMINATION_REPORT_APP,
            "upload-bank-statement/"+UPLOAD_BANK_STATEMENT,"detail-disbursal-report/"+DETAIL_DISBURSAL_REPORT,
            "daily-disbursal-report/"+DAILY_DISBURSAL_REPORT,"monthly-disbursal-report/"+MONTHLY_DISBURSAL_REPORT,
            "sap-manual/"+SAP_UPLOAD_RECONCILING, "gen-sap/"+GEN_SAP, "coll-trans/"+COLL_TRANS,
            "view-map/"+VIEW_MAP_SAP_GL,"view-finnone-entries/"+VIEW_FINNONE_ENTRIES,"view-sap-entries/"+VIEW_SAP_ENTRIES,"pending-disbur/"+PENDING_DISBURSEMENT,
            "coll-compare/"+COLLECTOR_COMPARE, "back-date-wo/"+BACK_DATE);
}
