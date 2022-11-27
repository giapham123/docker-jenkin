package com.dou.bankstatement.services;

import com.dou.adm.shared.ResponseObject;
import com.dou.bankstatement.models.*;

import java.util.List;

public interface FTPServiceInterface {
    List<String> listingFTP(String folder);
    boolean connectFTPServer();
    boolean disconnectFTPServer();
    boolean DeleteFTP(String path);
    String excelFTP(String ftpFilePath);
    boolean CopyFileFTP(String path);
    ResponseObject bankStatement(BankStatementModel model);
}
