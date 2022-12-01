package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoCallCenterMapper;
import com.dou.staff_info.models.AccountInfoCallCenter;
import com.dou.staff_info.services.AccountInfoCallCenterServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountInfoCallCenterService implements AccountInfoCallCenterServiceInterface {


    @Autowired
    private AccountInfoCallCenterMapper accountMapper;

    public ResponseObject<List<AccountInfoCallCenter>> getAll(String supervisorId) {
        List<AccountInfoCallCenter> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoCallCenter>> getInfo(AccountInfoCallCenter AccountInfoCallCenter) {
        List<AccountInfoCallCenter> list = accountMapper.getInfo(AccountInfoCallCenter);
        return new ResponseObject(list);
    }

    public ResponseObject insertInfo(AccountInfoCallCenter accountInfoDrs) {
        List<AccountInfoCallCenter> listCheck = accountMapper.checkExistAccountId(accountInfoDrs.getAccountId());
        if (listCheck.isEmpty()) {
            try {
                accountMapper.insertInfo(accountInfoDrs);
                return ResponseObject.INSERT_DATA_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseObject.INSERT_DATA_FAIL;
            }
        } else {
            return ResponseObject.EXIST_ACCOUNTID;
        }
    }

    public ResponseObject updateInfo(AccountInfoCallCenter AccountInfoCallCenter) {
        try {
            accountMapper.updateInfo(AccountInfoCallCenter);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteInfo(AccountInfoCallCenter AccountInfoCallCenter) {
        try {
            accountMapper.delete(AccountInfoCallCenter);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteAccountInfoCallCenter() {
        try {
            accountMapper.deleteAccountInfoCallCenter();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
    public ResponseObject insertAccountInfoCallCenter_LastBackUp() {
        try {
            accountMapper.insertAccountInfoCallCenter_LastBackUp();
            return ResponseObject.INSERT_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }
    public ResponseObject deleteAccountInfoCallCenter_LastBackUp() {
        try {
            accountMapper.deleteAccountInfoCallCenter_LastBackUp();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject importListAccountInfoCallCenter(List<AccountInfoCallCenter> accountInfoDrs) {

        List<AccountInfoCallCenter> listExist = new ArrayList<>();
        LinkedList<AccountInfoCallCenter>  listTemp = new LinkedList<>();
        Set<String> listAccount = new HashSet<>();

        for (AccountInfoCallCenter element: accountInfoDrs) {
            if (listAccount.contains(element.getAccountId().toUpperCase())){
                listExist.add(element);
            }
            if (listExist.size() > 0){
                break;
            }
            listAccount.add(element.getAccountId().toUpperCase());
        }

        if (listExist.size() > 0){
            return ResponseObject.failResponse("Exist " + listExist.get(0).getAccountId().toUpperCase());
        }else{
            accountMapper.deleteAccountInfoCallCenter_LastBackUp();
            accountMapper.insertAccountInfoCallCenter_LastBackUp();
            accountMapper.deleteAccountInfoCallCenter();
            for(AccountInfoCallCenter accountInfoCallCenter : accountInfoDrs){
                accountMapper.insertInfo(accountInfoCallCenter);
            }
            return ResponseObject.INSERT_DATA_SUCCESS;
            /*int totalRecords = accountInfoDrs.size();
            int numberRound = totalRecords / 1000;

            if (numberRound < 1){
               // accountMapper.importListAccountInfoCallCenter(accountInfoDrs);
                for(AccountInfoCallCenter accountInfoCallCenter : accountInfoDrs){
                    accountMapper.insertInfo(accountInfoCallCenter);
                }
                return ResponseObject.INSERT_DATA_SUCCESS;
            }else {

                for (int round = 1 ; round<= numberRound ;round++ ){
                    for ( int m =round* 1000 - 1000 ; m< round*1000; m ++){
                        listTemp.add(accountInfoDrs.get(m));
                    }
                    accountMapper.importListAccountInfoCallCenter(listTemp);
                    listTemp.clear();
                }
//                listTemp.clear();

                for ( int z = numberRound * 1000 ; z< totalRecords ;z++){
                    listTemp.add(accountInfoDrs.get(z));
                }
                accountMapper.importListAccountInfoCallCenter(listTemp);
                return ResponseObject.INSERT_DATA_SUCCESS;
            }*/
        }
    }

    public ResponseObject updateListAccountInfoCallCenter(List<AccountInfoCallCenter> accountInfoDrs) {
        try {
            if (accountInfoDrs.size() >0){
                for (AccountInfoCallCenter item : accountInfoDrs) {
                    accountMapper.updateInfo(item);
                }
                return ResponseObject.UPDATE_DATA_SUCCESS;
            }else {
                return ResponseObject.UPDATE_DATA_FAIL;
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }

    }

}
