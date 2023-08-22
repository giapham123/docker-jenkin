package com.dou.accounting.services;

import com.dou.accounting.models.ReturnBookingCaseModel;
import com.dou.adm.shared.ResponseObject;

public interface ReturnBookingCaseInterface {

    ResponseObject getReturnBookingCase(ReturnBookingCaseModel model);


}
