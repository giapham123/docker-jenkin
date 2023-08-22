package com.dou.accounting.services;

import com.dou.accounting.models.CollectorCompareModel;
import com.dou.adm.shared.ResponseObject;

import java.text.ParseException;

public interface CollectorCompareInterface {

    ResponseObject collectorCompare(String compareDt) throws ParseException;

    ResponseObject getCollectorCompare(CollectorCompareModel model);

    ResponseObject getProcess();

}
