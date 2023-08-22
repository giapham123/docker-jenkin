package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.DailyDisbursalReportMapper;
import com.dou.accounting.models.DailyDisbursalReportModel;
import com.dou.accounting.services.DailyDisbursalReportInterface;
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
public class DailyDisbursalReportService implements DailyDisbursalReportInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyDisbursalReportService.class);

    @Autowired
    DailyDisbursalReportMapper dailyDisbursalReportMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public ResponseObject getDataDetailDisbursalReport(DailyDisbursalReportModel dailyDisbursalReportModel)  {

        ResponseObject rs = new ResponseObject();
        Map<String, Object> param = new HashMap<>();
        param.put("date", dailyDisbursalReportModel.getDisbursalDate());
        param.put("out", new String());
        param.put("out_cur", new DailyDisbursalReportModel());

        Map<String, Object> paramT1 = new HashMap<>();
        paramT1.put("date", dailyDisbursalReportModel.getDateT1());
        paramT1.put("out", new String());
        paramT1.put("out_cur", new DailyDisbursalReportModel());
        try {
            dailyDisbursalReportMapper.getDataDetailDisbursalReport(param);
            dailyDisbursalReportMapper.getDataDetailDisbursalReport(paramT1);
        }catch (Exception e){
            rs.setMessage("Error when call proc!");
            rs.setSuccess(false);
            return rs;
        }
        List<DailyDisbursalReportModel> resultT = (List<DailyDisbursalReportModel>) param.get("out_cur");
        List<DailyDisbursalReportModel> resultT1 = (List<DailyDisbursalReportModel>) paramT1.get("out_cur");
        List<DailyDisbursalReportModel> lsSearch ;
        Map result = new HashMap();
        if(dailyDisbursalReportModel.getProductType().equals("All Product")){
            result.put("resultT",resultT);
            result.put("resultT1",resultT1);
            rs.setData(result);
        }else{
            lsSearch = searchData(resultT, dailyDisbursalReportModel.getProductType());
            result.put("resultT",lsSearch);
            result.put("resultT1",resultT1);
            rs.setData(result);
        }
        if(resultT.size() != 0){
            rs.setSuccess(true);
        }else {
            rs.setData(null);
            rs.setSuccess(false);
            rs.setMessage("Have no data!");
        }
        return rs;
    }
    String source= "";

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportExcel(DailyDisbursalReportModel dailyDisbursalReportModel) {
        try{
            Map<String, Object> param = new HashMap<>();
            param.put("date",dailyDisbursalReportModel.getDisbursalDate());
            param.put("out", new String());
            param.put("out_cur", new DailyDisbursalReportModel());
            dailyDisbursalReportMapper.getDataDetailDisbursalReport(param);

            List<DailyDisbursalReportModel> result = (List<DailyDisbursalReportModel>) param.get("out_cur");
            List<DailyDisbursalReportModel> lsSearch ;

            Path storage = configurations.getReportStorage(getCurrentRequest());
            String sourceTomcat = storage.resolve("Template").toString();
            String excelFilePath = sourceTomcat +"-export.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();

            source = storage.resolve("Template").toString();

            //Save Image To TomcatSer
            byte[] decodedImg = Base64.getDecoder()
                    .decode(dailyDisbursalReportModel.getBase64Img().split(",")[1]);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedImg));
            // write the image to a file
            File outputfile = new File(sourceTomcat + "myImage.png");
            ImageIO.write(image, "png", outputfile);
            // End Save Image To TomcatSer

            XSSFSheet sheet = workbook.createSheet("Reviews");

            writeHeaderLine(workbook,sheet, dailyDisbursalReportModel.getProductType(),
                    dailyDisbursalReportModel.getDisbursalDate());


            if(dailyDisbursalReportModel.getProductType().equals("All Product")){
                writeDataLines(result, workbook, sheet);
            }else{
                lsSearch = searchData(result, dailyDisbursalReportModel.getProductType());
                writeDataLines(lsSearch, workbook, sheet);
            }
            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
            workbook.close();
            File file = new File(excelFilePath);
            byte[] encoded = FileUtils.readFileToByteArray(file);

