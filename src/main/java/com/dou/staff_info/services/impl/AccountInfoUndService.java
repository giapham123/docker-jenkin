package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoUndMapper;
import com.dou.staff_info.models.AccountInfoUnd;
import com.dou.staff_info.services.AccountInfoUndServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountInfoUndService implements AccountInfoUndServiceInterface {


    @Autowired
    private AccountInfoUndMapper accountMapper;

    public ResponseObject<List<AccountInfoUnd>> getAll(String supervisorId) {
        List<AccountInfoUnd> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoUnd>> getInfo(AccountInfoUnd AccountInfoUnd) {
        List<AccountInfoUnd> list = accountMapper.getInfo(AccountInfoUnd);
        return new ResponseObject(list);
    }

    public ResponseObject insertInfo(AccountInfoUnd accountInfoDrs) {
        List<AccountInfoUnd> listCheck = accountMapper.checkExistAccountId(accountInfoDrs.getAccountId());
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

    public ResponseObject updateInfo(AccountInfoUnd AccountInfoUnd) {
        try {
            accountMapper.updateInfo(AccountInfoUnd);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteInfo(AccountInfoUnd AccountInfoUnd) {
        try {
            accountMapper.delete(AccountInfoUnd);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteAccountInfoUnd() {
        try {
            accountMapper.deleteAccountInfoUnd();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
    public ResponseObject insertAccountInfoUnd_LastBackUp() {
        try {
            accountMapper.insertAccountInfoUnd_LastBackUp();
            return ResponseObject.INSERT_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }
    public ResponseObject deleteAccountInfoUnd_LastBackUp() {
        try {
            accountMapper.deleteAccountInfoUnd_LastBackUp();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject importListAccountInfoUnd(List<AccountInfoUnd> accountInfoDrs) {

        List<AccountInfoUnd> listExist = new ArrayList<>();
        LinkedList<AccountInfoUnd>  listTemp = new LinkedList<>();
        Set<String> listAccount = new HashSet<>();

        for (AccountInfoUnd element: accountInfoDrs) {
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
            accountMapper.deleteAccountInfoUnd_LastBackUp();
            accountMapper.insertAccountInfoUnd_LastBackUp();
            accountMapper.deleteAccountInfoUnd();
            for(AccountInfoUnd accountInfoUnd : accountInfoDrs){
                accountMapper.insertInfo(accountInfoUnd);
            }
            return ResponseObject.INSERT_DATA_SUCCESS;
            /*int totalRecords = accountInfoDrs.size();
            int numberRound = totalRecords / 1000;

            if (numberRound < 1){
                //accountMapper.importListAccountInfoUnd(accountInfoDrs);
                for(AccountInfoUnd accountInfoUnd : accountInfoDrs){
                    accountMapper.insertInfo(accountInfoUnd);
                }
                return ResponseObject.INSERT_DATA_SUCCESS;
            }else {

                for (int round = 1 ; round<= numberRound ;round++ ){
                    for ( int m =round* 1000 - 1000 ; m< round*1000; m ++){
                        listTemp.add(accountInfoDrs.get(m));
                    }
                    accountMapper.importListAccountInfoUnd(listTemp);
                    listTemp.clear();
                }
//                listTemp.clear();

                for ( int z = numberRound * 1000 ; z< totalRecords ;z++){
                    listTemp.add(accountInfoDrs.get(z));
                }
                accountMapper.importListAccountInfoUnd(listTemp);
                return ResponseObject.INSERT_DATA_SUCCESS;
            }*/
        }
    }

    public ResponseObject updateListAccountInfoUnd(List<AccountInfoUnd> accountInfoDrs) {
        try {
            if (accountInfoDrs.size() >0){
                for (AccountInfoUnd item : accountInfoDrs) {
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
