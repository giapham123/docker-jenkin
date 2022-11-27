package com.dou.accounting.services;

import com.dou.accounting.models.RejectUploadFileModel;
import com.dou.accounting.models.UploadFileModel;

import java.util.List;
import java.util.Map;

public interface RejectUploadFileInterface {

    Map getRejectUploadFileData(RejectUploadFileModel rejectUploadFileModel);

    byte[] exportRejectUploadFile(RejectUploadFileModel rejectUploadFileModel);

}
