package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.MonthlyDisbursalReportMapper;
import com.dou.accounting.models.MonthlyDisbursalReportModel;
import com.dou.accounting.services.MonthlyDisbursalReportInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MonthlyDisbursalReportService implements MonthlyDisbursalReportInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyDisbursalReportService.class);

    @Autowired
    MonthlyDisbursalReportMapper monthlyDisbursalReportMapper;

    @Autowired
    ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataMonthlyDisbursalReport(MonthlyDisbursalReportModel monthlyDisbursalReportModel) {
        ResponseObject rs = new ResponseObject();
        Map<String, Object> param = new HashMap<>();
        param.put("date", monthlyDisbursalReportModel.getDisbursalDate());
        param.put("out", new String());
        param.put("out_cur", new MonthlyDisbursalReportModel());

        Map<String, Object> paramChart = new HashMap<>();
        paramChart.put("date", monthlyDisbursalReportModel.getDisbursalDate());
        paramChart.put("out", new String());
        paramChart.put("out_cur", new MonthlyDisbursalReportModel());
        try {
            monthlyDisbursalReportMapper.getDataMonthlyDisbursalReport(param);
            monthlyDisbursalReportMapper.getDataMonthlyDisbursalReportChart(paramChart);
        }catch (Exception e){
            System.out.println(e);
            rs.setMessage("Error when call proc!");
            rs.setSuccess(false);
            return rs;
        }
        List<MonthlyDisbursalReportModel> resultT = (List<MonthlyDisbursalReportModel>) param.get("out_cur");
        List<MonthlyDisbursalReportModel> resultChart = (List<MonthlyDisbursalReportModel>) paramChart.get("out_cur");
        List<MonthlyDisbursalReportModel> lsSearch ;
        List<MonthlyDisbursalReportModel> lsSearchChart ;
        List<MonthlyDisbursalReportModel> lsSearchChartCurYear = new ArrayList<>() ;
        List<MonthlyDisbursalReportModel> lsSearchChartPreYear  = new ArrayList<>() ;
        Map result = new HashMap();
        lsSearchChart = searchData(resultChart, monthlyDisbursalReportModel.getProductType());
        String[] curYear = monthlyDisbursalReportModel.getDisbursalDate().split("/");
        for(int i = 0; i<lsSearchChart.size(); i++ ){
            if(lsSearchChart.get(i).getYearRpt().equals(curYear[2])){
                lsSearchChartCurYear.add(lsSearchChart.get(i));
            }else{
                lsSearchChartPreYear.add(lsSearchChart.get(i));
            }
        }
        List<Integer> lsMonth = new ArrayList<>();
        lsMonth.add(1);
        lsMonth.add(2);
        lsMonth.add(3);
        lsMonth.add(4);
        lsMonth.add(5);
        lsMonth.add(6);
        lsMonth.add(7);
        lsMonth.add(8);
        lsMonth.add(9);
        lsMonth.add(10);
        lsMonth.add(11);
        lsMonth.add(12);


        for (int j = 0; j < lsSearchChartCurYear.size(); j++) {
            for(int i=0; i< lsMonth.size(); i++ ) {
                if (lsMonth.get(i) == lsSearchChartCurYear.get(j).getMonthRpt()) {
                    lsMonth.remove(i);
                    break;
                }
            }
        }
        List<Integer> lsMonthPre = new ArrayList<>();
        lsMonthPre.add(1);
        lsMonthPre.add(2);
        lsMonthPre.add(3);
        lsMonthPre.add(4);
        lsMonthPre.add(5);
        lsMonthPre.add(6);
        lsMonthPre.add(7);
        lsMonthPre.add(8);
        lsMonthPre.add(9);
        lsMonthPre.add(10);
        lsMonthPre.add(11);
        lsMonthPre.add(12);
        for (int j = 0; j < lsSearchChartPreYear.size(); j++) {
            for(int i=0; i< lsMonthPre.size(); i++ ) {
                if (lsMonthPre.get(i) == lsSearchChartPreYear.get(j).getMonthRpt()) {
                    lsMonthPre.remove(i);
                    break;
                }
            }
        }

        for (Integer dataMonth : lsMonth) {
            MonthlyDisbursalReportModel model = new MonthlyDisbursalReportModel();
            model.setMonthRpt(dataMonth);
            model.setCountApp(0);
            model.setDisAmout("0");
            lsSearchChartCurYear.add(model);
        }
        for (Integer dataMonth : lsMonthPre) {
            MonthlyDisbursalReportModel model = new MonthlyDisbursalReportModel();
            model.setMonthRpt(dataMonth);
            model.setCountApp(0);
            model.setDisAmout("0");
            lsSearchChartPreYear.add(model);
        }

        lsSearchChartCurYear.sort(new Comparator<MonthlyDisbursalReportModel>() {
            @Override
            public int compare(MonthlyDisbursalReportModel o1, MonthlyDisbursalReportModel o2) {
                return o1.getMonthRpt() - o2.getMonthRpt();
            }
        });
        lsSearchChartPreYear.sort(new Comparator<MonthlyDisbursalReportModel>() {
            @Override
            public int compare(MonthlyDisbursalReportModel o1, MonthlyDisbursalReportModel o2) {
                return o1.getMonthRpt() - o2.getMonthRpt();
            }
        });
        result.put("resultChartCurYear",lsSearchChartCurYear);
        result.put("resultPreYear",lsSearchChartPreYear);

        lsSearch = searchData(resultT, monthlyDisbursalReportModel.getProductType());
        result.put("resultT",lsSearch);
        rs.setData(result);

        if(resultT.size() != 0){
            rs.setSuccess(true);
        }else {
            rs.setData(null);
            rs.setSuccess(false);
            rs.setMessage("Have no data!");
        }
        return rs;
    }

    String  source = "";
    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportExcel(MonthlyDisbursalReportModel monthlyDisbursalReportModel) {
        try{
            Map<String, Object> param = new HashMap<>();
            param.put("date",monthlyDisbursalReportModel.getDisbursalDate());
            param.put("out", new String());
            param.put("out_cur", new MonthlyDisbursalReportModel());
            monthlyDisbursalReportMapper.getDataMonthlyDisbursalReport(param);

            List<MonthlyDisbursalReportModel> result = (List<MonthlyDisbursalReportModel>) param.get("out_cur");
            List<MonthlyDisbursalReportModel> lsSearch ;

            Path storage = configurations.getReportStorage(getCurrentRequest());
            String sourceTomcat = storage.resolve("Template").toString();
            String excelFilePath = sourceTomcat +"-export.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Reviews");

            //Save Image To TomcatSer
            source = storage.resolve("Template").toString();
            byte[] decodedImg = Base64.getDecoder()
                    .decode(monthlyDisbursalReportModel.getBase64Img().split(",")[1]);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedImg));
            // write the image to a file
            File outputfile = new File(sourceTomcat + "myImage.png");
            ImageIO.write(image, "png", outputfile);
            byte[] decodedImg1 = Base64.getDecoder()
                    .decode(monthlyDisbursalReportModel.getBase64Img1().split(",")[1]);
            BufferedImage image1 = ImageIO.read(new ByteArrayInputStream(decodedImg1));
            File outputfile1 = new File(sourceTomcat + "myImage1.png");
            ImageIO.write(image1, "png", outputfile1);
            // End Save Image To TomcatSer

            writeHeaderLine(workbook,sheet, monthlyDisbursalReportModel.getProductType(),
                    monthlyDisbursalReportModel.getDisbursalDate());
            lsSearch = searchData(result, monthlyDisbursalReportModel.getProductType());
            writeDataLines(lsSearch, workbook, sheet);
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            File file = new File(excelFilePath);
            byte[] encoded = FileUtils.readFileToByteArray(file);
            return encoded;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public List<MonthlyDisbursalReportModel> searchData(List<MonthlyDisbursalReportModel> data, String productType){
        List<MonthlyDisbursalReportModel> result = new ArrayList<>();
        for(MonthlyDisbursalReportModel model: data){
            if(model.getProduct().equals(productType)){
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
        headerCell.setCellValue("MONTHLY DISBURSAL REPORT");

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
        headerCell4.setCellValue("Date");
        headerCell4 = headerRow4.createCell(1);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("NO. OF APP");
        headerCell4 = headerRow4.createCell(2);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Disbursed Amount included Insurance");
        headerCell4 = headerRow4.createCell(3);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Disbursed Amount excluded Insurance");
        headerCell4 = headerRow4.createCell(4);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Insurance amount");
        headerCell4 = headerRow4.createCell(5);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("No. of App Actual");
        headerCell4 = headerRow4.createCell(6);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Actual cash disbursed (only)");
        headerCell4 = headerRow4.createCell(7);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Actual cash disbursed included insurance");
        headerCell4 = headerRow4.createCell(8);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Pending Disbursed Amount");
        headerCell4 = headerRow4.createCell(9);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Accumulated Disbursed Amount");
    }

    private void writeDataLines(List<MonthlyDisbursalReportModel> result, XSSFWorkbook workbook,
                                XSSFSheet sheet) throws SQLException, IOException {
        int rowCount = 6;
        Drawing drawing = sheet.createDrawingPatriarch();
        MonthlyDisbursalReportModel modelTotal = new MonthlyDisbursalReportModel();
        int totalApp = 0;
        long totalincludedInsAmt = 0;
        long  totalnotInsAmt = 0;
        long totalinsAmt = 0;
        long totalnoAppActual = 0;
        long totalactualCashDis = 0;
        long totalactualCashDisInIns = 0;
        long totalpendingDisAmount = 0;
        long totalAccumDisAmountInIns = 0;
        for(MonthlyDisbursalReportModel model : result){
            totalApp += model.getCountApp();
            totalincludedInsAmt += Long.parseLong(model.getIncludedInsAmt());
            totalnotInsAmt += Long.parseLong(model.getNotInsAmt());
            totalinsAmt += Long.parseLong(model.getInsAmt());
            totalnoAppActual += Long.parseLong(model.getNoAppActual());
            totalactualCashDis += Long.parseLong(model.getActualCashDis());
            totalactualCashDisInIns += Long.parseLong(model.getActualCashDisInIns());
            totalpendingDisAmount += Long.parseLong(model.getPendingDisAmount());
            totalAccumDisAmountInIns += Long.parseLong(model.getAccumDisAmountInIns());
        }
        modelTotal.setCountApp(totalApp);
        modelTotal.setIncludedInsAmt(String.valueOf(totalincludedInsAmt));
        modelTotal.setNotInsAmt(String.valueOf(totalnotInsAmt));
        modelTotal.setInsAmt(String.valueOf(totalinsAmt));
        modelTotal.setNoAppActual(String.valueOf(totalnoAppActual));
        modelTotal.setActualCashDis(String.valueOf(totalactualCashDis));
        modelTotal.setActualCashDisInIns(String.valueOf(totalactualCashDisInIns));
        modelTotal.setPendingDisAmount(String.valueOf(totalpendingDisAmount));
        modelTotal.setAccumDisAmountInIns(String.valueOf(totalAccumDisAmountInIns));
        modelTotal.setLogDt(null);
        result.add(modelTotal);


        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("MM/dd/yyyy"));

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        for(MonthlyDisbursalReportModel model : result){

            Row row = sheet.createRow(rowCount++);

            if(model.getLogDt() == null){
                int columnCount = 0;
                Cell cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue("TOTAL");

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getCountApp());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getIncludedInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getNotInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getNoAppActual());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getActualCashDis());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getActualCashDisInIns());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getPendingDisAmount());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(0);
            }else{
                int columnCount = 0;
                Cell cell = row.createCell(columnCount++);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(model.getLogDt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getCountApp());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getIncludedInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getNotInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getNoAppActual());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getActualCashDis());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getActualCashDisInIns());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getPendingDisAmount());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getAccumDisAmountInIns());
            }
        }
        Row row1 = sheet.createRow(rowCount+4);
        row1.setHeight((short) 1000);
        sheet.autoSizeColumn(1);
        sheet.setColumnWidth(1,10000);
        insertImageToCell(workbook, "xlsx", rowCount, drawing, "myImage.png",1,2);

        row1.setHeight((short) 1000);
        sheet.autoSizeColumn(3);
        sheet.setColumnWidth(3,10000);
        insertImageToCell(workbook, "xlsx", rowCount , drawing, "myImage1.png",3,4);
    }
    private void insertImageToCell(Workbook workbook, String fileType, int rowNum, Drawing drawing,
                                   String imageName, int column1, int column2) throws IOException {

        //Loading image from application resource
        InputStream is = new FileInputStream(source + imageName);

        //Converting input stream into byte array
        byte[] inputImageBytes = IOUtils.toByteArray(is);
        int inputImagePictureID = workbook.addPicture(inputImageBytes, Workbook.PICTURE_TYPE_PNG);
        is.close();
        ClientAnchor anchor = new XSSFClientAnchor();;
        //Creating the client anchor based on file format

        anchor.setCol1(column1);
        anchor.setCol2(column2);
        anchor.setRow1(rowNum +2);
        anchor.setRow2(rowNum + 15);
        drawing.createPicture(anchor, inputImagePictureID);
    }
}
