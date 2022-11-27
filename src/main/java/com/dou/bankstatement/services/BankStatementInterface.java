package com.dou.bankstatement.services;

import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.models.BankStatementModel;

import java.util.List;

public interface BankStatementInterface {
    ResponseObject getDataBankStatement(BankStatementModel model);

    ResponseObject getBank();
}
