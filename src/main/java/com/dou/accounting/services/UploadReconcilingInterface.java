package com.dou.accounting.services;

import com.dou.accounting.models.UploadReconcilingModel;
import com.dou.adm.shared.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadReconcilingInterface {

    ResponseObject uploadFile(MultipartFile file, String userLogin);

    ResponseObject getInitData();

    ResponseObject getDataUploadReconciling(UploadReconcilingModel model);

    ResponseObject deteleDataImportSap(List<UploadReconcilingModel> model);

}
