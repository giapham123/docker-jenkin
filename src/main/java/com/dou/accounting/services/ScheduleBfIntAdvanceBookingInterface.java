package com.dou.accounting.services;

import com.dou.accounting.models.ScheduleBfIntAdvanceBookingModel;
import com.dou.adm.shared.ResponseObject;

public interface ScheduleBfIntAdvanceBookingInterface {

    ResponseObject getReduceInterest(ScheduleBfIntAdvanceBookingModel model);

    ResponseObject getRepayment(ScheduleBfIntAdvanceBookingModel model);

    byte[]  exportRepayment(ScheduleBfIntAdvanceBookingModel model);

}
