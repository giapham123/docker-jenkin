package com.dou.bankstatement.mappers;

import com.dou.bankstatement.models.BankStatementModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForwardDB {
    @Autowired
    private FTPMapper _ftpMapper;

    public void bankStatement(BankStatementModel model){_ftpMapper.bankStatement(model);}
}
