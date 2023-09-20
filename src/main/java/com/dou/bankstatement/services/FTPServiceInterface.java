package com.dou.bankstatement.services;

import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.models.BankStatementModel;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FTPServiceInterface {
    List<String> listingFTP(String folder);
    boolean connectFTPServer();
    boolean disconnectFTPServer();
    boolean DeleteFTP(String path);
    String excelFTP(String ftpFilePath);
    boolean CopyFileFTP(String path);
    boolean copyFile_collectorTransaction(String path);
    ResponseObject bankStatement(BankStatementModel model);
    String collectorTransaction(String ftpFilePath) throws IOException, BiffException, URISyntaxException;
    ResponseObject procSyncColl(String collectorName);
    ResponseObject procDelColl(String collectorName, String fileName);
}
