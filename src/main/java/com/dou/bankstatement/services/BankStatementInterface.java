package com.dou.bankstatement.services;

import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.models.BankStatementModel;

public interface BankStatementInterface {
    ResponseObject getDataBankStatement(BankStatementModel model);

    ResponseObject getBank();
}
