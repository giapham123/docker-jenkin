package com.dou.accounting.services;

import com.dou.accounting.models.RejectUploadFileModel;

import java.util.Map;

public interface RejectUploadFileInterface {

    Map getRejectUploadFileData(RejectUploadFileModel rejectUploadFileModel);

    byte[] exportRejectUploadFile(RejectUploadFileModel rejectUploadFileModel);

}
