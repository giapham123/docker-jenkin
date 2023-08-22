package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.PendingDisbursementMapper;
import com.dou.accounting.models.PendingDisbursementModel;
import com.dou.accounting.models.SigProcess;
import com.dou.accounting.services.PendingDisburementInterface;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PendingDisbursementService implements PendingDisburementInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(PendingDisbursementService.class);

    @Autowired
    PendingDisbursementMapper disbursementMapper;

    SigProcess isProcess= SigProcess.getInstance();

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject uploadFile(MultipartFile excelFile, String userLogin) {
        ResponseObject rs = new ResponseObject();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<PendingDisbursementModel> pendingDisModel = new ArrayList<>();
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                PendingDisbursementModel model = new PendingDisbursementModel();
                XSSFRow row = worksheet.getRow(i);
                try {
                    model.setAgreementId(formatter.formatCellValue(row.getCell(0)).toUpperCase().trim());
                    model.setUserLogin(userLogin);
                    pendingDisModel.add(model);
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
            for(int i = 0; i< pendingDisModel.size(); i++) {
                String reason = "";
                int isExistAgreementId = disbursementMapper.isExistAgreementId(pendingDisModel.get(i).getAgreementId());
                if(isExistAgreementId != 0){
                    reason += "AgreementId is imported, ";
                }
                for (int j = 0; j < pendingDisModel.size(); j++) {
                    if (i != j) {
                        if (pendingDisModel.get(i).getAgreementId() != null && pendingDisModel.get(i).getAgreementId() != "") {
                            if (pendingDisModel.get(i).getAgreementId().equals(pendingDisModel.get(j).getAgreementId())) {
                                reason += "Duplicate AgreementId in files, ";
                            }
                        }
                    }
                }
                StringBuffer sb= new StringBuffer(reason.trim());
                if(reason != ""){
                    isSuccessFile = false;
                    sb.deleteCharAt(sb.length()-1);
                }
                pendingDisModel.get(i).setErrorMsg(String.valueOf(sb));
            }
            if(isSuccessFile){
                for (PendingDisbursementModel modelData: pendingDisModel){
                    disbursementMapper.insertDataPendingDisbur(modelData.getAgreementId(), modelData.getUserLogin());
                }
            }else{
                rs.setData(pendingDisModel);
                rs.setSuccess(false);
                rs.setMessage("Import Failed, Please Check Excel File!");
            }
            return rs;
        }
        catch (Exception e){
            throw new RuntimeException("Error occurred during data insertion", e);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject insertSigAgre(String agreementId, String userLogin) {
        ResponseObject rs = new ResponseObject();
        int isExistAgreementId = disbursementMapper.isExistAgreementId(agreementId);
        if(isExistAgreementId != 0){
            rs.setMessage( "AgreementId is imported");
            rs.setSuccess(false);
            rs.setData(null);
        }else{disbursementMapper.insertDataPendingDisbur(agreementId, userLogin);
            rs.setMessage( "Insert Success");
            rs.setSuccess(true);
            rs.setData(null);
        }

        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject deleteAgreementId(List<PendingDisbursementModel> model) {
        ResponseObject rs = new ResponseObject();
        List<PendingDisbursementModel> dataY = new ArrayList<>();
        List<PendingDisbursementModel> dataN = new ArrayList<>();
        for(PendingDisbursementModel selectModel : model){
            if(selectModel.getRunYn() !=null && selectModel.getRunYn().equals("Y")){
                dataY.add(selectModel);
            }else {
                dataN.add(selectModel);
            }
        }
        for(PendingDisbursementModel selectModel : model){
            disbursementMapper.deleteAgreementId(selectModel.getAgreementId());
        }
        if(dataY.size() != 0){
            rs.setData(dataN);
            rs.setSuccess(false);
            rs.setMessage("Delete Success with process N, Please Check Excel File");
        }else{
            rs.setData(dataN);
            rs.setSuccess(true);
            rs.setMessage("Delete Success");
        }

        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataPendingDisbursement(String agreementId) {
        ResponseObject rs = new ResponseObject();
        List<PendingDisbursementModel> rsData = disbursementMapper.getDataPendingDisbursement(agreementId);
        if(rsData.size() != 0){
            rs.setData(rsData);
            rs.setSuccess(true);
            rs.setMessage("Get Data Pending Disbursement Success");
        }else{
            rs.setData(rsData);
            rs.setSuccess(false);
            rs.setMessage("Have no data");
        }
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject processPending(String usrLg) throws ParseException {
        isProcess.setProcessPD(true);
        ResponseObject rs = new ResponseObject();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String sDate1= formatter.format(date);
        Date date1=new SimpleDateFormat("MM/dd/yyyy").parse(sDate1);
        Map<String, Object> param = new HashMap<>();
        param.put("curDt",date1);
        param.put("type","PD");
        param.put("usrLg",usrLg);
        param.put("out", new String());
        disbursementMapper.processPending(param);
        String isSuccess = (String) param.get("out");
        if(isSuccess.equals("Success")){
            rs.setSuccess(true);
            rs.setMessage("Process Completed");
        }else{
            rs.setSuccess(false);
            rs.setMessage(isSuccess);
        }
        isProcess.setProcessPD(false);
        return rs;
    }

    @Override
    public ResponseObject getProcess() {
        ResponseObject rs = new ResponseObject();
        rs.setData(isProcess.getProcessCollCompare());
        return rs;
    }
}
