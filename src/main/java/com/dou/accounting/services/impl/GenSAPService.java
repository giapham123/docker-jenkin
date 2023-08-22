package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.GenSAPMapper;
import com.dou.accounting.models.GenSAPModel;
import com.dou.accounting.models.SigProcess;
import com.dou.accounting.services.GenSAPInterface;
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
public class GenSAPService implements GenSAPInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenSAPService.class);

    @Autowired
    GenSAPMapper genSAPMapper;

    SigProcess isProcess= SigProcess.getInstance();

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getGenSAPData(GenSAPModel model) {
        ResponseObject rs = new ResponseObject();
        rs.setData(genSAPMapper.getGenSAPData(model));
        rs.setSuccess(true);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject authorGenSap() throws ParseException {
        isProcess.setAuthGenSap(true);
        ResponseObject rs = new ResponseObject();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            String sDate1 = formatter.format(date);
            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(sDate1);
            Map<String, Object> param = new HashMap<>();
            param.put("curDt", date1);
            param.put("out", new String());
            genSAPMapper.authorGenSap(param);
            String isSuccess = (String) param.get("out");
            if (isSuccess.equals("Success")) {
                rs.setSuccess(true);
                rs.setMessage("Authorize Generate SAP Completed");
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
        isProcess.setAuthGenSap(false);
        return rs;
    }

    @Override
    public ResponseObject getProcess() {
        ResponseObject rs = new ResponseObject();
        rs.setData(isProcess.getAuthGenSap());
        return rs;
    }
}
