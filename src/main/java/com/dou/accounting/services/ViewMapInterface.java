package com.dou.accounting.services;

import com.dou.accounting.models.ViewMapModel;
import com.dou.adm.shared.ResponseObject;

public interface ViewMapInterface {

    ResponseObject getDataViewMap(ViewMapModel model);

}
