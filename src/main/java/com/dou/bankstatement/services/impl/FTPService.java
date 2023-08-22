package com.dou.bankstatement.services.impl;

import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.mappers.FTPMapper;
import com.dou.bankstatement.mappers.ForwardDB;
import com.dou.bankstatement.models.*;
import com.dou.bankstatement.services.FTPServiceInterface;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class FTPService implements FTPServiceInterface {
    private static final int FTP_TIMEOUT = 60000000;
    private FTPClient _ftpClient;

    @Autowired
    private FTPMapper _ftpMapper;

    @Autowired
    private ForwardDB _forwardDB;

    @Value("${ftp.server.address.ip}")
    private String _mainServer;

    @Value("${ftp.server.address.port}")
    private int _mainPort;

    @Value("${ftp.server.auth.username}")
    private String _mainUsername;

    @Value("${ftp.server.auth.password}")
    private String _mainPassword;

    DateTimeFormatter _dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public boolean connectFTPServer() {
        boolean isSuccess = false;
        _ftpClient = new FTPClient();
        try {
            // connect to ftp server
            _ftpClient.setDefaultTimeout(FTP_TIMEOUT);
            _ftpClient.connect(_mainServer, _mainPort);
            // run the passive mode command
            _ftpClient.enterLocalPassiveMode();
            // check reply code
            if (!FTPReply.isPositiveCompletion(_ftpClient.getReplyCode())) {
                disconnectFTPServer();
                isSuccess = false;
                throw new IOException("Connect failed!");
            }
            else
            {
                _ftpClient.setSoTimeout(FTP_TIMEOUT);
                if(!_ftpClient.login(_mainUsername, _mainPassword))
                {
                    isSuccess = false;
                    throw new IOException("Could not login to the server!");
                }
                _ftpClient.setDataTimeout(FTP_TIMEOUT);
                isSuccess = true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean disconnectFTPServer() {
        boolean isSuccess = false;
        if (_ftpClient != null && _ftpClient.isConnected()) {
            try {
                _ftpClient.logout();
                _ftpClient.disconnect();
                isSuccess = true;
            } catch (IOException ex) {
                isSuccess = false;
                ex.printStackTrace();
            }
        }
        return isSuccess;
    }

    public List<String> listingFTP(String folder){
        connectFTPServer();
        List<String> list = new ArrayList<>();
        try
        {
            FTPFile[] remoteFiles = _ftpClient.listFiles(folder);
            for(FTPFile remoteFile : remoteFiles)
            {
                if (!remoteFile.getName().equals(".") && !remoteFile.getName().equals(".."))
                {
                    list.add(folder + "/" + remoteFile.getName());
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        disconnectFTPServer();
        return list;
    }

    private boolean deleteFolder(String remotePath) throws IOException{
        boolean success = true;
        FTPFile[] remoteFiles = _ftpClient.listFiles(remotePath);
        for (FTPFile remoteFile : remoteFiles)
        {
            if (!remoteFile.getName().equals(".") && !remoteFile.getName().equals(".."))
            {
                if(!deleteFTP(remotePath))
                {
                    success = false;
                    System.out.println(String.format("%s Cannot delete file path %s", LocalDateTime.now().format(_dtf), remotePath));
                }
            }
        }
        if(success)
        {
            _ftpClient.removeDirectory(remotePath);
            System.out.println(String.format("%s Folder %s deleted", LocalDateTime.now().format(_dtf), remotePath));
        }
        disconnectFTPServer();
        return success;
    }

    public boolean DeleteFTP(String path) {
        connectFTPServer();
        try {
            return deleteFolder(path);
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
    }

    private boolean deleteFTP(String path) {
        boolean removed = false;
        String dirPath = path;
        try {
            removed = _ftpClient.deleteFile(dirPath);
            if (removed) {
                System.out.println("DELETED the file: " + dirPath);
            } else {
                System.out.println("CANNOT delete the file: " + dirPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return removed;
    }

    public boolean CopyFileFTP(String path){
        try {
            connectFTPServer();
            return listFolder(_ftpClient, path, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean listFolder(FTPClient ftpClient, String remotePath, String newPath) throws IOException
    {
        boolean success = true;
        try
        {
            if(remotePath.split("/")[2].length() > 0)
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("_yyyy-MM-dd");
                if(remotePath.toUpperCase().contains("ANBINH"))
                {
                    String bankFolder = "An Binh";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("DONGA"))
                {
                    String bankFolder = "Dong A";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("HDB"))
                {
                    String bankFolder = "HDbank";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("BIDV"))
                {
                    String bankFolder = "BIDV";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("HSBC"))
                {
                    String bankFolder = "HSBC";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("SACOM"))
                {
                    String bankFolder = "Sacombank";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("SHB"))
                {
                    String bankFolder = "Shinhan";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("TECHCOM"))
                {
                    String bankFolder = "Techcombank";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("VCB"))
                {
                    String bankFolder = "Vietcombank";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("VIETIN"))
                {
                    String bankFolder = "Vietinbank";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("LPB"))
                {
                    String bankFolder = "Lienviet";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }

                else if(remotePath.toUpperCase().contains("WOORI"))
                {
                    String bankFolder = "WOORI";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("KOOKMIN"))
                {
                    String bankFolder = "KOOKMIN";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("KEB"))
                {
                    String bankFolder = "KEB";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("VIETBANK"))
                {
                    String bankFolder = "VIETBANK";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("NONGHYUP"))
                {
                    String bankFolder = "NONGHYUP";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("MBB"))
                {
                    String bankFolder = "MBB";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("PVC"))
                {
                    String bankFolder = "PVC";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("VIETCAP"))
                {
                    String bankFolder = "VIETCAP";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("MAYBANK"))
                {
                    String bankFolder = "MAYBANK";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("SEABANK"))
                {
                    String bankFolder = "SEABANK";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("UOB"))
                {
                    String bankFolder = "UOB";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("CITI"))
                {
                    String bankFolder = "CITI";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                else if(remotePath.toUpperCase().contains("MANUAL"))
                {
                    String bankFolder = "MANUAL";
                    newPath = String.format("/%s/%s/%s", remotePath.split("/")[1] + "_backup", bankFolder, bankFolder + LocalDateTime.now().format(dtf));
                }
                if(makeDirectory(ftpClient, newPath))
                {
                    System.out.println(String.format("%s Folder created", LocalDateTime.now().format(_dtf)));
                }
            }
        }
        catch (Exception ex)
        {
        }
        FTPFile[] remoteFiles = ftpClient.listFiles(remotePath);
        for (FTPFile remoteFile : remoteFiles)
        {
            if (!remoteFile.getName().equals(".") && !remoteFile.getName().equals(".."))
            {
                String remoteFilePath = remotePath;
                newPath = newPath + "/" + remoteFile.getName();
                if(newPath.contains("/"))
                {
                    System.out.println(newPath);
                    success = downloadFTPFile(remoteFilePath, newPath);
                }
            }
        }
        disconnectFTPServer();
        return success;
    }

    private boolean downloadFTPFile(String ftpFilePath, String newFilePath) {
        InputStream stream = null;
        InputStream in = null;
        boolean success = false;
        if(_ftpClient.getReplyString() == null)
        {
            connectFTPServer();
        }
        try {
            String url = String.format("ftp://%s:%s@%s%s", _mainUsername, _mainPassword, _mainServer, ftpFilePath);
            in = new BufferedInputStream(new URL(url).openStream());
            byte[] bytes = IOUtils.toByteArray(in);
            stream = new ByteArrayInputStream(bytes);
            _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            _ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            success = _ftpClient.storeFile(newFilePath, stream);
            stream.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            System.out.println(String.format("%s File %s has been downloaded successfully.", LocalDateTime.now().format(_dtf), ftpFilePath));
        }
        disconnectFTPServer();
        return success;
    }

    public boolean makeDirectory(FTPClient ftpClient, String subDirPath) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                if (StringUtils.hasText(subDirPath)) {
                    boolean targetExist = ftpClient.changeWorkingDirectory(subDirPath);
                    if (!targetExist) {
                        if (!ftpClient.makeDirectory(subDirPath)) {
                            System.out.println(String.format("%s Could not make directory : %s", LocalDateTime.now().format(_dtf), subDirPath));
                            return false;
                        }

                        ftpClient.changeWorkingDirectory(subDirPath);
                    }
                }
                return true;
            } catch (Exception e) {
                System.out.println("Error occurred while trying to upload a file to remote");
            }
        }
        return false;
    }

    public String excelFTP(String ftpFilePath){
        String result = "";
        String cell = "";
        String textCell = "";
        InputStream inputStream = null;
        DataFormatter formatter = new DataFormatter();
        connectFTPServer();
        try
        {
            System.out.println(ftpFilePath);
            inputStream = _ftpClient.retrieveFileStream(ftpFilePath);
            if(ftpFilePath.contains(".xlsx"))
            {
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet worksheet = workbook.getSheetAt(0);
                for(int i = worksheet.getPhysicalNumberOfRows() - 1; i > 0; i--)
                {
                    XSSFRow row = worksheet.getRow(i);
                    if(row != null)
                    {
                        String substring = ftpFilePath.substring(ftpFilePath.length() - 8);
                        if(ftpFilePath.toUpperCase().contains("HSBC"))
                        {
                            row = worksheet.getRow(1);
                            if(formatter.formatCellValue(row.getCell(23)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(23));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("HSBC");
                                try
                                {
                                    if(substring.contains("002"))
                                    {
                                        model.setAcct("091016006002");
                                        model.setSapAcct("131104");
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                }
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("BIDV"))
                        {
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(5));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("BIDV");
                                try
                                {
                                    if(substring.contains("979"))
                                    {
                                        model.setSapAcct("131108");
                                        model.setAcct("31010001052979");
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                }
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("DONGA"))
                        {
                            if(formatter.formatCellValue(row.getCell(1)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(1));
                                cell = cell.split(":")[1].trim();
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("Đông Á");
                                try
                                {
                                    if(substring.contains("001"))
                                    {
                                        model.setSapAcct("131115");
                                        model.setAcct("014666240001");
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                }
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("HDB"))
                        {
                            if(formatter.formatCellValue(row.getCell(3)).trim().length() != 0)
                            {
                                textCell = formatter.formatCellValue(row.getCell(0));
                                if(textCell.contains("Số dư cuối kỳ"))
                                {
                                    cell = formatter.formatCellValue(row.getCell(3));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("HDbank");
                                    try
                                    {
                                        if(substring.contains("588"))
                                        {
                                            model.setSapAcct("131120");
                                            model.setAcct("058704070006588");
                                        }
                                        else if(substring.contains("627"))
                                        {
                                            model.setSapAcct("131156");
                                            model.setAcct("003704070022627");
                                        }
                                        else if(substring.contains("888"))
                                        {
                                            model.setSapAcct("131149");
                                            model.setAcct("003704070666888");
                                        }
                                        else if(substring.contains("666"))
                                        {
                                            model.setSapAcct("131133");
                                            model.setAcct("059704070333666");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                    }
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("VIETIN"))
                        {
                            if(formatter.formatCellValue(row.getCell(2)).trim().length() != 0)
                            {
                                textCell = formatter.formatCellValue(row.getCell(1));
                                if(textCell.contains("Closing Balance"))
                                {
                                    cell = formatter.formatCellValue(row.getCell(2));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("Vietinbank");
                                    try
                                    {
                                        if(substring.contains("491"))
                                        {
                                            model.setSapAcct("131116");
                                            model.setAcct("137000000491");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                    }
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("ANBINH"))
                        {
                            row = worksheet.getRow(i + 2);
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(5));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("An Bình");
                                try
                                {
                                    if(substring.contains("039"))
                                    {
                                        model.setSapAcct("131118");
                                        model.setAcct("0521043964039");
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                }
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("LPB"))
                        {
//                            row = worksheet.getRow(1);
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(5));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("Lienvietpostbank");
                                try
                                {
                                    if(substring.contains("001"))
                                    {
                                        model.setSapAcct("131127");
                                        model.setAcct("999996590001");
                                    }
                                    else if(substring.contains("999"))
                                    {
                                        model.setSapAcct("131106");
                                        model.setAcct("999996599999");
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                }
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("SHB"))
                        {
                            row = worksheet.getRow(1);
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(5));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("Shinhan");
                                try
                                {
                                    if(substring.contains("521"))
                                    {
                                        model.setSapAcct("131102");
                                        model.setAcct("700000316521");
                                    }
                                    else if(substring.contains("126"))
                                    {
                                        model.setSapAcct("131125");
                                        model.setAcct("700-009-319126");
                                    }
                                    else if(substring.contains("095"))
                                    {
                                        model.setSapAcct("131138");
                                        model.setAcct("700013989095");
                                    }
                                    else if(substring.contains("818"))
                                    {
                                        model.setSapAcct("131142");
                                        model.setAcct("700016489818");
                                    }
                                    else if(substring.contains("530"))
                                    {
                                        model.setSapAcct("131148");
                                        model.setAcct("700017442530");
                                    }
                                    else if(substring.contains("538"))
                                    {
                                        model.setSapAcct("132102");
                                        model.setAcct("700-000-316538");
                                    }
                                    else if(substring.contains("545"))
                                    {
                                        model.setSapAcct("132103");
                                        model.setAcct("700-000-316545");
                                    }
                                    else if(substring.contains("722"))
                                    {
                                        model.setSapAcct("132105");
                                        model.setAcct("700-000-325722");
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                }
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("VCB"))
                        {
                            if(formatter.formatCellValue(row.getCell(3)).trim().length() != 0)
                            {
                                textCell = formatter.formatCellValue(row.getCell(2));
                                if(textCell.contains("Số dư cuối kỳ"))
                                {
                                    cell = formatter.formatCellValue(row.getCell(3));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("Vietcombank");
                                    try
                                    {
                                        if(substring.contains("289"))
                                        {
                                            model.setSapAcct("131112");
                                            model.setAcct("0071000809289");
                                        }
                                        else if(substring.contains("785"))
                                        {
                                            model.setSapAcct("131160");
                                            model.setAcct("1031143785");
                                        }
                                        else if(substring.contains("190"))
                                        {
                                            model.setSapAcct("131161");
                                            model.setAcct("1031144190");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                    }
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("SACOM"))
                        {
                            if(substring.contains("688"))
                            {
                                if(formatter.formatCellValue(row.getCell(2)).trim().length() != 0)
                                {
                                    if(row.getCell(1).toString().contains("Số dư cuối kỳ:"))
                                    {
                                        textCell = formatter.formatCellValue(row.getCell(1));
                                        if(textCell.contains("Số dư cuối kỳ:"))
                                        {
                                            cell = formatter.formatCellValue(row.getCell(2));
                                            BankStatementModel model = new BankStatementModel();
                                            model.setBalance(numberFormatted(cell));
                                            model.setFileName(ftpFilePath);
                                            model.setBankName("Sacombank");
                                            model.setSapAcct("131114");
                                            model.setAcct("060102703688");
                                            _forwardDB.bankStatement(model);
                                            return cell;
                                        }
                                    }
                                }
                                if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                                {
                                    if(row.getCell(2).toString().contains("Số dư cuối kỳ:"))
                                    {
                                        textCell = formatter.formatCellValue(row.getCell(2));
                                        if(textCell.contains("Số dư cuối kỳ:"))
                                        {
                                            cell = formatter.formatCellValue(row.getCell(4));
                                            BankStatementModel model = new BankStatementModel();
                                            model.setBalance(numberFormatted(cell));
                                            model.setFileName(ftpFilePath);
                                            model.setBankName("Sacombank");
                                            model.setSapAcct("131114");
                                            model.setAcct("060102703688");
                                            _forwardDB.bankStatement(model);
                                            return cell;
                                        }
                                    }
                                }
                            }
                            else if(substring.contains("058"))
                            {
                                if(formatter.formatCellValue(row.getCell(2)).trim().length() != 0)
                                {
                                    if(row.getCell(1).toString().contains("Số dư cuối kỳ:"))
                                    {
                                        textCell = formatter.formatCellValue(row.getCell(1));
                                        if(textCell.contains("Số dư cuối kỳ:"))
                                        {
                                            cell = formatter.formatCellValue(row.getCell(2));
                                            BankStatementModel model = new BankStatementModel();
                                            model.setBalance(numberFormatted(cell));
                                            model.setFileName(ftpFilePath);
                                            model.setBankName("Sacombank");
                                            model.setSapAcct("131143");
                                            model.setAcct("060244908058");
                                            _forwardDB.bankStatement(model);
                                            return cell;
                                        }
                                    }
                                }
                                if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                                {
                                    if(row.getCell(2).toString().contains("Số dư cuối kỳ:"))
                                    {
                                        textCell = formatter.formatCellValue(row.getCell(2));
                                        if(textCell.contains("Số dư cuối kỳ:"))
                                        {
                                            cell = formatter.formatCellValue(row.getCell(4));
                                            BankStatementModel model = new BankStatementModel();
                                            model.setBalance(numberFormatted(cell));
                                            model.setFileName(ftpFilePath);
                                            model.setBankName("Sacombank");
                                            model.setSapAcct("131143");
                                            model.setAcct("060244908058");
                                            _forwardDB.bankStatement(model);
                                            return cell;
                                        }
                                    }
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("TCB"))
                        {
                            //Old format get colum 7 5 7
                            if(formatter.formatCellValue(row.getCell(8)).trim().length() != 0)
                            {
                                textCell = formatter.formatCellValue(row.getCell(6));
                                if(textCell.contains("Closing balance"))
                                {
                                    cell = formatter.formatCellValue(row.getCell(8));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("Techcombank");
                                    try
                                    {
                                        if(substring.contains("028"))
                                        {
                                            model.setSapAcct("131109");
                                            model.setAcct("19125812410028");
                                        }
                                        else if(substring.contains("022"))
                                        {
                                            model.setSapAcct("131137");
                                            model.setAcct("19125812410022");
                                        }
                                        else if(substring.contains("011"))
                                        {
                                            model.setSapAcct("131151");
                                            model.setAcct("19025812410011");
                                        }
                                        else if(substring.contains("073"))
                                        {
                                            model.setSapAcct("131152");
                                            model.setAcct("19125812410073");
                                        }
                                        else if(substring.contains("036"))
                                        {
                                            model.setSapAcct("131155");
                                            model.setAcct("19125812410036");
                                        }
                                        else if(substring.contains("014"))
                                        {
                                            model.setSapAcct("132106");
                                            model.setAcct("19125812410014");
                                        }
                                        else if(substring.contains("044"))
                                        {
                                            model.setSapAcct("131165");
                                            model.setAcct("19025812410044");
                                        }
                                        else if(substring.contains("052"))
                                        {
                                            model.setSapAcct("131166");
                                            model.setAcct("19025812410052");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                    }
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("WOORI"))
                        {
                            if(i == 2)
                            {
                                if(formatter.formatCellValue(row.getCell(11)).trim().length() != 0)
                                {
                                    cell = formatter.formatCellValue(row.getCell(11));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("WooriBank");
                                    model.setSapAcct("131101");
                                    model.setAcct("100912087906");
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("KOOKMIN"))
                        {
                            if(i == 5)
                            {
                                if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                                {
                                    cell = formatter.formatCellValue(row.getCell(5));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("KookMin");
                                    model.setSapAcct("131111");
                                    model.setAcct("810001100000138");
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("KEB"))
                        {
                            row = worksheet.getRow(i + 1);
                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(4));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("Keb Hana");
                                model.setSapAcct("131122");
                                model.setAcct("0720054466");
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("VIETBANK"))
                        {
                            row = worksheet.getRow(i + 3);
                            if(formatter.formatCellValue(row.getCell(1)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(1)).replace("(VND)", "");
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("Vietbank");
                                model.setSapAcct("131135");
                                model.setAcct("000000217554");
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("NONGHYUP"))
                        {
                            if(i == 1)
                            {
                                if(formatter.formatCellValue(row.getCell(3)).trim().length() != 0)
                                {
                                    cell = formatter.formatCellValue(row.getCell(3));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("NH Bank");
                                    model.setSapAcct("131139");
                                    model.setAcct("6015902001999");
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("MBB"))
                        {
                            if(i == 8)
                            {
                                if(formatter.formatCellValue(row.getCell(7)).trim().length() != 0)
                                {
                                    cell = formatter.formatCellValue(row.getCell(7));
                                    BankStatementModel model = new BankStatementModel();
                                    model.setBalance(numberFormatted(cell));
                                    model.setFileName(ftpFilePath);
                                    model.setBankName("MBBank");
                                    model.setSapAcct("131140");
                                    model.setAcct("1031102220001");
                                    _forwardDB.bankStatement(model);
                                    return cell;
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("PVC"))
                        {
                            row = worksheet.getRow(i - 1);
                            if(formatter.formatCellValue(row.getCell(6)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(6));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("Pvcombank");
                                model.setSapAcct("131144");
                                model.setAcct("105000659162");
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("VIETCAP"))
                        {
                            if(formatter.formatCellValue(row.getCell(6)).trim().length() != 0)
                            {
                                if(row.getCell(5).toString().contains("SỐ DƯ CUỐI KỲ:"))
                                {
                                    textCell = formatter.formatCellValue(row.getCell(5));
                                    if(textCell.contains("SỐ DƯ CUỐI KỲ:"))
                                    {
                                        cell = formatter.formatCellValue(row.getCell(6));
                                        BankStatementModel model = new BankStatementModel();
                                        model.setBalance(numberFormatted(cell));
                                        model.setFileName(ftpFilePath);
                                        model.setBankName("VietCap");
                                        model.setSapAcct("131146");
                                        model.setAcct("0107041696969");
                                        _forwardDB.bankStatement(model);
                                        return cell;
                                    }
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("MAYBANK"))
                        {
                            if(formatter.formatCellValue(row.getCell(11)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(11));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("Maybank");
                                model.setSapAcct("131150");
                                model.setAcct("101020000007434");
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("SEABANK"))
                        {
                            if(formatter.formatCellValue(row.getCell(7)).trim().length() != 0)
                            {
                                if(row.getCell(5).toString().contains("Số dư cuối kỳ"))
                                {
                                    textCell = formatter.formatCellValue(row.getCell(5));
                                    if(textCell.contains("Số dư cuối kỳ"))
                                    {
                                        cell = formatter.formatCellValue(row.getCell(7));
                                        BankStatementModel model = new BankStatementModel();
                                        model.setBalance(numberFormatted(cell));
                                        model.setFileName(ftpFilePath);
                                        model.setBankName("Seabank");
                                        model.setSapAcct("131154");
                                        model.setAcct("000001209126");
                                        _forwardDB.bankStatement(model);
                                        return cell;
                                    }
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("UOB"))
                        {
                            if(formatter.formatCellValue(row.getCell(19)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(19));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName("UOB");
                                model.setSapAcct("131157");
                                model.setAcct("1053003619");
                                _forwardDB.bankStatement(model);
                                return cell;
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("CITI"))
                        {
                            if(formatter.formatCellValue(row.getCell(7)).trim().length() != 0)
                            {
                                if(row.getCell(1).toString().contains("CLOSING BALANCE")
                                        || row.getCell(1).toString().contains("SỐ DƯ CUỐI NGÀY"))
                                {
                                    textCell = formatter.formatCellValue(row.getCell(1));
                                    if(textCell.contains("CLOSING BALANCE") ||
                                            textCell.contains("SỐ DƯ CUỐI NGÀY"))
                                    {
                                        cell = formatter.formatCellValue(row.getCell(7));
                                        BankStatementModel model = new BankStatementModel();
                                        model.setBalance(numberFormatted(cell));
                                        model.setFileName(ftpFilePath);
                                        model.setBankName("CitiBank");
                                        try
                                        {
                                            if(substring.contains("006"))
                                            {
                                                model.setSapAcct("131158");
                                                model.setAcct("0101227006");
                                            }
//                                            else if(substring.contains("014"))
//                                            {
//                                                model.setSapAcct("132112");
//                                                model.setAcct("0101227014");
//                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            System.out.println(String.format("file [%s] sai cú pháp.", model.getBankName()));
                                        }
                                        _forwardDB.bankStatement(model);
                                    }
                                }
                            }
                        }
                        else if(ftpFilePath.toUpperCase().contains("MANUAL"))
                        {
                            if(formatter.formatCellValue(row.getCell(1)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(4));
                                BankStatementModel model = new BankStatementModel();
                                model.setBalance(numberFormatted(cell));
                                model.setFileName(ftpFilePath);
                                model.setBankName(formatter.formatCellValue(row.getCell(1)));
                                model.setSapAcct(formatter.formatCellValue(row.getCell(2)));
                                model.setAcct(formatter.formatCellValue(row.getCell(3)));
                                _forwardDB.bankStatement(model);
                            }
                        }


                        //coding
//                        if(ftpFilePath.toUpperCase().contains("CITI"))
//                        {
//                            if(i == 15)
//                            {
//                                if(formatter.formatCellValue(row.getCell(7)).trim().length() != 0)
//                                {
//                                    cell = formatter.formatCellValue(row.getCell(7));
//                                    System.out.println(cell);
//                                }
//                            }
//                        }
//                        if(ftpFilePath.toUpperCase().contains("MANUAL"))
//                        {
//                            //row = worksheet.getRow(i - 1);
//                            if(formatter.formatCellValue(row.getCell(1)).trim().length() != 0)
//                            {
//                                cell = formatter.formatCellValue(row.getCell(1));
//                                System.out.println(cell);
//                            }
//                            if(formatter.formatCellValue(row.getCell(2)).trim().length() != 0)
//                            {
//                                cell = formatter.formatCellValue(row.getCell(2));
//                                System.out.println(cell);
//                            }
//                            if(formatter.formatCellValue(row.getCell(3)).trim().length() != 0)
//                            {
//                                cell = formatter.formatCellValue(row.getCell(3));
//                                System.out.println(cell);
//                            }
//                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
//                            {
//                                cell = formatter.formatCellValue(row.getCell(4));
//                                System.out.println(cell);
//                            }
//                        }
//                        if(ftpFilePath.toUpperCase().contains("MANUAL"))
//                        {
//                            if(formatter.formatCellValue(row.getCell(7)).trim().length() != 0)
//                            {
//                                if(row.getCell(1).toString().contains("CLOSING BALANCE"))
//                                {
//                                    textCell = formatter.formatCellValue(row.getCell(1));
//                                    if(textCell.contains("CLOSING BALANCE"))
//                                    {
//                                        cell = formatter.formatCellValue(row.getCell(7));
//                                        System.out.println(cell);
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(inputStream != null)
                {
                    inputStream.close();
                    disconnectFTPServer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String collectorTransaction(String ftpFilePath)
    {
        String result = "";
        InputStream inputStream = null;
        List<SapCLEPayModel> sapCLEPayModelList = new ArrayList<>();
        List<SapCLMomoAppModel> sapCLMomoAppModelList = new ArrayList<>();
        List<SapCLMomoMFModel> sapCLMomoMFModelList = new ArrayList<>();
        List<SapCLOnepayModel> sapCLOnepayModelList = new ArrayList<>();
        List<SapCLPayooModel> sapCLPayooModelList = new ArrayList<>();
        List<SapCLShopeeAppModel> sapCLShopeeAppModelList = new ArrayList<>();
        List<SapCLShopeePayCTModel> sapCLShopeePayCTModelList = new ArrayList<>();
        List<SapCLSmartNetModel> sapCLSmartNetModelList = new ArrayList<>();
        List<SapCLViettelAppModel> sapCLViettelAppModelList = new ArrayList<>();
        List<SapCLViettelCounterModel> sapCLViettelCounterModelList = new ArrayList<>();
        List<SapCLViMoModel> sapCLViMoModelList = new ArrayList<>();
        List<SapCLVnPayBillingModel> sapCLVnPayBillingModelList = new ArrayList<>();
        List<SapCLVNPayGWModel> sapCLVNPayGWModelList = new ArrayList<>();
        List<SapCLVnPostModel> sapCLVnPostModelList = new ArrayList<>();
        List<SapCLVNPTPayModel> sapCLVNPTPayModelList = new ArrayList<>();
        List<SapCLZaloPayModel> sapCLZaloPayModelList = new ArrayList<>();

        List<SapCLCollectorConfigModel> list = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        connectFTPServer();
        try
        {
            System.out.println(ftpFilePath);
            inputStream = _ftpClient.retrieveFileStream(ftpFilePath);

            if(ftpFilePath.split("/")[2].split("_")[0].equals("EPAY"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if (row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(8)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLEPayModel model = new SapCLEPayModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("SO_HOP_DONG"))
                                        {
                                            model.setSo_hop_dong(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HO_TEN"))
                                        {
                                            model.setHo_ten(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLEPayModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLEPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLEPay(sapCLEPayModelList);
                                }
                                sapCLEPayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLEPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLEPay(sapCLEPayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLEPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLEPay(sapCLEPayModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(8)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLEPayModel model = new SapCLEPayModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("SO_HOP_DONG"))
                                        {
                                            model.setSo_hop_dong(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HO_TEN"))
                                        {
                                            model.setHo_ten(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLEPayModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLEPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLEPay(sapCLEPayModelList);
                                }
                                sapCLEPayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLEPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLEPay(sapCLEPayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLEPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLEPay(sapCLEPayModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("MOMOAPP"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLMomoAppModel model = new SapCLMomoAppModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("SO_HD"))
                                        {
                                            model.setSo_hd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KH"))
                                        {
                                            model.setTen_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SDT"))
                                        {
                                            model.setSdt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TID"))
                                        {
                                            model.setTid(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLMomoAppModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLMomoAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoApp(sapCLMomoAppModelList);
                                }
                                sapCLMomoAppModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoApp(sapCLMomoAppModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoApp(sapCLMomoAppModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLMomoAppModel model = new SapCLMomoAppModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("SO_HD"))
                                        {
                                            model.setSo_hd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KH"))
                                        {
                                            model.setTen_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SDT"))
                                        {
                                            model.setSdt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TID"))
                                        {
                                            model.setTid(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLMomoAppModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLMomoAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoApp(sapCLMomoAppModelList);
                                }
                                sapCLMomoAppModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoApp(sapCLMomoAppModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoApp(sapCLMomoAppModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("MOMOMYFINACE"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLMomoMFModel model = new SapCLMomoMFModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                        {
                                            model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KHACH_HANG"))
                                        {
                                            model.setTen_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_DIEN_THOAI"))
                                        {
                                            model.setSo_dien_thoai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLMomoMFModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLMomoMFModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoMF(sapCLMomoMFModelList);
                                }
                                sapCLMomoMFModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoMFModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoMF(sapCLMomoMFModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoMFModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoMF(sapCLMomoMFModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLMomoMFModel model = new SapCLMomoMFModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                        {
                                            model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KHACH_HANG"))
                                        {
                                            model.setTen_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_DIEN_THOAI"))
                                        {
                                            model.setSo_dien_thoai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLMomoMFModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLMomoMFModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoMF(sapCLMomoMFModelList);
                                }
                                sapCLMomoMFModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoMFModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoMF(sapCLMomoMFModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLMomoMFModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLMomoMF(sapCLMomoMFModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("ONEPAY"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(9)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLOnepayModel model = new SapCLOnepayModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("BANK_ID"))
                                        {
                                            model.setBank_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MERCHANT_ID"))
                                        {
                                            model.setMerchant_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRAN_DATE"))
                                        {
                                            model.setTran_date(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_ID"))
                                        {
                                            model.setTransaction_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MERCHANT_TRANS_REF"))
                                        {
                                            model.setMerchant_trans_ref(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("ORDER_REF"))
                                        {
                                            model.setOrder_ref(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("AMOUNT"))
                                        {
                                            model.setAmount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_STATE"))
                                        {
                                            model.setTransaction_state(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLOnepayModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLOnepayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLOnepay(sapCLOnepayModelList);
                                }
                                sapCLOnepayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLOnepayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLOnepay(sapCLOnepayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLOnepayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLOnepay(sapCLOnepayModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(9)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLOnepayModel model = new SapCLOnepayModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("BANK_ID"))
                                        {
                                            model.setBank_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MERCHANT_ID"))
                                        {
                                            model.setMerchant_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRAN_DATE"))
                                        {
                                            model.setTran_date(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_ID"))
                                        {
                                            model.setTransaction_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MERCHANT_TRANS_REF"))
                                        {
                                            model.setMerchant_trans_ref(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("ORDER_REF"))
                                        {
                                            model.setOrder_ref(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("AMOUNT"))
                                        {
                                            model.setAmount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_STATE"))
                                        {
                                            model.setTransaction_state(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLOnepayModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLOnepayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLOnepay(sapCLOnepayModelList);
                                }
                                sapCLOnepayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLOnepayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLOnepay(sapCLOnepayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLOnepayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLOnepay(sapCLOnepayModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("PAYOO"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLPayooModel model = new SapCLPayooModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("THOI_DIEM_THANH_TOAN"))
                                        {
                                            model.setThoi_diem_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                        {
                                            model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_KH"))
                                        {
                                            model.setMa_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            try
                                            {
                                                model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            catch (Exception ex)
                                            {
                                                Matcher matchKey = Pattern.compile("<x:v>(.*)<").matcher(row.toString());
                                                while (matchKey.find(304))
                                                {
                                                    model.setSo_tien(matchKey.group(1));
                                                    break;
                                                }
                                            }
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("KY_THANH_TOAN"))
                                        {
                                            model.setKy_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HO_TEN"))
                                        {
                                            model.setHo_ten(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("DIA_CHI"))
                                        {
                                            model.setDia_chi(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLPayooModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLPayooModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLPayoo(sapCLPayooModelList);
                                }
                                sapCLPayooModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLPayooModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLPayoo(sapCLPayooModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLPayooModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLPayoo(sapCLPayooModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLPayooModel model = new SapCLPayooModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("THOI_DIEM_THANH_TOAN"))
                                        {
                                            model.setThoi_diem_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                        {
                                            model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_KH"))
                                        {
                                            model.setMa_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            try
                                            {
                                                model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            catch (Exception ex)
                                            {
                                                Matcher matchKey = Pattern.compile("<x:v>(.*)<").matcher(row.toString());
                                                while (matchKey.find(304))
                                                {
                                                    model.setSo_tien(matchKey.group(1));
                                                    break;
                                                }
                                            }
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("KY_THANH_TOAN"))
                                        {
                                            model.setKy_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HO_TEN"))
                                        {
                                            model.setHo_ten(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("DIA_CHI"))
                                        {
                                            model.setDia_chi(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLPayooModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLPayooModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLPayoo(sapCLPayooModelList);
                                }
                                sapCLPayooModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLPayooModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLPayoo(sapCLPayooModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLPayooModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLPayoo(sapCLPayooModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("SHOPEEPAYAPP"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }
                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLShopeeAppModel model = new SapCLShopeeAppModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_GIAO_DICH"))
                                        {
                                            model.setThoi_gian_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("AGREEMENT_ID_CMND"))
                                        {
                                            model.setAgreement_id_cmnd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HO_VA_TEN"))
                                        {
                                            model.setHo_va_ten(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLShopeeAppModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLShopeeAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeeApp(sapCLShopeeAppModelList);
                                }
                                sapCLShopeeAppModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeeAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeeApp(sapCLShopeeAppModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeeAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeeApp(sapCLShopeeAppModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLShopeeAppModel model = new SapCLShopeeAppModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_GIAO_DICH"))
                                        {
                                            model.setThoi_gian_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("AGREEMENT_ID_CMND"))
                                        {
                                            model.setAgreement_id_cmnd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HO_VA_TEN"))
                                        {
                                            model.setHo_va_ten(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLShopeeAppModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLShopeeAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeeApp(sapCLShopeeAppModelList);
                                }
                                sapCLShopeeAppModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeeAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeeApp(sapCLShopeeAppModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeeAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeeApp(sapCLShopeeAppModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("SHOPEEPAYCOUNTER"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(12)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLShopeePayCTModel model = new SapCLShopeePayCTModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_THAM_CHIEU"))
                                        {
                                            model.setMa_tham_chieu(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_THAM_CHIEU_TUDT"))
                                        {
                                            model.setMa_tham_chieu_tudt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("GIA_TRI_GIAO_DICH"))
                                        {
                                            model.setGia_tri_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_TAO"))
                                        {
                                            model.setThoi_gian_tao(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_CAP_NHAT"))
                                        {
                                            model.setThoi_gian_cap_nhat(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLShopeePayCTModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLShopeePayCTModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeePayCT(sapCLShopeePayCTModelList);
                                }
                                sapCLShopeePayCTModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeePayCTModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeePayCT(sapCLShopeePayCTModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeePayCTModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeePayCT(sapCLShopeePayCTModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(12)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLShopeePayCTModel model = new SapCLShopeePayCTModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_THAM_CHIEU"))
                                        {
                                            model.setMa_tham_chieu(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_THAM_CHIEU_TUDT"))
                                        {
                                            model.setMa_tham_chieu_tudt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("GIA_TRI_GIAO_DICH"))
                                        {
                                            model.setGia_tri_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_TAO"))
                                        {
                                            model.setThoi_gian_tao(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_CAP_NHAT"))
                                        {
                                            model.setThoi_gian_cap_nhat(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLShopeePayCTModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLShopeePayCTModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeePayCT(sapCLShopeePayCTModelList);
                                }
                                sapCLShopeePayCTModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeePayCTModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeePayCT(sapCLShopeePayCTModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLShopeePayCTModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLShopeePayCT(sapCLShopeePayCTModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("SMARTNET"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLSmartNetModel model = new SapCLSmartNetModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_DATE"))
                                        {
                                            model.setTransaction_date(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("REF_NO"))
                                        {
                                            model.setRef_no(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CONTRACT_ID"))
                                        {
                                            model.setContract_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_STATUS"))
                                        {
                                            model.setTransaction_status(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_AMOUNT"))
                                        {
                                            model.setTransaction_amount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CUSTOMER_NAME"))
                                        {
                                            model.setCustomer_name(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLSmartNetModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLSmartNetModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLSmartNet(sapCLSmartNetModelList);
                                }
                                sapCLSmartNetModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLSmartNetModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLSmartNet(sapCLSmartNetModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLSmartNetModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLSmartNet(sapCLSmartNetModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLSmartNetModel model = new SapCLSmartNetModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_DATE"))
                                        {
                                            model.setTransaction_date(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("REF_NO"))
                                        {
                                            model.setRef_no(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CONTRACT_ID"))
                                        {
                                            model.setContract_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_STATUS"))
                                        {
                                            model.setTransaction_status(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_AMOUNT"))
                                        {
                                            model.setTransaction_amount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CUSTOMER_NAME"))
                                        {
                                            model.setCustomer_name(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLSmartNetModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLSmartNetModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLSmartNet(sapCLSmartNetModelList);
                                }
                                sapCLSmartNetModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLSmartNetModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLSmartNet(sapCLSmartNetModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLSmartNetModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLSmartNet(sapCLSmartNetModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("VIETTELAPP"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(8)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLViettelAppModel model = new SapCLViettelAppModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH_NCCDV"))
                                        {
                                            model.setMa_giao_dich_nccdv(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_THANH_TOAN"))
                                        {
                                            model.setMa_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THONG_TIN_GD"))
                                        {
                                            model.setThong_tin_gd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLViettelAppModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            connectFTPServer();
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLViettelAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelApp(sapCLViettelAppModelList);
                                }
                                sapCLViettelAppModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViettelAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelApp(sapCLViettelAppModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViettelAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelApp(sapCLViettelAppModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(8)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLViettelAppModel model = new SapCLViettelAppModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH_NCCDV"))
                                        {
                                            model.setMa_giao_dich_nccdv(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_THANH_TOAN"))
                                        {
                                            model.setMa_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN"))
                                        {
                                            model.setSo_tien(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THONG_TIN_GD"))
                                        {
                                            model.setThong_tin_gd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLViettelAppModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLViettelAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelApp(sapCLViettelAppModelList);
                                }
                                sapCLViettelAppModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViettelAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelApp(sapCLViettelAppModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViettelAppModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelApp(sapCLViettelAppModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("VIETTELCOUNTER"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows() + 4; i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(9)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLViettelCounterModel model = new SapCLViettelCounterModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                        {
                                            model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_HOA_DON"))
                                        {
                                            model.setMa_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GD_VIETTEL"))
                                        {
                                            model.setMa_gd_viettel(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_GOC"))
                                        {
                                            model.setSo_tien_goc(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_TT"))
                                        {
                                            model.setSo_tien_tt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KH"))
                                        {
                                            model.setTen_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_DT"))
                                        {
                                            model.setSo_dt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_THE"))
                                        {
                                            model.setSo_the(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GD"))
                                        {
                                            model.setNgay_gd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLViettelCounterModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLViettelCounterModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelCounter(sapCLViettelCounterModelList);
                                }
                                sapCLViettelCounterModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() + 4 - 1)
                            {
                                if(sapCLViettelCounterModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelCounter(sapCLViettelCounterModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() + 4 - 1)
                            {
                                if(sapCLViettelCounterModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelCounter(sapCLViettelCounterModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows() + 4; i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(9)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLViettelCounterModel model = new SapCLViettelCounterModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                        {
                                            model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_HOA_DON"))
                                        {
                                            model.setMa_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GD_VIETTEL"))
                                        {
                                            model.setMa_gd_viettel(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_GOC"))
                                        {
                                            model.setSo_tien_goc(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_TT"))
                                        {
                                            model.setSo_tien_tt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KH"))
                                        {
                                            model.setTen_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_DT"))
                                        {
                                            model.setSo_dt(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_THE"))
                                        {
                                            model.setSo_the(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_GD"))
                                        {
                                            model.setNgay_gd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLViettelCounterModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLViettelCounterModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelCounter(sapCLViettelCounterModelList);
                                }
                                sapCLViettelCounterModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() + 4 - 1)
                            {
                                if(sapCLViettelCounterModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelCounter(sapCLViettelCounterModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() + 4 - 1)
                            {
                                if(sapCLViettelCounterModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViettelCounter(sapCLViettelCounterModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("VIMO"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(9)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLViMoModel model = new SapCLViMoModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("SO_HOA_DON"))
                                        {
                                            model.setSo_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_KHACH_HANG"))
                                        {
                                            model.setMa_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KHACH_HANG"))
                                        {
                                            model.setTen_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("DIA_CHI_KHACH_HANG"))
                                        {
                                            model.setDia_chi_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("KY_HOA_DON"))
                                        {
                                            model.setKy_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_THANH_TOAN"))
                                        {
                                            model.setNgay_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_HOA_DON"))
                                        {
                                            model.setSo_tien_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PHU_PHI"))
                                        {
                                            model.setPhu_phi(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_THANH_TOAN"))
                                        {
                                            model.setSo_tien_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_VIMO"))
                                        {
                                            model.setMa_vimo(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLViMoModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLViMoModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViMo(sapCLViMoModelList);
                                }
                                sapCLViMoModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViMoModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViMo(sapCLViMoModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViMoModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViMo(sapCLViMoModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(9)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLViMoModel model = new SapCLViMoModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("SO_HOA_DON"))
                                        {
                                            model.setSo_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_KHACH_HANG"))
                                        {
                                            model.setMa_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KHACH_HANG"))
                                        {
                                            model.setTen_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("DIA_CHI_KHACH_HANG"))
                                        {
                                            model.setDia_chi_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("KY_HOA_DON"))
                                        {
                                            model.setKy_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_THANH_TOAN"))
                                        {
                                            model.setNgay_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_HOA_DON"))
                                        {
                                            model.setSo_tien_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PHU_PHI"))
                                        {
                                            model.setPhu_phi(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_THANH_TOAN"))
                                        {
                                            model.setSo_tien_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_VIMO"))
                                        {
                                            model.setMa_vimo(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLViMoModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLViMoModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViMo(sapCLViMoModelList);
                                }
                                sapCLViMoModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViMoModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViMo(sapCLViMoModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLViMoModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLViMo(sapCLViMoModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("VNPAYBILLING"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLVnPayBillingModel model = new SapCLVnPayBillingModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("REFNUM"))
                                        {
                                            model.setRefnum(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PAIDDATE"))
                                        {
                                            model.setPaiddate(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CONTRACTNO"))
                                        {
                                            model.setContractno(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CUSTOMERNAME"))
                                        {
                                            model.setCustomername(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("AMOUNT"))
                                        {
                                            model.setAmount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLVnPayBillingModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLVnPayBillingModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPayBilling(sapCLVnPayBillingModelList);
                                }
                                sapCLVnPayBillingModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVnPayBillingModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPayBilling(sapCLVnPayBillingModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVnPayBillingModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPayBilling(sapCLVnPayBillingModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(4)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLVnPayBillingModel model = new SapCLVnPayBillingModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("REFNUM"))
                                        {
                                            model.setRefnum(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PAIDDATE"))
                                        {
                                            model.setPaiddate(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CONTRACTNO"))
                                        {
                                            model.setContractno(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("CUSTOMERNAME"))
                                        {
                                            model.setCustomername(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("AMOUNT"))
                                        {
                                            model.setAmount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLVnPayBillingModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLVnPayBillingModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPayBilling(sapCLVnPayBillingModelList);
                                }
                                sapCLVnPayBillingModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVnPayBillingModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPayBilling(sapCLVnPayBillingModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVnPayBillingModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPayBilling(sapCLVnPayBillingModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("VNPAYGATEWAY"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(1);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(list.size() > 0)
                            {
                                stt++;
                                SapCLVNPayGWModel model = new SapCLVNPayGWModel();
                                for(SapCLCollectorConfigModel obj : list)
                                {
                                    if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_GD"))
                                    {
                                        model.setThoi_gian_gd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                    {
                                        model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("SO_HOA_DON"))
                                    {
                                        model.setSo_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("SO_HOP_DONG"))
                                    {
                                        model.setSo_hop_dong(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                    {
                                        model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("SO_DIEN_THOAI"))
                                    {
                                        model.setSo_dien_thoai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("TEN_KHACH_HANG"))
                                    {
                                        model.setTen_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("SO_TAI_KHOAN"))
                                    {
                                        model.setSo_tai_khoan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("THONG_TIN_DAT_HANG"))
                                    {
                                        model.setThong_tin_dat_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("NGAN_HANG"))
                                    {
                                        try
                                        {
                                            model.setNgan_hang(worksheet.getRow(i).getCell(obj.getIndex_num()).getStringCellValue().toUpperCase());
                                        }
                                        catch (Exception e)
                                        {
                                            model.setNgan_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("LOAI_THE"))
                                    {
                                        try
                                        {
                                            model.setLoai_the(worksheet.getRow(i).getCell(obj.getIndex_num()).getStringCellValue().toUpperCase());
                                        }
                                        catch (Exception e)
                                        {
                                            model.setLoai_the(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                    {
                                        model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_DOI_SOAT"))
                                    {
                                        model.setThoi_gian_doi_soat(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("GHI_CHU"))
                                    {
                                        model.setGhi_chu(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("DIEM_THU"))
                                    {
                                        model.setDiem_thu(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                    else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_TRUOC_KM"))
                                    {
                                        model.setSo_tien_truoc_km(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                    }
                                }
                                model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                sapCLVNPayGWModelList.add(model);
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLVNPayGWModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPayGW(sapCLVNPayGWModelList);
                                }
                                sapCLVNPayGWModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVNPayGWModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPayGW(sapCLVNPayGWModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVNPayGWModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPayGW(sapCLVNPayGWModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(1);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(16)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLVNPayGWModel model = new SapCLVNPayGWModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_GD"))
                                        {
                                            model.setThoi_gian_gd(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH"))
                                        {
                                            model.setMa_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_HOA_DON"))
                                        {
                                            model.setSo_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_HOP_DONG"))
                                        {
                                            model.setSo_hop_dong(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_DON_HANG"))
                                        {
                                            model.setMa_don_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_DIEN_THOAI"))
                                        {
                                            model.setSo_dien_thoai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KHACH_HANG"))
                                        {
                                            model.setTen_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TAI_KHOAN"))
                                        {
                                            model.setSo_tai_khoan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THONG_TIN_DAT_HANG"))
                                        {
                                            model.setThong_tin_dat_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAN_HANG"))
                                        {
                                            try
                                            {
                                                model.setNgan_hang(worksheet.getRow(i).getCell(obj.getIndex_num()).getStringCellValue().toUpperCase());
                                            }
                                            catch (Exception e)
                                            {
                                                model.setNgan_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("LOAI_THE"))
                                        {
                                            try
                                            {
                                                model.setLoai_the(worksheet.getRow(i).getCell(obj.getIndex_num()).getStringCellValue().toUpperCase());
                                            }
                                            catch (Exception e)
                                            {
                                                model.setLoai_the(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                        {
                                            model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("THOI_GIAN_DOI_SOAT"))
                                        {
                                            model.setThoi_gian_doi_soat(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("GHI_CHU"))
                                        {
                                            model.setGhi_chu(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("DIEM_THU"))
                                        {
                                            model.setDiem_thu(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_TRUOC_KM"))
                                        {
                                            model.setSo_tien_truoc_km(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLVNPayGWModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLVNPayGWModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPayGW(sapCLVNPayGWModelList);
                                }
                                sapCLVNPayGWModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVNPayGWModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPayGW(sapCLVNPayGWModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVNPayGWModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPayGW(sapCLVNPayGWModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("VNPOST"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(6)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLVnPostModel model = new SapCLVnPostModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_DATE"))
                                        {
                                            model.setTransaction_date(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_TIME"))
                                        {
                                            model.setTransaction_time(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_REF_NO"))
                                        {
                                            model.setTransaction_ref_no(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PO_CODE"))
                                        {
                                            model.setPo_code(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PAYMENT_MODE"))
                                        {
                                            model.setPayment_mode(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PAYMENT_AMOUNT"))
                                        {
                                            model.setPayment_amount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PAYER_ID"))
                                        {
                                            model.setPayer_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PAYER_NAME"))
                                        {
                                            model.setPayer_name(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("PAYER_ADDRESS"))
                                        {
                                            model.setPayer_address(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLVnPostModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLVnPostModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                }
                                sapCLVnPostModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVnPostModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVnPostModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    try
                    {
                        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                        HSSFSheet worksheet = workbook.getSheetAt(0);
                        int stt = 1;
                        int multiInsert = 1000;
                        for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                        {
                            List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                            HSSFRow row = worksheet.getRow(i);
                            if(row != null)
                            {
                                if(formatter.formatCellValue(row.getCell(6)).trim().length() != 0)
                                {
                                    if(list.size() > 0)
                                    {
                                        stt++;
                                        SapCLVnPostModel model = new SapCLVnPostModel();
                                        for(SapCLCollectorConfigModel obj : list)
                                        {
                                            if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_DATE"))
                                            {
                                                model.setTransaction_date(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_TIME"))
                                            {
                                                model.setTransaction_time(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_REF_NO"))
                                            {
                                                model.setTransaction_ref_no(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PO_CODE"))
                                            {
                                                model.setPo_code(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYMENT_MODE"))
                                            {
                                                model.setPayment_mode(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYMENT_AMOUNT"))
                                            {
                                                model.setPayment_amount(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYER_ID"))
                                            {
                                                model.setPayer_id(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYER_NAME"))
                                            {
                                                model.setPayer_name(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYER_ADDRESS"))
                                            {
                                                model.setPayer_address(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                        }
                                        model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                        sapCLVnPostModelList.add(model);
                                    }
                                }
                            }
                            if(worksheet.getPhysicalNumberOfRows() > 1000)
                            {
                                if(parallel.size() > 0 && stt == multiInsert)
                                {
                                    if(sapCLVnPostModelList.size() > 0)
                                    {
                                        _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                    }
                                    sapCLVnPostModelList = new ArrayList<>();
                                    if(parallel.size() > 1000)
                                    {
                                        multiInsert += 1000;
                                    }
                                    else
                                    {
                                        multiInsert += parallel.size();
                                    }
                                }
                                if(i == worksheet.getPhysicalNumberOfRows() -1)
                                {
                                    if(sapCLVnPostModelList.size() > 0)
                                    {
                                        _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                    }
                                }
                            }
                            else
                            {
                                if(i == worksheet.getPhysicalNumberOfRows() -1)
                                {
                                    if(sapCLVnPostModelList.size() > 0)
                                    {
                                        _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception ex)
                    {
//                        if(ex.getMessage().equals("Found EOFRecord before WindowTwoRecord was encountered"))
//                        {
//                        }
                        String url = String.format("ftp://%s:%s@%s%s", _mainUsername, _mainPassword, _mainServer, ftpFilePath);
                        inputStream = new BufferedInputStream(new URL(url).openStream());
                        Workbook workbook = Workbook.getWorkbook(inputStream);
                        Sheet worksheet = workbook.getSheet(0);
                        int stt = 1;
                        int multiInsert = 1000;
                        Cell[] row;
                        for(int i = index; i < worksheet.getRows(); i++)
                        {
                            List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getRows()).boxed().collect(Collectors.toList());
                            row = worksheet.getRow(i);
                            if(row != null)
                            {
                                if(row[6].getContents().length() != 0)
                                {
                                    if(list.size() > 0)
                                    {
                                        SapCLVnPostModel model = new SapCLVnPostModel();
                                        for(SapCLCollectorConfigModel obj : list)
                                        {
                                            if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_DATE"))
                                            {
                                                model.setTransaction_date(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_TIME"))
                                            {
                                                model.setTransaction_time(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("TRANSACTION_REF_NO"))
                                            {
                                                model.setTransaction_ref_no(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PO_CODE"))
                                            {
                                                model.setPo_code(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYMENT_MODE"))
                                            {
                                                model.setPayment_mode(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYMENT_AMOUNT"))
                                            {
                                                model.setPayment_amount(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYER_ID"))
                                            {
                                                model.setPayer_id(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYER_NAME"))
                                            {
                                                model.setPayer_name(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("PAYER_ADDRESS"))
                                            {
                                                model.setPayer_address(row[obj.getIndex_num()].getContents().toUpperCase());
                                            }
                                        }
                                        model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                        sapCLVnPostModelList.add(model);
                                    }
                                }
                            }
                            if(worksheet.getRows() > 1000)
                            {
                                if(parallel.size() > 0 && stt == multiInsert)
                                {
                                    if(sapCLVnPostModelList.size() > 0)
                                    {
                                        _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                    }
                                    sapCLVnPostModelList = new ArrayList<>();
                                    if(parallel.size() > 1000)
                                    {
                                        multiInsert += 1000;
                                    }
                                    else
                                    {
                                        multiInsert += parallel.size();
                                    }
                                }
                                if(i == worksheet.getRows() -1)
                                {
                                    if(sapCLVnPostModelList.size() > 0)
                                    {
                                        _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                    }
                                }
                            }
                            else
                            {
                                if(i == worksheet.getRows() -1)
                                {
                                    if(sapCLVnPostModelList.size() > 0)
                                    {
                                        _forwardDB.insSapCLVnPost(sapCLVnPostModelList);
                                    }
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("VNPTPAY"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(1);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows() + 3; i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(6)).trim().length() != 0)
                            {
                                if(row.getCell(26).toString().length() <= 30)
                                {
                                    if(list.size() > 0)
                                    {
                                        stt++;
                                        SapCLVNPTPayModel model = new SapCLVNPTPayModel();
                                        for(SapCLCollectorConfigModel obj : list)
                                        {
                                            if(obj.getColumn_name().toUpperCase().equals("MAGD_VNPT_PAY"))
                                            {
                                                model.setMagd_vnpt_pay(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("GIA_TRI_HOA_DON"))
                                            {
                                                model.setGia_tri_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                            {
                                                model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                            {
                                                model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("MA_KHACH_HANG"))
                                            {
                                                model.setMa_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH_MERCHANT"))
                                            {
                                                model.setMa_giao_dich_merchant(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH_PARTNER"))
                                            {
                                                model.setMa_giao_dich_partner(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                        }
                                        model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                        sapCLVNPTPayModelList.add(model);
                                    }
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLVNPTPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPTPay(sapCLVNPTPayModelList);
                                }
                                sapCLVNPTPayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVNPTPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPTPay(sapCLVNPTPayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() + 3 - 1)
                            {
                                if(sapCLVNPTPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPTPay(sapCLVNPTPayModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(1);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows() + 3; i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(6)).trim().length() != 0)
                            {
                                if(row.getCell(26).toString().length() <= 30)
                                {
                                    if(list.size() > 0)
                                    {
                                        stt++;
                                        SapCLVNPTPayModel model = new SapCLVNPTPayModel();
                                        for(SapCLCollectorConfigModel obj : list)
                                        {
                                            if(obj.getColumn_name().toUpperCase().equals("MAGD_VNPT_PAY"))
                                            {
                                                model.setMagd_vnpt_pay(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("GIA_TRI_HOA_DON"))
                                            {
                                                model.setGia_tri_hoa_don(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                            {
                                                model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI"))
                                            {
                                                model.setTrang_thai(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("MA_KHACH_HANG"))
                                            {
                                                model.setMa_khach_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH_MERCHANT"))
                                            {
                                                model.setMa_giao_dich_merchant(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                            else if(obj.getColumn_name().toUpperCase().equals("MA_GIAO_DICH_PARTNER"))
                                            {
                                                model.setMa_giao_dich_partner(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                            }
                                        }
                                        model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                        sapCLVNPTPayModelList.add(model);
                                    }
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLVNPTPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPTPay(sapCLVNPTPayModelList);
                                }
                                sapCLVNPTPayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() + 3 - 1)
                            {
                                if(sapCLVNPTPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPTPay(sapCLVNPTPayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLVNPTPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLVNPTPay(sapCLVNPTPayModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
            else if(ftpFilePath.split("/")[2].split("_")[0].equals("ZALOPAY"))
            {
                list = _forwardDB.getSapCollectorConfig(ftpFilePath.split("/")[2].split("_")[0]);
                int index = 0;
                if(list.size() > 0)
                {
                    index = list.get(0).getRow_num();
                }

                if(ftpFilePath.split("\\.")[1].equals("xlsx"))
                {
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet worksheet = workbook.getSheetAt(0);
                    worksheet.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        XSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(1)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLZaloPayModel model = new SapCLZaloPayModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GD_MERCHANT"))
                                        {
                                            model.setMa_gd_merchant(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GD_ZALOPAY"))
                                        {
                                            model.setMa_gd_zalopay(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_KH"))
                                        {
                                            model.setMa_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KH"))
                                        {
                                            model.setTen_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_APP"))
                                        {
                                            model.setTen_app(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("KENH_THANH_TOAN"))
                                        {
                                            model.setKenh_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HINH_THUC_THANH_TOAN"))
                                        {
                                            model.setHinh_thuc_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI_GIAO_DICH"))
                                        {
                                            model.setTrang_thai_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MO_TA"))
                                        {
                                            model.setMo_ta(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_NGAN_HANG"))
                                        {
                                            model.setMa_ngan_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("BANK_TRACE_NO"))
                                        {
                                            model.setBank_trace_no(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_GIAO_DICH"))
                                        {
                                            model.setSo_tien_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_XULY_HOANTRA"))
                                        {
                                            model.setNgay_xuly_hoantra(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_HOANTRA"))
                                        {
                                            model.setSo_tien_hoantra(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLZaloPayModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            connectFTPServer();
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLZaloPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLZaloPay(sapCLZaloPayModelList);
                                }
                                sapCLZaloPayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLZaloPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLZaloPay(sapCLZaloPayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLZaloPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLZaloPay(sapCLZaloPayModelList);
                                }
                            }
                        }
                    }
                }
                else if(ftpFilePath.split("\\.")[1].equals("xls"))
                {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    int stt = 1;
                    int multiInsert = 1000;
                    for(int i = index; i < worksheet.getPhysicalNumberOfRows(); i++)
                    {
                        List<Long> parallel = LongStream.rangeClosed(multiInsert, worksheet.getPhysicalNumberOfRows()).boxed().collect(Collectors.toList());
                        HSSFRow row = worksheet.getRow(i);
                        if(row != null)
                        {
                            if(formatter.formatCellValue(row.getCell(1)).trim().length() != 0)
                            {
                                if(list.size() > 0)
                                {
                                    stt++;
                                    SapCLZaloPayModel model = new SapCLZaloPayModel();
                                    for(SapCLCollectorConfigModel obj : list)
                                    {
                                        if(obj.getColumn_name().toUpperCase().equals("NGAY_GIAO_DICH"))
                                        {
                                            model.setNgay_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GD_MERCHANT"))
                                        {
                                            model.setMa_gd_merchant(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_GD_ZALOPAY"))
                                        {
                                            model.setMa_gd_zalopay(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_KH"))
                                        {
                                            model.setMa_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_KH"))
                                        {
                                            model.setTen_kh(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TEN_APP"))
                                        {
                                            model.setTen_app(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("KENH_THANH_TOAN"))
                                        {
                                            model.setKenh_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("HINH_THUC_THANH_TOAN"))
                                        {
                                            model.setHinh_thuc_thanh_toan(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("TRANG_THAI_GIAO_DICH"))
                                        {
                                            model.setTrang_thai_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MO_TA"))
                                        {
                                            model.setMo_ta(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("MA_NGAN_HANG"))
                                        {
                                            model.setMa_ngan_hang(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("BANK_TRACE_NO"))
                                        {
                                            model.setBank_trace_no(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_GIAO_DICH"))
                                        {
                                            model.setSo_tien_giao_dich(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("NGAY_XULY_HOANTRA"))
                                        {
                                            model.setNgay_xuly_hoantra(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                        else if(obj.getColumn_name().toUpperCase().equals("SO_TIEN_HOANTRA"))
                                        {
                                            model.setSo_tien_hoantra(formatter.formatCellValue(row.getCell(obj.getIndex_num())).toUpperCase());
                                        }
                                    }
                                    model.setFile_name(ftpFilePath.split("/")[2].trim().toUpperCase());
                                    sapCLZaloPayModelList.add(model);
                                }
                            }
                        }
                        if(worksheet.getPhysicalNumberOfRows() > 1000)
                        {
                            if(parallel.size() > 0 && stt == multiInsert)
                            {
                                if(sapCLZaloPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLZaloPay(sapCLZaloPayModelList);
                                }
                                sapCLZaloPayModelList = new ArrayList<>();
                                if(parallel.size() > 1000)
                                {
                                    multiInsert += 1000;
                                }
                                else
                                {
                                    multiInsert += parallel.size();
                                }
                            }
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLZaloPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLZaloPay(sapCLZaloPayModelList);
                                }
                            }
                        }
                        else
                        {
                            if(i == worksheet.getPhysicalNumberOfRows() -1)
                            {
                                if(sapCLZaloPayModelList.size() > 0)
                                {
                                    _forwardDB.insSapCLZaloPay(sapCLZaloPayModelList);
                                }
                            }
                        }
                    }
                }
                inputStream.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return String.format("File error: %s | %s", ftpFilePath.split("/")[2].trim(), ex.getMessage());
        }
        finally
        {
            try
            {
                if(inputStream != null)
                {
                    inputStream.close();
                    disconnectFTPServer();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                return String.format("File error: %s | %s", ftpFilePath.split("/")[2].trim(), ex.getMessage());
            }
        }
        return "SUCCESS";
    }

    public boolean copyFile_collectorTransaction(String path){
        try {
            connectFTPServer();
            return listFoldercollectorTransaction(_ftpClient, path, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean listFoldercollectorTransaction(FTPClient ftpClient, String remotePath, String newPath) throws IOException
    {
        boolean success = true;
        try
        {
            if(remotePath.split("/")[2].length() > 0)
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                newPath = String.format("/%s/%s", remotePath.split("/")[1] + "_backup", LocalDateTime.now().format(dtf));
                if(makeDirectory(ftpClient, newPath))
                {
                    System.out.println(String.format("%s Folder created", LocalDateTime.now().format(_dtf)));
                }
            }

        }
        catch (Exception ex)
        {
        }
        FTPFile[] remoteFiles = ftpClient.listFiles(remotePath);
        for (FTPFile remoteFile : remoteFiles)
        {
            if (!remoteFile.getName().equals(".") && !remoteFile.getName().equals(".."))
            {
                String remoteFilePath = remotePath;
                newPath = newPath + "/" + remoteFile.getName();
                if(newPath.contains("/"))
                {
                    System.out.println(newPath);
                    success = downloadFTPFile(remoteFilePath, newPath);
                }
            }
        }
        disconnectFTPServer();
        return success;
    }

    @Override
    public ResponseObject bankStatement(BankStatementModel model) {
        try {
            _ftpMapper.bankStatement(model);
            System.out.println(String.format("%s %s", LocalDateTime.now().format(_dtf), CommonStrings.INSERT_SUCCESS));
            return ResponseObject.INSERT_DATA_SUCCESS;
        }
        catch (Exception e) {
            System.out.println(String.format("%s %s", LocalDateTime.now().format(_dtf), CommonStrings.INSERT_FAIL));
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }

    @Override
    public ResponseObject procSyncColl(String collectorName){
        ResponseObject obj = new ResponseObject();
        try
        {
            Map<String, Object> params = new HashMap<>();
            params.put("P_COLLECTOR_NAME", collectorName);
            _ftpMapper.procSyncColl(params);
            if(params.get("P_RESNUM").equals("0"))
            {
                obj.setSuccess(false);
            }
            obj.setData(params.get("P_RESNUM").toString());
            obj.setMessage(params.get("P_RESULT").toString());

        }
        catch (Exception e) {
            String log = String.format("[%s] Error when procSyncColl.", e.getMessage());
            System.out.println(log);
            obj = new ResponseObject(false, CommonStrings.RESP_MSG_SERVER_ERROR, false);
        }
        return obj;
    }

    @Override
    public ResponseObject procDelColl(String collectorName, String fileName){
        ResponseObject obj = new ResponseObject();
        try
        {
            Map<String, Object> params = new HashMap<>();
            params.put("P_COLLECTOR_NAME", collectorName);
            params.put("P_FILE_NAME", fileName);
            _ftpMapper.procDelColl(params);
            if(params.get("P_RES").equals("0"))
            {
                obj.setSuccess(false);
                obj.setMessage("FAIl");
            }
            obj.setData(params.get("P_RES").toString());
            obj.setMessage("SUCCESS");

        }
        catch (Exception e) {
            String log = String.format("[%s] Error when procDelColl.", e.getMessage());
            System.out.println(log);
            obj = new ResponseObject(false, CommonStrings.RESP_MSG_SERVER_ERROR, false);
        }
        return obj;
    }

    private String numberFormatted(String value){
        String result = null;
        try
        {
            if(value != null && value.length() > 1)
            {
                if(value.contains(".00"))
                {
                    value = value.split("\\.")[0];
                }
                if(value.contains("."))
                {
                    try
                    {
                        if(value.split("\\.")[1].length() == 2)
                        {
                            value = value.split("\\.")[0];
                        }
                        else
                        {
                            value = value.replace(".", "");
                        }
                    }
                    catch (Exception e)
                    {
                        return e.getMessage();
                    }
                }
                if(value.contains(","))
                {
                    value = value.replace(",", "");
                }
                if(value.contains(":"))
                {
                    value = value.replace(": ", "");
                }
                if(value.contains("-"))
                {
                    value = value.replace("- 0", "0");
                }
                result = String.format("%,d", Long.parseLong(value));
            }
            else
            {
                return value;
            }
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
        return result;
    }
}
