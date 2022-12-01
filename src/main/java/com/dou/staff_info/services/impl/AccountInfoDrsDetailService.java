package com.dou.staff_info.services.impl;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoDrsDetailMapper;
import com.dou.staff_info.models.AccountInfoDrsDetail;
import com.dou.staff_info.services.AccountInfoDrsDetailServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AccountInfoDrsDetailService implements AccountInfoDrsDetailServiceInterface {


    @Autowired
    private AccountInfoDrsDetailMapper accountMapper;

    public ResponseObject<List<AccountInfoDrsDetail>> getInfo(AccountInfoDrsDetail accountInfoDrsDetail) {
        List<AccountInfoDrsDetail> list = accountMapper.getInfo(accountInfoDrsDetail);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoDrsDetail>> getAccount(AccountInfoDrsDetail accountInfoDrsDetail) {
        List<AccountInfoDrsDetail> list1 = accountMapper.getAccount(accountInfoDrsDetail);
        return new ResponseObject(list1);
    }

    public ResponseObject updateInfo(JwtUser user, AccountInfoDrsDetail accountInfoDrsDetail) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        try {
            accountInfoDrsDetail.setUserUpdate(user.getUsername().toUpperCase());
            accountInfoDrsDetail.setDateTime(formattedDate);

            accountMapper.updateInfo(accountInfoDrsDetail);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject insertInfo(AccountInfoDrsDetail accountInfoDrsDetail) {
        List<AccountInfoDrsDetail> listCheck = accountMapper.checkExistAccountId(accountInfoDrsDetail.getAccountId());
        if (listCheck.isEmpty()) {
            try {
                accountMapper.insertInfo(accountInfoDrsDetail);
                return ResponseObject.INSERT_DATA_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseObject.INSERT_DATA_FAIL;
            }
        } else {
            return ResponseObject.EXIST_ACCOUNTID;
        }
    }

}
