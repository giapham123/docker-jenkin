package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.WriteOffMapper;
import com.dou.accounting.models.WriteOffModel;
import com.dou.accounting.services.WriteOffInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
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
public class WriteOffService implements WriteOffInterface {
    @Autowired
    private WriteOffMapper _writeOffMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public Map getListDataWriteOff(WriteOffModel writeOffModel){

        Map rs = new HashMap();
        rs.put("data",_writeOffMapper.getWriteOffData(writeOffModel));
        rs.put("totalPages",_writeOffMapper.countAllDataWriteOff(writeOffModel));
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportWriteOffData(WriteOffModel writeOffModel){
        try {
            List<WriteOffModel> result  = _writeOffMapper.getWriteOffDataForExport(writeOffModel);

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            List<String[]> data = new ArrayList<String[]>();

            data.add(new String[]{"agreementid", "agreementno", "out_date", "AMTFIN", "PRINCIPLE_BF",
                    "INTEREST_BF", "LPP_BF", "PRINCIPLE_AT", "INTEREST_AT",
                    "LPP_AT", "TOTAL_PRINCIPLE", "TOTAL_INTEREST", "TOTAL_LPP","PAID","WAIVEOFF_AMT","TOTAL_EXCESS", "total_net_receivable", "insert_date"});

            for (int i = 0; i < result.size(); i++) {
                data.add(new String[]{result.get(i).getAgreementId(), result.get(i).getAgreementNo(),
                        result.get(i).getOutDate(), result.get(i).getAmtFin(), result.get(i).getPrincipleBf(),
                        result.get(i).getInterestBf(), result.get(i).getLppBf(), result.get(i).getPrincipleAt(),
                        result.get(i).getInterestAt(), result.get(i).getLppAt(), result.get(i).getTotalPrinciple(),
                        result.get(i).getTotalInterest(), result.get(i).getTotalLpp(),
                        result.get(i).getPaid(),result.get(i).getWaiveoffAmt(),result.get(i).getTotalExcess(),result.get(i).getTotalNetReceivable(),
                        result.get(i).getInsertDt()});
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
