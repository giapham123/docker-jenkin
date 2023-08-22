package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.CollectorCompareMapper;
import com.dou.accounting.models.CollectorCompareModel;
import com.dou.accounting.models.SigProcess;
import com.dou.accounting.services.CollectorCompareInterface;
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
public class CollectorCompareService implements CollectorCompareInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectorCompareService.class);

    @Autowired
    CollectorCompareMapper collectorCompareMapper;

    SigProcess getCollCompare= SigProcess.getInstance();

    @Override
    public ResponseObject collectorCompare(String compareDt) throws ParseException {
        getCollCompare.setProcessCollCompare(true);
        ResponseObject rs = new ResponseObject();
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            String sDate1= formatter.format(date);
            Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(sDate1);
            Map<String, Object> param = new HashMap<>();
            param.put("curDt",date1);
            param.put("out", new String());
            collectorCompareMapper.collectorCompare(param);
            String isSuccess = (String) param.get("out");
            if(isSuccess.equals("1")){
                rs.setSuccess(true);
                rs.setMessage("Compare Success");
            }else{
                rs.setSuccess(false);
                rs.setMessage("Compare Failed");
            }
        }catch (Exception e){
            LOGGER.error(e.toString());
            System.out.println(e);
            rs.setSuccess(false);
            rs.setMessage("Error System");
        }
        getCollCompare.setProcessCollCompare(false);
        return rs;
    }

    @Override
    public ResponseObject getCollectorCompare(CollectorCompareModel model) {
        ResponseObject rs = new ResponseObject();
        rs.setData(collectorCompareMapper.getCollectorCompare(model));
        rs.setSuccess(true);
        return rs;
    }

    @Override
    public ResponseObject getProcess() {
        ResponseObject rs = new ResponseObject();
        rs.setData(getCollCompare.getProcessCollCompare());
        return rs;
    }
}
