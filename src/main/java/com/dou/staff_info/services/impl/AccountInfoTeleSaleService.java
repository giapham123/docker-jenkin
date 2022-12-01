package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoTeleSaleMapper;
import com.dou.staff_info.models.AccountInfoTeleSale;
import com.dou.staff_info.services.AccountInfoTeleSaleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountInfoTeleSaleService implements AccountInfoTeleSaleServiceInterface {


    @Autowired
    private AccountInfoTeleSaleMapper accountMapper;

    public ResponseObject<List<AccountInfoTeleSale>> getAll(String supervisorId) {
        List<AccountInfoTeleSale> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoTeleSale>> getInfo(AccountInfoTeleSale AccountInfoTeleSale) {
        List<AccountInfoTeleSale> list = accountMapper.getInfo(AccountInfoTeleSale);
        return new ResponseObject(list);
    }

    public ResponseObject insertInfo(AccountInfoTeleSale accountInfoDrs) {
        List<AccountInfoTeleSale> listCheck = accountMapper.checkExistAccountId(accountInfoDrs.getAccountId());
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

    public ResponseObject updateInfo(AccountInfoTeleSale AccountInfoTeleSale) {
        try {
            accountMapper.updateInfo(AccountInfoTeleSale);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteInfo(AccountInfoTeleSale AccountInfoTeleSale) {
        try {
            accountMapper.delete(AccountInfoTeleSale);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteAccountInfoTeleSale() {
        try {
            accountMapper.deleteAccountInfoTeleSale();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
    public ResponseObject insertAccountInfoTeleSale_LastBackUp() {
        try {
            accountMapper.insertAccountInfoTeleSale_LastBackUp();
            return ResponseObject.INSERT_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }
    public ResponseObject deleteAccountInfoTeleSale_LastBackUp() {
        try {
            accountMapper.deleteAccountInfoTeleSale_LastBackUp();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject importListAccountInfoTeleSale(List<AccountInfoTeleSale> accountInfoDrs) {

        List<AccountInfoTeleSale> listExist = new ArrayList<>();
        LinkedList<AccountInfoTeleSale>  listTemp = new LinkedList<>();
        Set<String> listAccount = new HashSet<>();

        for (AccountInfoTeleSale element: accountInfoDrs) {
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
            accountMapper.deleteAccountInfoTeleSale_LastBackUp();
            accountMapper.insertAccountInfoTeleSale_LastBackUp();
            accountMapper.deleteAccountInfoTeleSale();
            for(AccountInfoTeleSale accountInfoTeleSale : accountInfoDrs){
                accountMapper.insertInfo(accountInfoTeleSale);
            }
            return ResponseObject.INSERT_DATA_SUCCESS;
            /*int totalRecords = accountInfoDrs.size();
            int numberRound = totalRecords / 1000;

            if (numberRound < 1){
                //accountMapper.importListAccountInfoTeleSale(accountInfoDrs);
                for(AccountInfoTeleSale accountInfoTeleSale : accountInfoDrs){
                    accountMapper.insertInfo(accountInfoTeleSale);
                }
                return ResponseObject.INSERT_DATA_SUCCESS;
            }else {

                for (int round = 1 ; round<= numberRound ;round++ ){
                    for ( int m =round* 1000 - 1000 ; m< round*1000; m ++){
                        listTemp.add(accountInfoDrs.get(m));
                    }
                    accountMapper.importListAccountInfoTeleSale(listTemp);
                    listTemp.clear();
                }
//                listTemp.clear();

                for ( int z = numberRound * 1000 ; z< totalRecords ;z++){
                    listTemp.add(accountInfoDrs.get(z));
                }
                accountMapper.importListAccountInfoTeleSale(listTemp);
                return ResponseObject.INSERT_DATA_SUCCESS;
            }*/
        }
    }

    public ResponseObject updateListAccountInfoTeleSale(List<AccountInfoTeleSale> accountInfoDrs) {
        try {
            if (accountInfoDrs.size() >0){
                for (AccountInfoTeleSale item : accountInfoDrs) {
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
