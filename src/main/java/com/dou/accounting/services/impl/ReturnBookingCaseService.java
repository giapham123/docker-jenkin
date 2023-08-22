package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.ReturnBookingCaseMapper;
import com.dou.accounting.models.ReturnBookingCaseModel;
import com.dou.accounting.services.ReturnBookingCaseInterface;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnBookingCaseService implements ReturnBookingCaseInterface {

    @Autowired
    ReturnBookingCaseMapper returnBookingCaseMapper;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getReturnBookingCase(ReturnBookingCaseModel model) {
        ResponseObject rs = new ResponseObject();
        List<ReturnBookingCaseModel> result = returnBookingCaseMapper.getReturnBookingCase(model);
        rs.setData(result);
        rs.setSuccess(true);
        rs.setMessage(null);
        return rs;
    }
}
