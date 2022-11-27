package com.dou.accounting.services;

import com.dou.accounting.models.AccountingModel;
import com.dou.accounting.models.UploadFileModel;

import java.util.List;
import java.util.Map;

public interface UploadFileInterface {

    List<UploadFileModel> getUploadFileData(UploadFileModel uploadFileModel);

    byte[] exportUploadFile(UploadFileModel uploadFileModel);

    int totalPage(UploadFileModel uploadFileModel);
}
