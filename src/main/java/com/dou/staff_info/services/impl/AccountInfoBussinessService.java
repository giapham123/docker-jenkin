package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoBussinessMapper;
import com.dou.staff_info.models.AccountInfoBussiness;
import com.dou.staff_info.services.AccountInfoBussinessServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountInfoBussinessService implements AccountInfoBussinessServiceInterface {


    @Autowired
    private AccountInfoBussinessMapper accountMapper;

    public ResponseObject<List<AccountInfoBussiness>> getAll(String supervisorId){
        List<AccountInfoBussiness> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }


}
