package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.RejectUploadFileMapper;
import com.dou.accounting.mappers.UploadFileMapper;
import com.dou.accounting.models.RejectUploadFileModel;
import com.dou.accounting.models.UploadFileModel;
import com.dou.accounting.services.RejectUploadFileInterface;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RejectUploadFileService implements RejectUploadFileInterface {

    @Autowired
    RejectUploadFileMapper mapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public Map getRejectUploadFileData(RejectUploadFileModel rejectUploadFileModel) {
        int countAllDataInRejectUpload = mapper.countAllDataInRejectUpload(rejectUploadFileModel);
        Map rs = new HashMap();
        rs.put("data",mapper.getRejectUploadFileData(rejectUploadFileModel));
        rs.put("totalPages",countAllDataInRejectUpload);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportRejectUploadFile(RejectUploadFileModel rejectUploadFileModel) {
        try {
            List<RejectUploadFileModel> result =  mapper.exportRejectUploadFile(rejectUploadFileModel);

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"AGREEMENTID", "AGREEMENTNO", "BATCHID", "RECEIPTNO", "CHEQUE_NO",
                    "BANKACNUM", "DRAWNON", "TOWARDS", "RECEIPT_AMT", "RECEIPT_DATE",
                    "USERID", "UPLOAD_DATE", "REASON"});

            for (int i = 0; i < result.size(); i++) {
                data.add(new String[]{result.get(i).getAgreementId(), result.get(i).getAgreementNo(),
                        result.get(i).getBatchId(), result.get(i).getReceiptNo(), result.get(i).getChequeNo(),
                        result.get(i).getBankAcnum(), result.get(i).getDrawNon(), result.get(i).getToWards(),
                        result.get(i).getReceiptAmt(), result.get(i).getReceiptDate(), result.get(i).getUserId(),
                        result.get(i).getUploadDate(), result.get(i).getReaSon()});
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
