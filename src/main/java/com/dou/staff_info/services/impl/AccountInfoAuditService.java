package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoAuditMapper;
import com.dou.staff_info.models.AccountInfoAudit;
import com.dou.staff_info.services.AccountInfoAuditServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountInfoAuditService implements AccountInfoAuditServiceInterface {


    @Autowired
    private AccountInfoAuditMapper accountMapper;

    public ResponseObject<List<AccountInfoAudit>> getAll(String supervisorId){
        List<AccountInfoAudit> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }


}
