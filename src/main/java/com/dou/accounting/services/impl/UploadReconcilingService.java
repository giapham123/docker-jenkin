package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.UploadReconcilingMapper;
import com.dou.accounting.models.UploadReconcilingModel;
import com.dou.accounting.services.UploadReconcilingInterface;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UploadReconcilingService implements UploadReconcilingInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadReconcilingService.class);

    @Autowired
    UploadReconcilingMapper uploadReconcilingMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject uploadFile(MultipartFile excelFile, String userLogin) {
        ResponseObject rs = new ResponseObject();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<UploadReconcilingModel> dataUploadReconciling = new ArrayList<>();
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                UploadReconcilingModel model = new UploadReconcilingModel();
                XSSFRow row = worksheet.getRow(i);
                try {
                    model.setTransType(formatter.formatCellValue(row.getCell(0)).toUpperCase().trim());
                    model.setChannelCd(formatter.formatCellValue(row.getCell(1)).toUpperCase().trim());
                    model.setAgreementId(formatter.formatCellValue(row.getCell(2)).toUpperCase().trim());
                    model.setAmount(formatter.formatCellValue(row.getCell(3)).toUpperCase().trim());
                    model.setTransDt(formatter.formatCellValue(row.getCell(4)).toUpperCase().trim());
                    model.setUserLogin(userLogin);
                    dataUploadReconciling.add(model);
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
            for(int i = 0; i< dataUploadReconciling.size(); i++) {
                String reason = "";

                String str = dataUploadReconciling.get(i).getAgreementId().replaceAll("[^\\w\\s]", "");
                dataUploadReconciling.get(i).setAgreementId(str);
                if(dataUploadReconciling.get(i).getTransType() == null || dataUploadReconciling.get(i).getTransType() == ""){
                    reason += "Trans Type is empty, ";
                }
                if(dataUploadReconciling.get(i).getChannelCd() == null || dataUploadReconciling.get(i).getChannelCd() == ""){
                    reason += "Channel is empty, ";
                }
                if(dataUploadReconciling.get(i).getAmount() == null || dataUploadReconciling.get(i).getAmount() == ""){
                    reason += "Amount is empty, ";
                }else if(dataUploadReconciling.get(i).getAmount().equals("0")){
                    reason += "Amount equal 0, ";
                }
                if(dataUploadReconciling.get(i).getTransDt() == null || dataUploadReconciling.get(i).getTransDt() == ""){
                    reason += "Trans Date is empty, ";
                }else{
                    String[] splitDate = dataUploadReconciling.get(i).getTransDt().split("/")[2].split("");
                    if(splitDate.length < 4){
                        reason += "Trans Date is wrong format (MM/dd/yyyy), ";
                    }else{
                        Boolean isValid = isValid(dataUploadReconciling.get(i).getTransDt());
                        if(!isValid){
                            reason += "Trans Date is wrong format (MM/dd/yyyy), ";
                        }else{
                            Date transDt=new SimpleDateFormat("MM/dd/yyyy").parse(dataUploadReconciling.get(i).getTransDt());
                            dataUploadReconciling.get(i).setTransDate(transDt);
                        }
                    }
                }

                StringBuffer sb= new StringBuffer(reason.trim());
                if(reason != ""){
                    isSuccessFile = false;
                    sb.deleteCharAt(sb.length()-1);
                }
                dataUploadReconciling.get(i).setReason(String.valueOf(sb));
            }
            if(isSuccessFile){
                int maxIdInSap = uploadReconcilingMapper.getMaxIdInSap();
                int maxIdInTMP = uploadReconcilingMapper.getMaxIdInTMP();
                int batchId = 0;
                if(maxIdInSap == maxIdInTMP){
                    batchId = maxIdInSap + 1;
                }else if(maxIdInSap > maxIdInTMP){
                    batchId = maxIdInSap + 1;
                }else if(maxIdInSap < maxIdInTMP){
                    batchId = maxIdInTMP + 1;
                }

                for(UploadReconcilingModel model: dataUploadReconciling){
                    model.setBatchId(batchId);
                    uploadReconcilingMapper.insertDataToSapTxn(model);
                }
                Map<String, Object> param = new HashMap<>();
                param.put("batchId", batchId);
                param.put("out", new String());
                try {
                    uploadReconcilingMapper.importDataToTxnImport(param);
                }catch (Exception e){
                    throw new RuntimeException("Error occurred during executive proc",e);
                }
            }else{
                rs.setData(dataUploadReconciling);
                rs.setSuccess(false);
                rs.setMessage("Import Failed, Please Check Excel File!");
            }
            return rs;
        }
        catch (Exception e){
            LOGGER.error(e.toString());
            throw new RuntimeException("Error occurred during data insertion", e);
        }
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getInitData() {
        ResponseObject rs = new ResponseObject();
        Map dataLoadingInit = new HashMap();
        dataLoadingInit.put("channel", uploadReconcilingMapper.getChannel());
        dataLoadingInit.put("trans", uploadReconcilingMapper.getTransType());
        rs.setData(dataLoadingInit);
        rs.setSuccess(true);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataUploadReconciling(UploadReconcilingModel model) {
        ResponseObject rs = new ResponseObject();
        Map dataLoadingInit = new HashMap();
        dataLoadingInit.put("uploadReconcilingImport", uploadReconcilingMapper.getDataUploadReconciling(model));
        dataLoadingInit.put("uploadReconcilingImportTmp", uploadReconcilingMapper.getDataUploadReconcilingTmp(model));
        rs.setData(dataLoadingInit);
        rs.setSuccess(true);
        rs.setMessage("Success");
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject deteleDataImportSap(List<UploadReconcilingModel> model) {
        for(int i = 0; i< model.size(); i++){
            uploadReconcilingMapper.deleteInSapGL(model.get(i).getCaseId(), model.get(i).getLeaVoucherId());
            uploadReconcilingMapper.deleteInSapImport(model.get(i).getAgreementId(), model.get(i).getLeaVoucherId());
        }
        ResponseObject rs = new ResponseObject();
        rs.setSuccess(true);
        rs.setMessage("Success");
        return rs;
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    public void exception() throws Exception {
        throw new Exception("Error occurred during data insertion");
    }
}
