package com.dou.accounting.mappers;

import com.dou.accounting.models.OutNetReportModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OutNetReportMapper {

    List<OutNetReportModel> getOutNetReportData(OutNetReportModel outNetReportModel);
}
