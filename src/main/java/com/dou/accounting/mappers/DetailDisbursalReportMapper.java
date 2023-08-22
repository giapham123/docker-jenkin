package com.dou.accounting.mappers;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DetailDisbursalReportMapper {

    String getDataDetailDisbursalReport(Map<String,Object> param);
}
