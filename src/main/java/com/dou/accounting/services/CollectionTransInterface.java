package com.dou.accounting.services;

import com.dou.accounting.models.CollectionTransModel;
import com.dou.adm.shared.ResponseObject;

public interface CollectionTransInterface {

    ResponseObject getCollTransData(CollectionTransModel model);

    ResponseObject getTransactions();

}
