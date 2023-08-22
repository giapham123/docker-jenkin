package com.dou.accounting.services;

import com.dou.accounting.models.ViewSapEntriesModel;
import com.dou.adm.shared.ResponseObject;

import java.text.ParseException;

public interface ViewSapEntriesInterface {

    ResponseObject getDataViewSapEntries(ViewSapEntriesModel model);

    ResponseObject pushSap(String userLogin) throws ParseException;

    ResponseObject getProcess();

}
