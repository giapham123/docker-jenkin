package com.dou.accounting.services;

import com.dou.accounting.models.ViewFinnoneEntriesModel;
import com.dou.adm.shared.ResponseObject;

public interface ViewFinnoneEntriesInterface {

    ResponseObject getDataViewFinnoneEntries(ViewFinnoneEntriesModel model);

    byte[] dataForExport(ViewFinnoneEntriesModel model);

}
