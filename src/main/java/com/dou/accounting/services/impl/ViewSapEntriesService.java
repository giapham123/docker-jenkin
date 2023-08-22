package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.ViewSapEntriesMapper;
import com.dou.accounting.models.SigProcess;
import com.dou.accounting.models.ViewSapEntriesModel;
import com.dou.accounting.services.ViewSapEntriesInterface;
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
import java.util.Map;

@Service
public class ViewSapEntriesService implements ViewSapEntriesInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewSapEntriesService.class);

    @Autowired
    ViewSapEntriesMapper viewSapEntriesMapper;

    SigProcess isProcess= SigProcess.getInstance();

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataViewSapEntries(ViewSapEntriesModel model) {
        ResponseObject rs = new ResponseObject();
        rs.setData(viewSapEntriesMapper.getDataViewSapEntries(model));
        rs.setSuccess(true);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject pushSap(String userLogin) throws ParseException {
        isProcess.setPutSap(true);
        ResponseObject rs = new ResponseObject();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            String sDate1 = formatter.format(date);
            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(sDate1);
            Map<String, Object> param = new HashMap<>();
            param.put("curDt", date1);
            param.put("usrLg", userLogin);
            param.put("out", new String());
            viewSapEntriesMapper.pushSap(param);
            String isSuccess = (String) param.get("out");
            if (isSuccess.equals("Success")) {
                rs.setSuccess(true);
                rs.setMessage(isSuccess);
            } else if (isSuccess.equals("Sap template all data already pushed into SAP DB")){
                rs.setSuccess(false);
                rs.setMessage(isSuccess);
            }
            else {
                rs.setSuccess(false);
                rs.setMessage("Push SAP Failed");
            }
        }catch (Exception e){
            LOGGER.error(e.toString());
            System.out.println(e);
            rs.setSuccess(false);
            rs.setMessage("Push SAP Failed");
        }
        isProcess.setPutSap(false);
        return rs;
    }

    @Override
    public ResponseObject getProcess() {
        ResponseObject rs = new ResponseObject();
        rs.setData(isProcess.getAuthGenSap());
        return rs;
    }
}
