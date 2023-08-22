package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.BackDateWOMapper;
import com.dou.accounting.models.BackDateWOModel;
import com.dou.accounting.services.BackDateWOInterface;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackDateWOService implements BackDateWOInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackDateWOService.class);

    @Autowired
    BackDateWOMapper backDateWOMapper;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataBackDate(String date, String type) throws ParseException {
        ResponseObject rs = new ResponseObject();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date0 = new Date();
        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(date);
        List<BackDateWOModel> rsData = backDateWOMapper.getDataBackDate(date1,type);
        if(rsData.size() != 0){
            rs.setData(rsData);
            rs.setSuccess(true);
            rs.setMessage("Get Data Back Date Success");
        }else{
            rs.setData(rsData);
            rs.setSuccess(false);
            rs.setMessage("Have no data");
        }
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject processPendingBackDate(String type,String date, String usgLg) throws ParseException {
        ResponseObject rs = new ResponseObject();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date0 = new Date();
            String sDate1 = formatter.format(date0);
            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(date);
            Map<String, Object> param = new HashMap<>();
            param.put("curDt", date1);
            param.put("type", type);
            param.put("usrLg", usgLg);
            param.put("out", new String());
            backDateWOMapper.processPendingBackDate(param);
            String isSuccess = (String) param.get("out");
            if (isSuccess.equals("Success")) {
                rs.setSuccess(true);
                rs.setMessage("Process Completed");
            }
            if (isSuccess.equals("No appids meet the conditions")) {
                rs.setSuccess(true);
                rs.setMessage(isSuccess);
            } else {
                rs.setSuccess(false);
                rs.setMessage(isSuccess);
            }
        }catch (Exception e){
            LOGGER.error(e.toString());
            System.out.println(e);
            rs.setSuccess(false);
            rs.setMessage("Error System");
        }
        return rs;
    }
}
