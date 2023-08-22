package com.dou.accounting.mappers;

import com.dou.accounting.models.TerminationReportAppModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TerminationReportAppMapper {

    List<TerminationReportAppModel> getDataTerminationReportApp(TerminationReportAppModel terminationReportAppModel);

    int getProcTermiFinish();

    int countAllDataInTerminal(TerminationReportAppModel terminationReportAppModel);

    List<TerminationReportAppModel> exportTerminationReportAppData(TerminationReportAppModel terminationReportAppModel);
}
