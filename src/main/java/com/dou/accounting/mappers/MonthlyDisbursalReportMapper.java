package com.dou.accounting.mappers;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MonthlyDisbursalReportMapper {

    String getDataMonthlyDisbursalReport(Map<String,Object> param);

    String getDataMonthlyDisbursalReportChart(Map<String,Object> param);
}
