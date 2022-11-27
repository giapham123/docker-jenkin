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

    List<String> SCREEN_PATH = Arrays.asList(
            "close-sold-out/"+CLOSE_SOLD_OUT,"accounting/"+ACCOUNTING,
            "accountingHis/"+ACCOUNTING_HIS,"check-termination-daily-report/"+CHECK_TER_RPT,
            "out-net-report/"+OUTNET_RPT,"receipt-upload-file/"+UPLOAD_FILE,
            "reject-upload-file/"+REJECT_UPLOAD_FILE,"reject-upload-file-gl-sap/"+REJECT_UPLOAD_FILE_GL_SAP,
            "wave-off-amount/"+WAVE_OFF_AMOUNT,"writeoff/"+WRITE_OFF,"schedule-bf-reduce-interest/"+SCHEDULE_BF_REDUCE_INTEREST,
            "casRepaymentSchedule/"+CAS_REPAYMENT_SCHEDULE);
}
