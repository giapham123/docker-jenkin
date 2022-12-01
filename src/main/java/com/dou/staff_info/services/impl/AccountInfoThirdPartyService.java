package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoThirdPartyMapper;
import com.dou.staff_info.models.AccountInfoThirdParty;
import com.dou.staff_info.services.AccountInfoThirdPartyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountInfoThirdPartyService implements AccountInfoThirdPartyServiceInterface {


    @Autowired
    private AccountInfoThirdPartyMapper accountMapper;

    public ResponseObject<List<AccountInfoThirdParty>> getAll(String supervisorId) {
        List<AccountInfoThirdParty> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoThirdParty>> getInfo(AccountInfoThirdParty AccountInfoThirdParty) {
        List<AccountInfoThirdParty> list = accountMapper.getInfo(AccountInfoThirdParty);
        return new ResponseObject(list);
    }

    public ResponseObject insertInfo(AccountInfoThirdParty accountInfoDrs) {
        List<AccountInfoThirdParty> listCheck = accountMapper.checkExistAccountId(accountInfoDrs.getAccountId());
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

    public ResponseObject updateInfo(AccountInfoThirdParty AccountInfoThirdParty) {
        try {
            accountMapper.updateInfo(AccountInfoThirdParty);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteInfo(AccountInfoThirdParty AccountInfoThirdParty) {
        try {
            accountMapper.delete(AccountInfoThirdParty);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteAccountInfoThirdParty() {
        try {
            accountMapper.deleteAccountInfoThirdParty();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
    public ResponseObject insertAccountInfoThirdParty_LastBackUp() {
        try {
            accountMapper.insertAccountInfoThirdParty_LastBackUp();
            return ResponseObject.INSERT_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }
    public ResponseObject deleteAccountInfoThirdParty_LastBackUp() {
        try {
            accountMapper.deleteAccountInfoThirdParty_LastBackUp();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject importListAccountInfoThirdParty(List<AccountInfoThirdParty> accountInfoDrs) {

        List<AccountInfoThirdParty> listExist = new ArrayList<>();
        LinkedList<AccountInfoThirdParty>  listTemp = new LinkedList<>();
        Set<String> listAccount = new HashSet<>();

        for (AccountInfoThirdParty element: accountInfoDrs) {
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
            accountMapper.deleteAccountInfoThirdParty_LastBackUp();
            accountMapper.insertAccountInfoThirdParty_LastBackUp();
            accountMapper.deleteAccountInfoThirdParty();
            for(AccountInfoThirdParty accountInfoThirdParty : accountInfoDrs){
                accountMapper.insertInfo(accountInfoThirdParty);
            }
            return ResponseObject.INSERT_DATA_SUCCESS;
            /*int totalRecords = accountInfoDrs.size();
            int numberRound = totalRecords / 1000;

            if (numberRound < 1){
                //accountMapper.importListAccountInfoThirdParty(accountInfoDrs);
                for(AccountInfoThirdParty accountInfoThirdParty : accountInfoDrs){
                    accountMapper.insertInfo(accountInfoThirdParty);
                }
                return ResponseObject.INSERT_DATA_SUCCESS;
            }else {

                for (int round = 1 ; round<= numberRound ;round++ ){
                    for ( int m =round* 1000 - 1000 ; m< round*1000; m ++){
                        listTemp.add(accountInfoDrs.get(m));
                    }
                    accountMapper.importListAccountInfoThirdParty(listTemp);
                    listTemp.clear();
                }
//                listTemp.clear();

                for ( int z = numberRound * 1000 ; z< totalRecords ;z++){
                    listTemp.add(accountInfoDrs.get(z));
                }
                accountMapper.importListAccountInfoThirdParty(listTemp);
                return ResponseObject.INSERT_DATA_SUCCESS;
            }*/
        }
    }

    public ResponseObject updateListAccountInfoThirdParty(List<AccountInfoThirdParty> accountInfoDrs) {
        try {
            if (accountInfoDrs.size() >0){
                for (AccountInfoThirdParty item : accountInfoDrs) {
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
