package com.dou.staff_info.services.impl;


import com.dou.adm.shared.ResponseObject;
import com.dou.staff_info.mappers.AccountInfoSalesOperationMapper;
import com.dou.staff_info.models.AccountInfoSalesOperation;
import com.dou.staff_info.services.AccountInfoSalesOperationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountInfoSalesOperationService implements AccountInfoSalesOperationServiceInterface {


    @Autowired
    private AccountInfoSalesOperationMapper accountMapper;

    public ResponseObject<List<AccountInfoSalesOperation>> getAll(String supervisorId) {
        List<AccountInfoSalesOperation> list = accountMapper.getAll(supervisorId);
        return new ResponseObject(list);
    }

    public ResponseObject<List<AccountInfoSalesOperation>> getInfo(AccountInfoSalesOperation AccountInfoSalesOperation) {
        List<AccountInfoSalesOperation> list = accountMapper.getInfo(AccountInfoSalesOperation);
        return new ResponseObject(list);
    }

    public ResponseObject insertInfo(AccountInfoSalesOperation accountInfoDrs) {
        List<AccountInfoSalesOperation> listCheck = accountMapper.checkExistAccountId(accountInfoDrs.getAccountId());
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

    public ResponseObject updateInfo(AccountInfoSalesOperation AccountInfoSalesOperation) {
        try {
            accountMapper.updateInfo(AccountInfoSalesOperation);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteInfo(AccountInfoSalesOperation AccountInfoSalesOperation) {
        try {
            accountMapper.delete(AccountInfoSalesOperation);
            return ResponseObject.UPDATE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    public ResponseObject deleteAccountInfoSalesOperation() {
        try {
            accountMapper.deleteAccountInfoSalesOperation();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }
    public ResponseObject insertAccountInfoSalesOperation_LastBackUp() {
        try {
            accountMapper.insertAccountInfoSalesOperation_LastBackUp();
            return ResponseObject.INSERT_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }
    public ResponseObject deleteAccountInfoSalesOperation_LastBackUp() {
        try {
            accountMapper.deleteAccountInfoSalesOperation_LastBackUp();
            return ResponseObject.DELETE_DATA_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject importListAccountInfoSalesOperation(List<AccountInfoSalesOperation> accountInfoDrs) {

        List<AccountInfoSalesOperation> listExist = new ArrayList<>();
        LinkedList<AccountInfoSalesOperation>  listTemp = new LinkedList<>();
        Set<String> listAccount = new HashSet<>();

        for (AccountInfoSalesOperation element: accountInfoDrs) {
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
            accountMapper.deleteAccountInfoSalesOperation_LastBackUp();
            accountMapper.insertAccountInfoSalesOperation_LastBackUp();
            accountMapper.deleteAccountInfoSalesOperation();
            for(AccountInfoSalesOperation accountInfoSalesOperation : accountInfoDrs){
                accountMapper.insertInfo(accountInfoSalesOperation);
            }
            return ResponseObject.INSERT_DATA_SUCCESS;
            /*int totalRecords = accountInfoDrs.size();
            int numberRound = totalRecords / 1000;

            if (numberRound < 1){
                //accountMapper.importListAccountInfoSalesOperation(accountInfoDrs);
                for(AccountInfoSalesOperation accountInfoSalesOperation : accountInfoDrs){
                    accountMapper.insertInfo(accountInfoSalesOperation);
                }
                return ResponseObject.INSERT_DATA_SUCCESS;
            }else {

                for (int round = 1 ; round<= numberRound ;round++ ){
                    for ( int m =round* 1000 - 1000 ; m< round*1000; m ++){
                        listTemp.add(accountInfoDrs.get(m));
                    }
                    accountMapper.importListAccountInfoSalesOperation(listTemp);
                    listTemp.clear();
                }
//                listTemp.clear();

                for ( int z = numberRound * 1000 ; z< totalRecords ;z++){
                    listTemp.add(accountInfoDrs.get(z));
                }
                accountMapper.importListAccountInfoSalesOperation(listTemp);
                return ResponseObject.INSERT_DATA_SUCCESS;
            }*/
        }
    }

    public ResponseObject updateListAccountInfoSalesOperation(List<AccountInfoSalesOperation> accountInfoDrs) {
        try {
            if (accountInfoDrs.size() >0){
                for (AccountInfoSalesOperation item : accountInfoDrs) {
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
