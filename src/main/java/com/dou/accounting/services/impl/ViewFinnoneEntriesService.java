package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.ViewFinnoneEntriesMapper;
import com.dou.accounting.models.ViewFinnoneEntriesModel;
import com.dou.accounting.services.ViewFinnoneEntriesInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ViewFinnoneEntriesService implements ViewFinnoneEntriesInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewFinnoneEntriesService.class);

    @Autowired
    ViewFinnoneEntriesMapper viewFinnoneEntriesMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataViewFinnoneEntries(ViewFinnoneEntriesModel model) {
        ResponseObject rs = new ResponseObject();
        Map rsMap = new HashMap();
        rsMap.put("data",viewFinnoneEntriesMapper.getDataViewFinnoneEntries(model));
        rsMap.put("totalPages",viewFinnoneEntriesMapper.countAllDataViewFinnoneEntries(model));
        rs.setData(rsMap);
        rs.setSuccess(true);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] dataForExport(ViewFinnoneEntriesModel model) {
        try {
            List<ViewFinnoneEntriesModel> result =  viewFinnoneEntriesMapper.dataForExport(model);

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            List<String[]> data = new ArrayList<String[]>();
//            data.add(new String[]{"View _Finnone_Entries "});
//            data.add(new String[]{"Generated On (Date & Time): " + formatter.format(date)});
            data.add(new String[]{"AgreementId", "Debit Amt", "Credit Amt", "Trans Date", "Sap GL",
                    "Remarks", "Lea VoucherId", "Note", "Update Date", "SapYN"});

            for (int i = 0; i < result.size(); i++) {
                data.add(new String[]{result.get(i).getAgreementId(),result.get(i).getDebitAmt(),result.get(i).getCreditAmt(),
                        result.get(i).getTransDt(),result.get(i).getSapGl(),result.get(i).getRemarks(),
                        result.get(i).getLeaVoucherId(),result.get(i).getNote(),result.get(i).getUpdateDt(),result.get(i).getSapYn()
                });
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
