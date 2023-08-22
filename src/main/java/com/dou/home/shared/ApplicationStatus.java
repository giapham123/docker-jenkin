package com.dou.home.shared;

import java.util.HashSet;
import java.util.Set;

public interface ApplicationStatus {

    String ALL                                      = "ALL";

    String ANYONE                                   = "ANYONE";

    String APPROVED                                 = "APPROVED";

    String APPROVAL                                 = "APPROVAL";

    String REJECTED                                 = "REJECTED";

    String PENDING                                  = "PENDING";

    String DEFER                                    = "DEFER";

    String ASSIGNED                                 = "ASSIGNED";

    String UNASSIGNED                               = "UNASSIGNED";

    Set<String> FILTER_SALE_APP_STATUS              = getSaleFilterStatus();

    Set<String> FILTER_SALE_ADMIN_APP_STATUS        = getSaleFilterStatus();

    Set<String> FILTER_UND_ASSIGN_STATUS            = getUndAssignFilterStatus();

    Set<String> FILTER_UNDERWRITING_STATUS          = getUnderwritingFilterStatus();

    static Set getSaleFilterStatus() {
        Set<String> status = new HashSet<>();
        status.add(APPROVED);
        status.add(REJECTED);
        status.add(PENDING);
        status.add(DEFER);
        status.add(ALL);
        return status;
    }

    static Set getUndAssignFilterStatus() {
        Set<String> status = new HashSet<>();
        status.add(ASSIGNED);
        status.add(UNASSIGNED);
        return status;
    }

    static Set getUnderwritingFilterStatus() {
        Set<String> status = new HashSet<>();
        status.add(APPROVAL);
        status.add(REJECTED);
        status.add(PENDING);
        status.add(DEFER);
        status.add(ALL);
        status.add(ANYONE);
        return status;
    }
}
