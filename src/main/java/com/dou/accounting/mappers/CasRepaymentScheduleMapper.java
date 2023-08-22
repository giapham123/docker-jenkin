
package com.dou.accounting.mappers;

import com.dou.accounting.models.CasRepaymentScheduleModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CasRepaymentScheduleMapper {

    List<CasRepaymentScheduleModel> getDataCasRepayment(CasRepaymentScheduleModel casRepaymentScheduleModel);

    String getReceivableTillDate(CasRepaymentScheduleModel casRepaymentScheduleModel);

    String getNotOverduePrincipal(CasRepaymentScheduleModel casRepaymentScheduleModel);

    String getEarlyPaymentPenalty(CasRepaymentScheduleModel casRepaymentScheduleModel);

    String getRecAmount(CasRepaymentScheduleModel casRepaymentScheduleModel);

    String getWaiveOffCovid(CasRepaymentScheduleModel casRepaymentScheduleModel);
}
