package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoDrsMapper;
import com.dou.staff_info.models.AccountInfoDrs;
import com.dou.staff_info.services.AccountInfoDrsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountInfoDrsService implements AccountInfoDrsServiceInterface {


    @Autowired
    private AccountInfoDrsMapper accountMapper;

    public ResponseObject<List<AccountInfoDrs>> getAll(String supervisorId) {
        List<AccountInfoDrs> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoDrs>> getInfo(AccountInfoDrs AccountInfoDrs) {
        List<AccountInfoDrs> list = accountMapper.getInfo(AccountInfoDrs);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoDrs>> getInfoAccount(AccountInfoDrs AccountInfoDrs) {
        List<AccountInfoDrs> list = accountMapper.getInfoAccount(AccountInfoDrs);
        return new ResponseObject(list);
    }

    public ResponseObject insertInfo(AccountInfoDrs accountInfoDrs) {
        List<AccountInfoDrs> listCheck = accountMapper.checkExistAccountId(accountInfoDrs.getAccountId());
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

    public ResponseObject updateInfo(AccountInfoDrs AccountInfoDrs) {
        try {
            accountMapper.updateInfo(AccountInfoDrs);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteInfo(AccountInfoDrs AccountInfoDrs) {
        try {
            accountMapper.delete(AccountInfoDrs);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteAccountInfoDrs() {
        try {
            accountMapper.deleteAccountInfoDrs();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
    public ResponseObject insertAccountInfoDrs_LastBackUp() {
        try {
            accountMapper.insertAccountInfoDrs_LastBackUp();
            return ResponseObject.INSERT_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }
    public ResponseObject deleteAccountInfoDrs_LastBackUp() {
        try {
            accountMapper.deleteAccountInfoDrs_LastBackUp();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject importListAccountInfoDrs(List<AccountInfoDrs> accountInfoDrs) {

        List<AccountInfoDrs> listExist = new ArrayList<>();
        LinkedList<AccountInfoDrs>  listTemp = new LinkedList<>();
        Set<String> listAccount = new HashSet<>();

        for (AccountInfoDrs element: accountInfoDrs) {
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
            accountMapper.deleteAccountInfoDrs_LastBackUp();
            accountMapper.insertAccountInfoDrs_LastBackUp();
            accountMapper.deleteAccountInfoDrs();
            for(AccountInfoDrs accountInfo : accountInfoDrs){
                accountMapper.insertInfo(accountInfo);
            }
            return ResponseObject.INSERT_DATA_SUCCESS;
//            int totalRecords = accountInfoDrs.size();
//            int numberRound = totalRecords / 1000;
//
//            if (numberRound < 1){
//                for(AccountInfoDrs accountInfo : accountInfoDrs){
//                    accountMapper.insertInfo(accountInfo);
//                }
//                return ResponseObject.INSERT_DATA_SUCCESS;
//            }else {
//
//                for (int round = 1 ; round<= numberRound ;round++ ){
//                    for ( int m =round* 1000 - 1000 ; m< round*1000; m ++){
//                        listTemp.add(accountInfoDrs.get(m));
//                    }
//                    accountMapper.importListAccountInfoDrs(listTemp);
//                    listTemp.clear();
//                }
//
//                for ( int z = numberRound * 1000 ; z< totalRecords ;z++){
//                    listTemp.add(accountInfoDrs.get(z));
//                }
//                accountMapper.importListAccountInfoDrs(listTemp);
//                return ResponseObject.INSERT_DATA_SUCCESS;
//            }
        }
    }

    public ResponseObject updateListAccountInfoDrs(List<AccountInfoDrs> accountInfoDrs) {
        try {
            if (accountInfoDrs.size() >0){
                for (AccountInfoDrs item : accountInfoDrs) {
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
