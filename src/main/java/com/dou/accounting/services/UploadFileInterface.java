package com.dou.accounting.services;

import com.dou.accounting.models.UploadFileModel;

import java.util.List;

public interface UploadFileInterface {

    List<UploadFileModel> getUploadFileData(UploadFileModel uploadFileModel);

    byte[] exportUploadFile(UploadFileModel uploadFileModel);

    int totalPage(UploadFileModel uploadFileModel);
}
