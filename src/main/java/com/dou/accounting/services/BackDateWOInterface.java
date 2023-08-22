package com.dou.accounting.services;

import com.dou.adm.shared.ResponseObject;

import java.text.ParseException;

public interface BackDateWOInterface {

    ResponseObject getDataBackDate(String date,String type) throws ParseException;

    ResponseObject processPendingBackDate(String type,String date, String usgLg) throws ParseException;

}
