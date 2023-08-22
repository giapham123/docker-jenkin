package com.dou.accounting.mappers;

import com.dou.accounting.models.CheckTerminationDailyRPTModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CheckTerminationDailyRPTMapper {

    List<CheckTerminationDailyRPTModel> getTerminationDailyReport(CheckTerminationDailyRPTModel checkTerminationDailyRPTModel);
}
