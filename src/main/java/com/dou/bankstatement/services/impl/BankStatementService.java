package com.dou.bankstatement.services.impl;

import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.mappers.BankStatementMapper;
import com.dou.bankstatement.models.BankStatementModel;
import com.dou.bankstatement.services.BankStatementInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BankStatementService implements BankStatementInterface {

    @Autowired
    BankStatementMapper bankStatementMapper;

    @Override
    public ResponseObject getDataBankStatement(BankStatementModel model) {
        ResponseObject rs = new ResponseObject();
        try{
            List<BankStatementModel> result = bankStatementMapper.getDataBankStatement(model);
            for(BankStatementModel modelRs: result){
                String statementDtInfile = modelRs.getFileName().split("_")[1];
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                String cunvertCurrentDate=statementDtInfile;
                Date date = new Date();
                date = df.parse(cunvertCurrentDate);
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String strDate = dateFormat.format(date);
                modelRs.setStatmentDtInFileName(strDate);
            }
            rs.setData(result);
            rs.setSuccess(true);
            rs.setMessage("Success!");
        }catch (Exception e){
            System.out.println(e);
            rs.setSuccess(false);
            rs.setMessage("Error Failed!");
        }
        return rs;
    }

    @Override
    public ResponseObject getBank() {
        ResponseObject rs = new ResponseObject();
        List<String> lsBank = bankStatementMapper.getBank();
        List<String> newList = new ArrayList<>();
        if(lsBank.size() != 0){
            for (String element : lsBank) {
                if (!newList.contains(element)) {
                    newList.add(element);
                }
            }
        }
        rs.setData(newList);
        rs.setSuccess(true);
        return rs;
    }
}
