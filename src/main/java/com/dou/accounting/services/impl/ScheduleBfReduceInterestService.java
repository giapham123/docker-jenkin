package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.ScheduleBfReduceInterestMapper;
import com.dou.accounting.models.ScheduleBfReduceInterestModel;
import com.dou.accounting.services.ScheduleBfReduceInterestInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleBfReduceInterestService implements ScheduleBfReduceInterestInterface {

    @Autowired
    ScheduleBfReduceInterestMapper scheduleBfReduceInterestMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getReduceInterest(ScheduleBfReduceInterestModel model) {
        ResponseObject rs = new ResponseObject();
        Map result = new HashMap();
        List<ScheduleBfReduceInterestModel> resultReduceInterest = scheduleBfReduceInterestMapper.getReduceInterest(model);
        int totalPage = scheduleBfReduceInterestMapper.countAllDataRepayment(model);

        result.put("totalPage", totalPage);
        result.put("resultData", resultReduceInterest);

        rs.setData(result);
        rs.setSuccess(true);
        rs.setMessage(null);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getRepayment(ScheduleBfReduceInterestModel model) {
        ResponseObject rs = new ResponseObject();
        List<ScheduleBfReduceInterestModel> result = scheduleBfReduceInterestMapper.getRepayment(model);
        rs.setData(result);
        rs.setSuccess(true);
        rs.setMessage(null);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportRepayment(ScheduleBfReduceInterestModel model) {
        try {
            List<ScheduleBfReduceInterestModel> result =  scheduleBfReduceInterestMapper.getAllRepayment(model);

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//            Date date = new Date();

            List<String[]> data = new ArrayList<String[]>();
//            data.add(new String[]{"Repayment"});
//            data.add(new String[]{"Generated On (Date & Time): " + formatter.format(date)});
            data.add(new String[]{"APP_ID_C", "INSTLNUM", "DUEDATE", "INSTLAMT", "PRINCOMP",
                    "INTCOMP", "EFFRATE", "BALPRIN", "DAYS"});

            for (int i = 0; i < result.size(); i++) {
                data.add(new String[]{result.get(i).getProposalId(),result.get(i).getInstlNum(),result.get(i).getDueDate(),
                        result.get(i).getInstlAmt(),result.get(i).getPrintComp(),result.get(i).getIntComp(),result.get(i).getEffRate(),
                        result.get(i).getBalPrin(),result.get(i).getDays()});
            }
            writer.writeAll(data);

            File file1 = new File(a);
            writer.close();
            byte[] dataBase64 = FileUtils.readFileToByteArray(file1);
            return dataBase64;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static HttpServletRequest getCurrentRequest() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes)
                return ((ServletRequestAttributes) requestAttributes).getRequest();
        } catch (Exception e) {
            // Ignore case
        }
        return null;
    }
}
