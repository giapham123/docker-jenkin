package com.dou.accounting.services;

import com.dou.accounting.models.RejectUploadFileGLSAPModel;
import com.dou.accounting.models.RejectUploadFileModel;

import java.util.List;

public interface RejectUploadFileGLSAPInterface {

    List<RejectUploadFileGLSAPModel> getRejectUploadFileGLSAPData(RejectUploadFileGLSAPModel rejectUploadFileGLSAPModel);

}
