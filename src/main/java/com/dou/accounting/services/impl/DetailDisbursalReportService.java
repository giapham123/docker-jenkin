package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.DetailDisbursalReportMapper;
import com.dou.accounting.models.DetailDisbursalReportModel;
import com.dou.accounting.services.DetailDisbursalReportInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DetailDisbursalReportService implements DetailDisbursalReportInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(DetailDisbursalReportService.class);

    @Autowired
    DetailDisbursalReportMapper detailDisbursalReportMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataDetailDisbursalReport(DetailDisbursalReportModel detailDisbursalReportModel) {
        ResponseObject rs = new ResponseObject();
        Map<String, Object> param = new HashMap<>();
        param.put("frDate", detailDisbursalReportModel.getFromDt());
        param.put("toDate", detailDisbursalReportModel.getToDt());
        param.put("out", new String());
        param.put("out_cur", new DetailDisbursalReportModel());
        detailDisbursalReportMapper.getDataDetailDisbursalReport(param);
        List<DetailDisbursalReportModel> result = (List<DetailDisbursalReportModel>) param.get("out_cur");
        List<DetailDisbursalReportModel> lsSearch ;
        if(detailDisbursalReportModel.getTypeProduct().equals("All Product")){
            rs.setData(result);
            rs.setSuccess(true);
        }else{
            lsSearch = searchData(result, detailDisbursalReportModel.getTypeProduct());
            rs.setData(lsSearch);
            rs.setSuccess(true);
        }
        return rs;
    }


    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportExcel(DetailDisbursalReportModel detailDisbursalReportModel) {
        try{
//            List<DetailDisbursalReportModel> result= detailDisbursalReportMapper.getDataDetailDisbursalReport(detailDisbursalReportModel);
            Map<String, Object> param = new HashMap<>();
            param.put("frDate", detailDisbursalReportModel.getFromDt());
            param.put("toDate", detailDisbursalReportModel.getToDt());
            param.put("out", new String());
            param.put("out_cur", new DetailDisbursalReportModel());
            detailDisbursalReportMapper.getDataDetailDisbursalReport(param);
            List<DetailDisbursalReportModel> result = (List<DetailDisbursalReportModel>) param.get("out_cur");
            List<DetailDisbursalReportModel> lsSearch ;
            Path storage = configurations.getReportStorage(getCurrentRequest());
            String sourceTomcat = storage.resolve("Template").toString();
            String excelFilePath = sourceTomcat +"-export.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Reviews");
            writeHeaderLine(workbook,sheet, detailDisbursalReportModel.getTypeProduct(),
                    detailDisbursalReportModel.getFromDt() + " To "+ detailDisbursalReportModel.getToDt());
            if(detailDisbursalReportModel.getTypeProduct().equals("All Product")){
                writeDataLines(result, workbook, sheet);
            }else{
                lsSearch = searchData(result, detailDisbursalReportModel.getTypeProduct());
                writeDataLines(lsSearch, workbook, sheet);
            }
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            File file = new File(excelFilePath);
            byte[] encoded =FileUtils.readFileToByteArray(file);
            return encoded;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public List<DetailDisbursalReportModel> searchData(List<DetailDisbursalReportModel> data, String productType){
        List<DetailDisbursalReportModel> result = new ArrayList<>();
        for(DetailDisbursalReportModel model: data){
            if(model.getTypeProduct().equals(productType)){
                result.add(model);
            }
        }
        return result;
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

    private void writeHeaderLine(XSSFWorkbook workbook,XSSFSheet sheet, String productType, String disbursalDate) {
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy: HH:mm:ss").format(new Date());
        Row headerRow = sheet.createRow(0);
        Row headerRow1 = sheet.createRow(1);
        Row headerRow2 = sheet.createRow(2);
        Row headerRow3 = sheet.createRow(3);
        Row headerRow4 = sheet.createRow(5);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("Report:");
        headerCell = headerRow.createCell(1);
        headerCell.setCellStyle(style);
        headerCell.setCellValue("DETAIL DISBURSAL REPORT");

        Cell headerCell1 = headerRow1.createCell(0);
        headerCell1.setCellStyle(style);
        headerCell1.setCellValue("Generated on (Date & Time):");
        headerCell1 = headerRow1.createCell(1);
        headerCell1.setCellValue(timeStamp);

        Cell headerCell2 = headerRow2.createCell(0);
        headerCell2.setCellStyle(style);
        headerCell2.setCellValue("Product:");
        headerCell2 = headerRow2.createCell(1);
        headerCell2.setCellValue(productType);

        Cell headerCell3 = headerRow3.createCell(0);
        headerCell3.setCellStyle(style);
        headerCell3.setCellValue("Disbursal date:");
        headerCell3 = headerRow3.createCell(1);
        headerCell3.setCellValue(disbursalDate);

        Cell headerCell4 = headerRow4.createCell(0);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Date Dis (F1)");
        headerCell4 = headerRow4.createCell(1);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Product");
        headerCell4 = headerRow4.createCell(2);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("AppID");
        headerCell4 = headerRow4.createCell(3);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Customer Name");
        headerCell4 = headerRow4.createCell(4);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Beneficiary Name");
        headerCell4 = headerRow4.createCell(5);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Agreement No");
        headerCell4 = headerRow4.createCell(6);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Bank Name");
        headerCell4 = headerRow4.createCell(7);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Bank Code");
        headerCell4 = headerRow4.createCell(8);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Bank Branch Name");
        headerCell4 = headerRow4.createCell(9);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Bank Account Number");
        headerCell4 = headerRow4.createCell(10);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Disbursal Amount");
        headerCell4 = headerRow4.createCell(11);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Partner Bank");
        headerCell4 = headerRow4.createCell(12);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Scheme Name");
        headerCell4 = headerRow4.createCell(13);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Disbursal Amount Ins");
        headerCell4 = headerRow4.createCell(14);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Txn No");
        headerCell4 = headerRow4.createCell(15);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Actual Date");
    }

    private void writeDataLines(List<DetailDisbursalReportModel> result, XSSFWorkbook workbook,
                                XSSFSheet sheet) throws SQLException {
        int rowCount = 6;
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("MM/dd/yyyy"));
        for(DetailDisbursalReportModel model : result){

            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);

            cell.setCellStyle(cellStyle);
            cell.setCellValue(model.getDisDt());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getTypeProduct());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getAppId());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getCustomerNm());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getBenNm());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getAgreeNo());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getBankNm());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getBankCd());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getBankBranch());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getBankAccNum());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getDisAmt());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getPartBank());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getSchemeNm());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getDisAmtIns());

            cell = row.createCell(columnCount++);
            cell.setCellValue(model.getTxnNo());

            cell = row.createCell(columnCount++);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(model.getStatementDt());
        }
    }
}
