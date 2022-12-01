package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoAntiMapper;
import com.dou.staff_info.models.AccountInfoAnti;
import com.dou.staff_info.services.AccountInfoAntiServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountInfoAntiService implements AccountInfoAntiServiceInterface {


    @Autowired
    private AccountInfoAntiMapper accountMapper;

    public ResponseObject<List<AccountInfoAnti>> getAll(String supervisorId){
        List<AccountInfoAnti> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }


}
