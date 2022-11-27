package com.dou.bankstatement.services.impl;

import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.mappers.FTPMapper;
import com.dou.bankstatement.mappers.ForwardDB;
import com.dou.bankstatement.models.BankStatementModel;
import com.dou.bankstatement.services.FTPServiceInterface;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
                            if(formatter.formatCellValue(row.getCell(13)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(13));
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
                            if(formatter.formatCellValue(row.getCell(5)).trim().length() != 0)
                            {
                                cell = formatter.formatCellValue(row.getCell(5));
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
                            if(formatter.formatCellValue(row.getCell(7)).trim().length() != 0)
                            {
                                textCell = formatter.formatCellValue(row.getCell(5));
                                if(textCell.contains("Closing balance"))
                                {
                                    cell = formatter.formatCellValue(row.getCell(7));
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
                                            model.setSapAcct("131155");
                                            model.setAcct("19125812410073");
                                        }
                                        else if(substring.contains("036"))
                                        {
                                            model.setSapAcct("131143");
                                            model.setAcct("19125812410036");
                                        }
                                        else if(substring.contains("014"))
                                        {
                                            model.setSapAcct("132106");
                                            model.setAcct("19125812410014");
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
