package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoRiskMapper;
import com.dou.staff_info.models.AccountInfoRisk;
import com.dou.staff_info.services.AccountInfoRiskServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountInfoRiskService implements AccountInfoRiskServiceInterface {


    @Autowired
    private AccountInfoRiskMapper accountMapper;

    public ResponseObject<List<AccountInfoRisk>> getAll(String supervisorId){
        List<AccountInfoRisk> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }


}
