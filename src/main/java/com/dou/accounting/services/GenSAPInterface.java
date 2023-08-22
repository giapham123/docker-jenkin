package com.dou.accounting.services;

import com.dou.accounting.models.GenSAPModel;
import com.dou.adm.shared.ResponseObject;

import java.text.ParseException;

public interface GenSAPInterface {

    ResponseObject getGenSAPData(GenSAPModel model);

    ResponseObject authorGenSap() throws ParseException;

    ResponseObject getProcess();

}
