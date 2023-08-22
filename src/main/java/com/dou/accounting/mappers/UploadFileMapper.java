package com.dou.accounting.mappers;

import com.dou.accounting.models.UploadFileModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UploadFileMapper {

    List<UploadFileModel> getUploadFileData(UploadFileModel  uploadFileModel);

    int countAllDataInUpload(UploadFileModel uploadFileModel);

    List<UploadFileModel> exportUploadFile(UploadFileModel  uploadFileModel);

}
