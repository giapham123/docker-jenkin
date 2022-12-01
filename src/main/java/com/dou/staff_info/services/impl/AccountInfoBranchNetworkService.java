package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoBranchNetworkMapper;
import com.dou.staff_info.models.AccountInfoBranchNetwork;
import com.dou.staff_info.services.AccountInfoBranchNetworkServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountInfoBranchNetworkService implements AccountInfoBranchNetworkServiceInterface {


    @Autowired
    private AccountInfoBranchNetworkMapper accountMapper;

    public ResponseObject<List<AccountInfoBranchNetwork>> getAll(String supervisorId) {
        List<AccountInfoBranchNetwork> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoBranchNetwork>> getInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork) {
        List<AccountInfoBranchNetwork> list = accountMapper.getInfo(AccountInfoBranchNetwork);
        return new ResponseObject(list);
    }

    public ResponseObject insertInfo(AccountInfoBranchNetwork accountInfoDrs) {
        List<AccountInfoBranchNetwork> listCheck = accountMapper.checkExistAccountId(accountInfoDrs.getAccountId());
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

    public ResponseObject updateInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork) {
        try {
            accountMapper.updateInfo(AccountInfoBranchNetwork);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteInfo(AccountInfoBranchNetwork AccountInfoBranchNetwork) {
        try {
            accountMapper.delete(AccountInfoBranchNetwork);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteAccountInfoBranchNetwork() {
        try {
            accountMapper.deleteAccountInfoBranchNetwork();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
    public ResponseObject insertAccountInfoBranchNetwork_LastBackUp() {
        try {
            accountMapper.insertAccountInfoBranchNetwork_LastBackUp();
            return ResponseObject.INSERT_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }
    public ResponseObject deleteAccountInfoBranchNetwork_LastBackUp() {
        try {
            accountMapper.deleteAccountInfoBranchNetwork_LastBackUp();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject importListAccountInfoBranchNetwork(List<AccountInfoBranchNetwork> accountInfoDrs) {
        List<AccountInfoBranchNetwork> listExist = new ArrayList<>();
        LinkedList<AccountInfoBranchNetwork>  listTemp = new LinkedList<>();
        Set<String> listAccount = new HashSet<>();

        for (AccountInfoBranchNetwork element: accountInfoDrs) {
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
            accountMapper.deleteAccountInfoBranchNetwork_LastBackUp();
            accountMapper.insertAccountInfoBranchNetwork_LastBackUp();
            accountMapper.deleteAccountInfoBranchNetwork();
            for(AccountInfoBranchNetwork accountInfoBranchNetwork : accountInfoDrs){
                accountMapper.insertInfo(accountInfoBranchNetwork);
            }
            return ResponseObject.INSERT_DATA_SUCCESS;
            /*int totalRecords = accountInfoDrs.size();
            int numberRound = totalRecords / 1000;

            if (numberRound < 1){
                //accountMapper.importListAccountInfoBranchNetwork(accountInfoDrs);
                for(AccountInfoBranchNetwork accountInfoBranchNetwork : accountInfoDrs){
                    accountMapper.insertInfo(accountInfoBranchNetwork);
                }
                return ResponseObject.INSERT_DATA_SUCCESS;
            }else {

                for (int round = 1 ; round<= numberRound ;round++ ){
                    for ( int m =round* 1000 - 1000 ; m< round*1000; m ++){
                        listTemp.add(accountInfoDrs.get(m));
                    }
                    accountMapper.importListAccountInfoBranchNetwork(listTemp);
                    listTemp.clear();
                }
//                listTemp.clear();

                for ( int z = numberRound * 1000 ; z< totalRecords ;z++){
                    listTemp.add(accountInfoDrs.get(z));
                }
                accountMapper.importListAccountInfoBranchNetwork(listTemp);
                return ResponseObject.INSERT_DATA_SUCCESS;
            }*/
        }
    }

    public ResponseObject updateListAccountInfoBranchNetwork(List<AccountInfoBranchNetwork> accountInfoDrs) {
        try {
            if (accountInfoDrs.size() >0){
                for (AccountInfoBranchNetwork item : accountInfoDrs) {
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
