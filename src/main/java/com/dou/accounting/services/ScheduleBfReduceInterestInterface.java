package com.dou.accounting.services;

import com.dou.accounting.models.ScheduleBfReduceInterestModel;
import com.dou.adm.shared.ResponseObject;

public interface ScheduleBfReduceInterestInterface {

    ResponseObject getReduceInterest(ScheduleBfReduceInterestModel model);

    ResponseObject getRepayment(ScheduleBfReduceInterestModel model);

    byte[]  exportRepayment(ScheduleBfReduceInterestModel model);

}
