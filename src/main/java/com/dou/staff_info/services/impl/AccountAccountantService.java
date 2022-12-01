package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountAccountantMapper;
import com.dou.staff_info.models.AccountAccountant;
import com.dou.staff_info.services.AccountAccountantServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountAccountantService implements AccountAccountantServiceInterface {


    @Autowired
    private AccountAccountantMapper accountMapper;

    public ResponseObject<List<AccountAccountant>> getAll(String supervisorId){
        List<AccountAccountant> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }


}
