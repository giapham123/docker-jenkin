package com.dou.accounting.services;

import com.dou.accounting.models.WriteOffModel;

import java.util.Map;

public interface WriteOffInterface {
    Map getListDataWriteOff(WriteOffModel writeOffModel);

    byte[] exportWriteOffData(WriteOffModel writeOffModel);
}
