package com.dou.accounting.services;

import com.dou.accounting.models.UploadBankStatementModel;
import com.dou.adm.shared.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

public interface UploadBankStatementInterface {
    ResponseObject insertUploadBankStatement(MultipartFile excelFile, String userLogin);

    ResponseObject getDataBankStatement(UploadBankStatementModel uploadBankStatementModel);

    byte[] exportBankStatementData(UploadBankStatementModel uploadBankStatementModel);
}
