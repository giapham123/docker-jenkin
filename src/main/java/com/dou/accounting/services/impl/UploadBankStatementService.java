package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.UploadBankStatementMapper;
import com.dou.accounting.models.UploadBankStatementModel;
import com.dou.accounting.services.UploadBankStatementInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UploadBankStatementService implements UploadBankStatementInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadBankStatementService.class);

    @Autowired
    UploadBankStatementMapper uploadBankStatementMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    @Transactional
    public ResponseObject insertUploadBankStatement(MultipartFile excelFile, String userLogin) {
        ResponseObject rs = new ResponseObject();
        try {
            List<UploadBankStatementModel> dataUpload = new ArrayList<>();
            if(excelFile != null) {
                XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
                XSSFSheet worksheet = workbook.getSheetAt(0);
                DataFormatter formatter = new DataFormatter();
                int rownNum = worksheet.getPhysicalNumberOfRows();
                for (int i = 1; i <= rownNum; i++) {
                    UploadBankStatementModel model = new UploadBankStatementModel();
                    XSSFRow row = worksheet.getRow(i);
                    try {
                        model.setAppId(formatter.formatCellValue(row.getCell(0)).toUpperCase().trim());
                        model.setStatementDate(row.getCell(1).getDateCellValue());
                        model.setDetail(formatter.formatCellValue(row.getCell(2)).trim());
                        model.setTxnNo(formatter.formatCellValue(row.getCell(3)).toUpperCase().trim());
                        model.setDescAcc(formatter.formatCellValue(row.getCell(4)).trim());
                        model.setDebitAmt(formatter.formatCellValue(row.getCell(5)).trim());
                        model.setUserLogin(userLogin);
                        dataUpload.add(model);
                    } catch (NullPointerException e) {
                        System.out.println(e);

                    }catch (Exception e){
                        System.out.println(e);
                        rs.setData(null);
                        rs.setMessage("Please check format data!");
                        rs.setSuccess(false);
                        return rs;
                    }
                }
                Boolean isSuccessFile = true;
                for(int i = 0; i< dataUpload.size(); i++) {
                    String reason = "";
                    if(dataUpload.get(i).getTxnNo() == null || dataUpload.get(i).getTxnNo() == ""){
                        reason += "Txn_no is empty, ";
                    }else {
                        int isExistTxnNo = uploadBankStatementMapper.isExistTxnNo(dataUpload.get(i).getTxnNo(), dataUpload.get(i).getStatementDate());
                        if(isExistTxnNo != 0){
                           reason += "Txn_no is exist in System, ";
                        }

                        for (int j = 0; j < dataUpload.size(); j++) {
                            if (i != j) {
                                if (dataUpload.get(i).getTxnNo() != null && dataUpload.get(i).getTxnNo() != "") {
                                    if (dataUpload.get(i).getTxnNo().equals(dataUpload.get(j).getTxnNo())) {
                                        reason += "Duplicate TXN_NO, ";
                                    }
                                }
                            }
                        }
                    }
                    if(dataUpload.get(i).getAppId() == null || dataUpload.get(i).getAppId() == ""){
                        reason += "Appid is empty, ";
                    }else{
                        try{
                            int isExistAppId = uploadBankStatementMapper.isExistAgreementId(dataUpload.get(i).getAppId());
                            if(isExistAppId == 0){
                                reason += "Appid is not Exist in system, ";
                            }
                            for (int j = 0; j < dataUpload.size(); j++) {
                                if (i != j) {
                                    if (dataUpload.get(i).getAppId() != null && dataUpload.get(i).getAppId() != "") {
                                        if (dataUpload.get(i).getAppId().equals(dataUpload.get(j).getAppId())) {
                                            reason += "Duplicate AppId, ";
                                        }
                                    }
                                }
                            }
                        }catch (Exception e){
                            System.out.println(e);
                            reason += "Please check AppId, ";
                        }
                    }
                    if(dataUpload.get(i).getStatementDate() == null || dataUpload.get(i).getStatementDate().equals("")){
                        reason += "Statement Date is empty, ";
                    }else{
                        Date currentDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        int isCompare = sdf.format(dataUpload.get(i).getStatementDate()).compareTo(sdf.format(currentDate));
                        if(isCompare > 0){
                            reason += "Statement Date is over current date, ";
                        }else{
                            for (int j = 0; j < dataUpload.size(); j++) {
                                if (i != j) {
                                    Boolean isCurrentDt = sdf.format(dataUpload.get(i).getStatementDate()).equals(sdf.format(dataUpload.get(j).getStatementDate()));
                                    if (!isCurrentDt) {
                                        reason += "Statement date is not the same, ";
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if(dataUpload.get(i).getDetail() == null || dataUpload.get(i).getDetail() == ""){
                        reason += "Detail is empty, ";
                    }
                    if(dataUpload.get(i).getDescAcc() == null || dataUpload.get(i).getDescAcc() == ""){
                        reason += "Desc_acc is empty, ";
                    }
                    if(dataUpload.get(i).getDebitAmt() == null || dataUpload.get(i).getDebitAmt() == ""){
                        reason += "Debit_amt is empty";
                    }
                    StringBuffer sb= new StringBuffer(reason.trim());
                    if(reason != ""){
                        isSuccessFile = false;
                        sb.deleteCharAt(sb.length()-1);
                    }

                    dataUpload.get(i).setReason(String.valueOf(sb));
                }
                if(isSuccessFile){
                    for(UploadBankStatementModel model: dataUpload){
                        uploadBankStatementMapper.deleteDataInBankStatement(model.getStatementDate());
                    }
                    for(UploadBankStatementModel model: dataUpload){
                        uploadBankStatementMapper.insertDataUploadBankStatement(model);
                    }
                    rs.setData(dataUpload);
                    rs.setSuccess(true);
                    rs.setMessage("Import Complete!");
                }else{
                    rs.setData(dataUpload);
                    rs.setSuccess(false);
                    rs.setMessage("Import Failed, Please Check Excel File!");
                }
            }
        }catch (Exception e) {
            System.out.println(e);
            rs.setData(null);
            rs.setMessage("Error from system");
            rs.setSuccess(false);
            return rs;
        }
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataBankStatement(UploadBankStatementModel uploadBankStatementModel) {
        ResponseObject rs = new ResponseObject();
        int countAllData = uploadBankStatementMapper.countAllData(uploadBankStatementModel);
        List<UploadBankStatementModel> result = uploadBankStatementMapper.getDataBankStatement(uploadBankStatementModel);
        if(result.size() != 0){
            Map rsMap = new HashMap();
            rsMap.put("data",result);
            rsMap.put("totalPages",countAllData);
            rs.setData(rsMap);
            rs.setSuccess(true);
        }else {
            rs.setData(null);
            rs.setSuccess(false);
            rs.setMessage("Have no data!");
        }
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportBankStatementData(UploadBankStatementModel uploadBankStatementModel) {
        try {
            List<UploadBankStatementModel> result =  uploadBankStatementMapper.getDataBankStatementForExport(uploadBankStatementModel);

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"AppID", "Date", "Detail", "Txn No", "Desc Acc",
                    "Debit amount"});

            for (int i = 0; i < result.size(); i++) {
                data.add(new String[]{result.get(i).getAppId(), result.get(i).getStatementDateExcel(),
                        result.get(i).getDetail(), result.get(i).getTxnNo(),  "=\"" +result.get(i).getDescAcc()+ "\"",
                        result.get(i).getDebitAmt()});
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
