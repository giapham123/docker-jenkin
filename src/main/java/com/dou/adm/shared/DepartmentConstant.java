package com.dou.adm.shared;

import java.util.HashSet;
import java.util.Set;

public abstract class DepartmentConstant {
    public static final String DEPR_ACCOUNTANT                  = "DM01";

    public static final String DEPR_AUDIT                       = "DM02";

    public static final String DEPR_BUSSINESS                   = "DM03";

    public static final String DEPR_DIRECT_SALES                = "DM04";

    public static final String DEPR_SALES_OPERATION             = "DM05";

    public static final String DEPR_UNDER_WRITTING              = "DM06";

    public static final String DEPR_TELE_SALE                   = "DM07";

    public static final String DEPR_ANTI                        = "DM08";

    public static final String DEPR_RISK                        = "DM09";

    public static final String DEPR_BRANCH_NETWORK              = "DM10";

    public static final String DEPR_CALL_CENTER                 = "DM11";

    public static final String DEPR_THIRD_PARTY                 = "DM13";

    public static final Set<String> DEPR_OF_CSR                 = new HashSet<>();

    public static boolean isCSRStaff(String department) {
        return DepartmentConstant.DEPR_BRANCH_NETWORK.equalsIgnoreCase(department)
                || DepartmentConstant.DEPR_SALES_OPERATION.equalsIgnoreCase(department);
    }

    static {
        DEPR_OF_CSR.add(DEPR_BRANCH_NETWORK);
        DEPR_OF_CSR.add(DEPR_SALES_OPERATION);
    }
}
