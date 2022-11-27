package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.AccountingMapper;
import com.dou.accounting.mappers.CloseSoldoutMapper;
import com.dou.accounting.mappers.orahomeMapper;
import com.dou.accounting.models.AccountingModel;
import com.dou.accounting.models.CloseSoldoutModel;
import com.dou.accounting.models.WaveOffAmountModel;
import com.dou.accounting.services.AccountingInterface;
import com.dou.accounting.services.CloseSoldoutInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.controllers.LoginController;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class CloseSoldoutService implements CloseSoldoutInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseSoldoutService.class);

    @Autowired
    CloseSoldoutMapper closeSoldoutMapper;

    @Autowired
    orahomeMapper orahomeMapper;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public Map loadingDetailsData(MultipartFile excelFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            Map rsData = new HashMap();
            List<CloseSoldoutModel> dataClose = new ArrayList<>();
            int rownNum = worksheet.getPhysicalNumberOfRows();
            if (rownNum > 501) {
                rsData.put("checkRcr", "Over 500 records in file, Please check!");
                return rsData;
            } else {
                for (int i = 1; i < rownNum; i++) {
                    CloseSoldoutModel model = new CloseSoldoutModel();
                    XSSFRow row = worksheet.getRow(i);
                    try {
                        model.setAgreementId(formatter.formatCellValue(row.getCell(0)).toUpperCase());
                        dataClose.add(model);
                    } catch (Exception e) {

                    }
                }
                Map dataCheck = CheckLsApp(dataClose);
                boolean isFalse = (boolean) dataCheck.get("isFalse");
                List<CloseSoldoutModel> dataForInsert = (List<CloseSoldoutModel>) dataCheck.get("data");
                List<CloseSoldoutModel> dataCloseRs = closeSoldoutMapper.loadingDetailsData(dataClose);
                if (isFalse) {
                    rsData.put("ErrorData", dataForInsert);

                } else {
                    rsData.put("ErrorData", null);
                }
                rsData.put("LoadingData", dataCloseRs);
                return rsData;
            }
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }


    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject closeLsApp(List<CloseSoldoutModel> closeSoldoutModels) {
        ResponseObject rs = new ResponseObject();
        try{
            Map dataCheck = CheckLsApp(closeSoldoutModels);
            boolean isFalse = (boolean) dataCheck.get("isFalse");
            List<CloseSoldoutModel> dataForInsert = (List<CloseSoldoutModel>) dataCheck.get("data");
            if(isFalse){
                rs.setData(dataForInsert);
                rs.setSuccess(false);
                rs.setMessage("Data is not available");
            }else{
                for (CloseSoldoutModel model: dataForInsert){
                    closeSoldoutMapper.updateAppId(model);//update in F1
                }
                for (CloseSoldoutModel model: dataForInsert){
                    orahomeMapper.insertInCloseSoldoutHis(model); //insert in orahome
                }
                rs.setData(dataForInsert);
                rs.setSuccess(true);
                rs.setMessage("Data is  available");
            }
            return rs;
        }catch (Exception e){
            rs.setData(null);
            rs.setSuccess(false);
            rs.setMessage("Data is not available");
            System.out.println(e);
            return rs;
        }
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public List<CloseSoldoutModel> getCloseSoldout(List<CloseSoldoutModel> closeSoldoutModels) {
        return closeSoldoutMapper.loadingDetailsData(closeSoldoutModels);
    }

    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public Map CheckLsApp(List<CloseSoldoutModel> lsApp){
        Map rs = new HashMap();
        List <CloseSoldoutModel> dataForInsert = new ArrayList<>();
        Boolean isFalse = false;
        for(CloseSoldoutModel modelData:lsApp){
            CloseSoldoutModel model = new CloseSoldoutModel();
            int isSoldout = closeSoldoutMapper.isSoldout(modelData.getAgreementId());
            int isExistApp = closeSoldoutMapper.isExistApp(modelData.getAgreementId());
            model.setAgreementId(modelData.getAgreementId());
            model.setUserLogin(modelData.getUserLogin());
            String messageError = "";
            if(isExistApp == 0){
                isFalse = true;
                messageError += "AGREEMENT ID IS NOT EXIST. ";
            }
            if(isSoldout == 0){
                isFalse = true;
                messageError += "NPA STAGE IS NOT SOLDOUT OR STATUS IS NOT A.";
            }
            model.setError(messageError);
            dataForInsert.add(model);
        }
        rs.put("data",dataForInsert);
        rs.put("isFalse", isFalse);
        return rs;
    }
}
