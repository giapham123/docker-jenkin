package com.dou.accounting.mappers;

import com.dou.accounting.models.AccountingModel;
import com.dou.accounting.models.RejectUploadFileModel;
import com.dou.accounting.models.UploadFileModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RejectUploadFileMapper {

    List<RejectUploadFileModel> getRejectUploadFileData(RejectUploadFileModel rejectUploadFileModel);

    int countAllDataInRejectUpload(RejectUploadFileModel rejectUploadFileModel);

    List<RejectUploadFileModel> exportRejectUploadFile(RejectUploadFileModel rejectUploadFileModel);
}
