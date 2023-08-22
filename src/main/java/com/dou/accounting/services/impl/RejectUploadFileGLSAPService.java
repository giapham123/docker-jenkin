package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.RejectUploadFileGLSAPMapper;
import com.dou.accounting.models.RejectUploadFileGLSAPModel;
import com.dou.accounting.services.RejectUploadFileGLSAPInterface;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RejectUploadFileGLSAPService implements RejectUploadFileGLSAPInterface {

    @Autowired
    RejectUploadFileGLSAPMapper mapper;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public List<RejectUploadFileGLSAPModel> getRejectUploadFileGLSAPData(RejectUploadFileGLSAPModel rejectUploadFileGLSAPModel) {
        return mapper.getRejectUploadFileGLSAPData(rejectUploadFileGLSAPModel);
    }
}
