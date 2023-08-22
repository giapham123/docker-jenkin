package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.WaveOffAmountMapper;
import com.dou.accounting.models.WaveOffAmountModel;
import com.dou.accounting.services.WaveOffAmountInterface;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class WaveOffAmountService implements WaveOffAmountInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaveOffAmountService.class);

    @Autowired
    private WaveOffAmountMapper _waveOffAmountMapper;

    LoadingUploadSingle statusFinishUpload = LoadingUploadSingle.getInstance() ;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public List<WaveOffAmountModel> getWaveOffAmountData(WaveOffAmountModel WaveOffAmountModel){
        List<WaveOffAmountModel> rs = _waveOffAmountMapper.getWaveOffAmountData(WaveOffAmountModel);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject uploadFileWaveOffAmount(MultipartFile excelFile) {
        ResponseObject rs = new ResponseObject();
        try {
            statusFinishUpload.setUploadFinish(false);
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<WaveOffAmountModel> dataWaveOff = new ArrayList<>();
            _waveOffAmountMapper.deleteAllData();
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                WaveOffAmountModel model = new WaveOffAmountModel();
                XSSFRow row = worksheet.getRow(i);
                try {
                    model.setAgreementId(formatter.formatCellValue(row.getCell(0)).toUpperCase());
                    model.setWaveOffAmount(formatter.formatCellValue(row.getCell(1)).toUpperCase());
                    dataWaveOff.add(model);
                    _waveOffAmountMapper.insertDataToWaveOff(model);
                } catch (Exception e) {
                    System.out.println(e);
                    LOGGER.error("Error when upload file waive off ",e);
                    statusFinishUpload.setUploadFinish(true);
                    rs.setData(null);
                    rs.setSuccess(false);
                    rs.setMessage("Wrong format excel, Please check!");
                    return rs;
                }
            }
            statusFinishUpload.setUploadFinish(true);
            rs.setData(null);
            rs.setSuccess(true);
            rs.setMessage("Upload Success");
            return rs;
        }
        catch (Exception e){
            LOGGER.error("Error when upload file waive off ",e);
            System.out.println(e);
            rs.setData(null);
            rs.setSuccess(false);
            rs.setMessage("Failed!");
            return rs;
        }
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public int updateWaveOffAmount(WaveOffAmountModel waveOffAmountModel) {
        try {
            int rs = _waveOffAmountMapper.updateWaveOffAmount(waveOffAmountModel);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public boolean uploadStatus() {
        return  statusFinishUpload.getUploadFinish();
    }
}
