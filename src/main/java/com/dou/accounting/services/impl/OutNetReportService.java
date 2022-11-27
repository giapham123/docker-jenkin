package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.OutNetReportMapper;
import com.dou.accounting.mappers.RejectUploadFileMapper;
import com.dou.accounting.models.OutNetReportModel;
import com.dou.accounting.models.RejectUploadFileModel;
import com.dou.accounting.services.OutNetReportInterface;
import com.dou.accounting.services.RejectUploadFileInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OutNetReportService implements OutNetReportInterface {

    @Autowired
    OutNetReportMapper mapper;


    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public List<OutNetReportModel> getOutNetReportData(OutNetReportModel outNetReportModel) {
         List rs= mapper.getOutNetReportData(outNetReportModel);

        return rs;
    }

}
