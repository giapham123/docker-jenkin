package com.dou.accounting.mappers;

import com.dou.accounting.models.RejectUploadFileGLSAPModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RejectUploadFileGLSAPMapper {

    List<RejectUploadFileGLSAPModel> getRejectUploadFileGLSAPData(RejectUploadFileGLSAPModel rejectUploadFileGLSAPModel);
}
