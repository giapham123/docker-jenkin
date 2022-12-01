package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.AccountInfoSimMapper;
import com.dou.acctmanagement.mappers.ReportRequestMapper;
import com.dou.acctmanagement.models.*;
import com.dou.acctmanagement.services.ReportRequestInterface;
import com.dou.adm.security.JwtProvider;
import com.dou.adm.shared.ResponseObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportRequestService implements ReportRequestInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportRequestService.class);
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private static final String PATTERN_DATE = "MMddyyyy";

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ReportRequestMapper reportRequestMapper;

    @Autowired
    private AccountInfoSimMapper accountInfoSimMapper;

    private static List<String> HEADERS_REQUEST_SUMMARY_REPORT = new ArrayList<>();
    private static List<String> HEADERS_REQUEST_DETAIL_REPORT = new ArrayList<>();
    private static List<String> HEADERS_REQUEST_PERMISSION_REPORT = new ArrayList<>();

    static {
        HEADERS_REQUEST_SUMMARY_REPORT.add("STT");
        HEADERS_REQUEST_SUMMARY_REPORT.add("RequestID");
        HEADERS_REQUEST_SUMMARY_REPORT.add("AccountID");
        HEADERS_REQUEST_SUMMARY_REPORT.add("RequestType");
        HEADERS_REQUEST_SUMMARY_REPORT.add("Request Note");
        HEADERS_REQUEST_SUMMARY_REPORT.add("Request By");
        HEADERS_REQUEST_SUMMARY_REPORT.add("Request Time");
        HEADERS_REQUEST_SUMMARY_REPORT.add("Verify Status");


        HEADERS_REQUEST_DETAIL_REPORT.add("STT");
        HEADERS_REQUEST_DETAIL_REPORT.add("RequestID");
        HEADERS_REQUEST_DETAIL_REPORT.add("AccountID");
        HEADERS_REQUEST_DETAIL_REPORT.add("RequestType");
        HEADERS_REQUEST_DETAIL_REPORT.add("AccountName");
        HEADERS_REQUEST_DETAIL_REPORT.add("Department");
        HEADERS_REQUEST_DETAIL_REPORT.add("MAFC Code");
        HEADERS_REQUEST_DETAIL_REPORT.add("Email");
        HEADERS_REQUEST_DETAIL_REPORT.add("Phone");
        HEADERS_REQUEST_DETAIL_REPORT.add("Mobile");
        HEADERS_REQUEST_DETAIL_REPORT.add("Level");
        HEADERS_REQUEST_DETAIL_REPORT.add("Position Company ID");
        HEADERS_REQUEST_DETAIL_REPORT.add("Is Deleted");
        HEADERS_REQUEST_DETAIL_REPORT.add("Joinning Date");
        HEADERS_REQUEST_DETAIL_REPORT.add("End Date");
        HEADERS_REQUEST_DETAIL_REPORT.add("Branch ID");
        HEADERS_REQUEST_DETAIL_REPORT.add("Supervisor ID");
        HEADERS_REQUEST_DETAIL_REPORT.add("Is Admin");
        HEADERS_REQUEST_DETAIL_REPORT.add("Staff Type");
        HEADERS_REQUEST_DETAIL_REPORT.add("Group");
        HEADERS_REQUEST_DETAIL_REPORT.add("Team");
        HEADERS_REQUEST_DETAIL_REPORT.add("Area");
        HEADERS_REQUEST_DETAIL_REPORT.add("Personal ID");
        HEADERS_REQUEST_DETAIL_REPORT.add("Is Change Dept");
        HEADERS_REQUEST_DETAIL_REPORT.add("Verify Status");
        HEADERS_REQUEST_DETAIL_REPORT.add("User Verify");
        HEADERS_REQUEST_DETAIL_REPORT.add("Verify Time");
        HEADERS_REQUEST_DETAIL_REPORT.add("Verify Note");

        HEADERS_REQUEST_PERMISSION_REPORT.add("STT");
        HEADERS_REQUEST_PERMISSION_REPORT.add("RequestID");
        HEADERS_REQUEST_PERMISSION_REPORT.add("AccountID");
        HEADERS_REQUEST_PERMISSION_REPORT.add("GroupID");
        HEADERS_REQUEST_PERMISSION_REPORT.add("Verify Status");
        HEADERS_REQUEST_PERMISSION_REPORT.add("Verify By");
        HEADERS_REQUEST_PERMISSION_REPORT.add("Verify Time");
        HEADERS_REQUEST_PERMISSION_REPORT.add("Verify Note");
    }

    public ResponseObject<List<AccountInfo>> getAccountInfoSearch(AccountInfo accountInfo) {
        List<AccountInfo> listAcctInfo = new ArrayList<>();
        if (accountInfo.getAccountId() != null && !accountInfo.getAccountId().equals("")) {
            accountInfo.setDepartmentId(null);
        }
        listAcctInfo = reportRequestMapper.getAccountInfoSearch(accountInfo.getAccountId(), accountInfo.getDepartmentId());

        return new ResponseObject(listAcctInfo);
    }

    public ResponseObject<List<RequestSummary>> getAccountRequestSummaryReport(FilterSim_VerifyTicket accountInfo) {
        List<RequestSummary> listAcctInfo = new ArrayList<>();
        if ((accountInfo.getAccountId() != null && !accountInfo.getAccountId().equals("")) || (accountInfo.getRequestId() != null && !accountInfo.getRequestId().equals(""))) {
            accountInfo.setRequestType(null);
        }
        listAcctInfo = reportRequestMapper.getAccountRequestSummaryReport(accountInfo);

        return new ResponseObject(listAcctInfo);
    }

    public void getRequestAllScheduleEmail() throws Exception {
        List<RequestSummary> requestSummaries = reportRequestMapper.getAccountRequestSummaryScheduleMail();
        List<AccountInfoDetail> infoDetailList = reportRequestMapper.getAccountRequestDetailScheduleMail();
        List<AccountPermissionGroupSimFeature> listGroupSimFeatures = reportRequestMapper.getAccountRequestPermissionScheduleMail();
        this.importFileRequestScheduleMail(requestSummaries, infoDetailList, listGroupSimFeatures);
    }

    public void importFileRequestScheduleMail(List<RequestSummary> listRequestSummary, List<AccountInfoDetail> infoDetailList,
                                              List<AccountPermissionGroupSimFeature> listGroupSimFeatures) throws Exception{
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_DATE);
            String date = simpleDateFormat.format(new Date());
            String fileName = "FileAccountManagerment_"+date;
            Workbook workbook = new HSSFWorkbook();

            // Create a Sheet
            Sheet sheet = workbook.createSheet("RequestSummary");
            Sheet sheet2 = workbook.createSheet("RequestDetail");
            Sheet sheet3 = workbook.createSheet("RequestPermission");

            // Create a Font for styling header cells
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 9);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            // Create a Row
            Row headerRow = sheet.createRow(0);
            Row headerRowTab2 = sheet2.createRow(0);
            Row headerRowTab3 = sheet3.createRow(0);

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            //Tab 1
            for (int i = 0; i < HEADERS_REQUEST_SUMMARY_REPORT.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS_REQUEST_SUMMARY_REPORT.get(i));
                cell.setCellStyle(headerCellStyle);
            }
            // Create Other rows and cells with employees data
            int rowNum = 1;
            for (RequestSummary requestSummary : listRequestSummary) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(requestSummary.getRequestId());
                row.createCell(2).setCellValue(requestSummary.getAccountId());
                row.createCell(3).setCellValue(requestSummary.getRequestTypeName());
                row.createCell(4).setCellValue(requestSummary.getRequestNote());
                row.createCell(5).setCellValue(requestSummary.getRequestBy());
                row.createCell(6).setCellValue(requestSummary.getRequestTimeString());
                row.createCell(7).setCellValue(requestSummary.getVerifyTimeString());
            }
            // Resize all columns to fit the content size
            for (int i = 0; i < HEADERS_REQUEST_SUMMARY_REPORT.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            //Tab2-------
            for (int i = 0; i < HEADERS_REQUEST_DETAIL_REPORT.size(); i++) {
                Cell cell = headerRowTab2.createCell(i);
                cell.setCellValue(HEADERS_REQUEST_DETAIL_REPORT.get(i));
                cell.setCellStyle(headerCellStyle);
            }
            // Create Other rows and cells with employees data
            int rowNumTab2 = 1;
            for (AccountInfoDetail requestDetail : infoDetailList) {
                Row row = sheet2.createRow(rowNumTab2++);
                row.createCell(0).setCellValue(rowNumTab2 - 1);
                row.createCell(1).setCellValue(requestDetail.getRequestId());
                row.createCell(2).setCellValue(requestDetail.getAccountId());
                row.createCell(3).setCellValue(requestDetail.getRequestTypeName());
                row.createCell(4).setCellValue(requestDetail.getFullName());
                row.createCell(5).setCellValue(requestDetail.getDepartmentName());
                row.createCell(6).setCellValue(requestDetail.getMafcCode());
                row.createCell(7).setCellValue(requestDetail.getEmail());
                row.createCell(8).setCellValue(requestDetail.getDeptPhone());
                row.createCell(9).setCellValue(requestDetail.getMobile());
                if(requestDetail.getLevel() != null) {
                    row.createCell(10).setCellValue(requestDetail.getLevel());
                }else {
                    row.createCell(10).setCellValue("");
                }
                row.createCell(11).setCellValue(requestDetail.getPosition());
                row.createCell(12).setCellValue(requestDetail.getIsDeleted());
                row.createCell(13).setCellValue(requestDetail.getJoinDateString());
                row.createCell(14).setCellValue(requestDetail.getEndDateString());
                row.createCell(15).setCellValue(requestDetail.getBranchId());
                row.createCell(16).setCellValue(requestDetail.getSupervisorCode());
                row.createCell(17).setCellValue(requestDetail.getIsAdmin());
                row.createCell(18).setCellValue(requestDetail.getStaffType());
                row.createCell(19).setCellValue(requestDetail.getGroup());
                row.createCell(20).setCellValue(requestDetail.getTeam());

                row.createCell(21).setCellValue(requestDetail.getArea());
                row.createCell(22).setCellValue(requestDetail.getPersonalId());
                if(requestDetail.getIsChangeDept() != null) {
                    if(requestDetail.getIsChangeDept().equals("1") || requestDetail.getIsChangeDept() == 1){
                        row.createCell(23).setCellValue("Yes");
                    }else {
                        row.createCell(23).setCellValue("No");
                    }
                }else {
                    row.createCell(23).setCellValue("No");
                }
                row.createCell(24).setCellValue(requestDetail.getVerifyStatus());
                row.createCell(25).setCellValue(requestDetail.getUserVerify());
                row.createCell(26).setCellValue(requestDetail.getTimeVerify());
                row.createCell(27).setCellValue(requestDetail.getVerifyNote());
            }
            // Resize all columns to fit the content size
            for (int i = 0; i < HEADERS_REQUEST_DETAIL_REPORT.size(); i++) {
                sheet2.autoSizeColumn(i);
            }

            //Tab 3--------
            for (int i = 0; i < HEADERS_REQUEST_PERMISSION_REPORT.size(); i++) {
                Cell cell = headerRowTab3.createCell(i);
                cell.setCellValue(HEADERS_REQUEST_PERMISSION_REPORT.get(i));
                cell.setCellStyle(headerCellStyle);
            }
            int rowNumTab3 = 1;
            for (AccountPermissionGroupSimFeature requestPermission : listGroupSimFeatures) {
                Row row = sheet3.createRow(rowNumTab3++);
                row.createCell(0).setCellValue(rowNumTab3 - 1);
                row.createCell(1).setCellValue(requestPermission.getRequestId());
                row.createCell(2).setCellValue(requestPermission.getAccountId());
                row.createCell(3).setCellValue(requestPermission.getGroupId());
                row.createCell(4).setCellValue(requestPermission.getVerifyStatus());
                row.createCell(5).setCellValue(requestPermission.getVerifyBy());
                row.createCell(6).setCellValue(requestPermission.getVerifyTimeString());
                row.createCell(7).setCellValue(requestPermission.getVerifyNote());
            }
            // Resize all columns to fit the content size
            for (int i = 0; i < HEADERS_REQUEST_PERMISSION_REPORT.size(); i++) {
                sheet3.autoSizeColumn(i);
            }
            String pathFile = "ReportDaily/";

            File file = new File(pathFile + fileName +".xls"); //E:\HANG\ACCOUNT_MANAGEMENT
            file.getParentFile().mkdirs();

            FileOutputStream outFile = new FileOutputStream(file);
            workbook.write(outFile);
            System.out.println("Created file: " + file.getAbsolutePath());
            }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