//            String aaa = new String(encoded, StandardCharsets.UTF_8);
//            System.out.println(aaa);
            return encoded;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public List<DailyDisbursalReportModel> searchData(List<DailyDisbursalReportModel> data, String productType){
        List<DailyDisbursalReportModel> result = new ArrayList<>();
        for(DailyDisbursalReportModel model: data){
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
        headerCell.setCellValue("DAILY DISBURSAL REPORT");

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
        headerCell4.setCellValue("Product");
        headerCell4 = headerRow4.createCell(2);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("NO. OF APP");
        headerCell4 = headerRow4.createCell(3);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Disbursed Amount included Insurance");
        headerCell4 = headerRow4.createCell(4);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Disbursed Amount excluded Insurance");
        headerCell4 = headerRow4.createCell(5);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Insurance amount");
        headerCell4 = headerRow4.createCell(6);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Accumulated No. of App");
        headerCell4 = headerRow4.createCell(7);
        headerCell4.setCellStyle(style);
        headerCell4.setCellValue("Accumulated Disbursed Amount");
    }

    private void writeDataLines(List<DailyDisbursalReportModel> result, XSSFWorkbook workbook,
                                XSSFSheet sheet) throws SQLException, IOException {
        int rowCount = 6;
        Drawing drawing = sheet.createDrawingPatriarch();
        DailyDisbursalReportModel modelTotal = new DailyDisbursalReportModel();
        int totalApp = 0;
        long totalincludedInsAmt = 0;
        long  totalnotInsAmt = 0;
        long totalinsAmt = 0;
        long totalaccumApp = 0;
        long totalaccumAmt = 0;
        for(DailyDisbursalReportModel model : result){
            totalApp += model.getCountApp();
            totalincludedInsAmt += Long.parseLong(model.getIncludedInsAmt());
            totalnotInsAmt += Long.parseLong(model.getNotInsAmt());
            totalinsAmt += Long.parseLong(model.getInsAmt());
            totalaccumApp += Long.parseLong(model.getAccumApp());
            totalaccumAmt += Long.parseLong(model.getAccumAmt());
        }
        modelTotal.setCountApp(totalApp);
        modelTotal.setIncludedInsAmt(String.valueOf(totalincludedInsAmt));
        modelTotal.setNotInsAmt(String.valueOf(totalnotInsAmt));
        modelTotal.setInsAmt(String.valueOf(totalinsAmt));
        modelTotal.setAccumApp(String.valueOf(totalaccumApp));
        modelTotal.setAccumAmt(String.valueOf(totalaccumAmt));
        modelTotal.setProduct("TOTAL");
        modelTotal.setLogDt(null);
        result.add(modelTotal);

        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("MM/dd/yyyy"));

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        for(DailyDisbursalReportModel model : result){

            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            if(model.getProduct().equals("TOTAL")){
                Cell cell = row.createCell(columnCount++);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(model.getLogDt());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getProduct());

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
                cell.setCellValue(model.getAccumApp());

                cell = row.createCell(columnCount++);
                cell.setCellStyle(style);
                cell.setCellValue(model.getAccumAmt());
            }else{
                Cell cell = row.createCell(columnCount++);

                cell.setCellStyle(cellStyle);
                cell.setCellValue(model.getLogDt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getProduct());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getCountApp());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getIncludedInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getNotInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getInsAmt());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getAccumApp());

                cell = row.createCell(columnCount++);
                cell.setCellValue(model.getAccumAmt());
            }

        }


        Row row1 = sheet.createRow(rowCount+4);
        row1.setHeight((short) 1000);
        sheet.autoSizeColumn(1);
        sheet.setColumnWidth(1,10000);
        insertImageToCell(workbook, "xlsx", rowCount, drawing, "dailyDisbursalRptChart.png");
    }
    private void insertImageToCell(Workbook workbook, String fileType, int rowNum, Drawing drawing,
                                   String imageName) throws IOException {

        //Loading image from application resource
        InputStream is = new FileInputStream(source + "myImage.png");

        //Converting input stream into byte array
        byte[] inputImageBytes = IOUtils.toByteArray(is);
        int inputImagePictureID = workbook.addPicture(inputImageBytes, Workbook.PICTURE_TYPE_PNG);
        is.close();
        ClientAnchor anchor = new XSSFClientAnchor();;
        //Creating the client anchor based on file format

        anchor.setCol1(1);
        anchor.setCol2(2);
        anchor.setRow1(rowNum +2);
        anchor.setRow2(rowNum + 15);
        drawing.createPicture(anchor, inputImagePictureID);

    }
}
