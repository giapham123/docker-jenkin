package com.dou.accounting.mappers;

import com.dou.accounting.models.ScheduleBfReduceInterestModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleBfReduceInterestMapper {

    List<ScheduleBfReduceInterestModel> getReduceInterest(ScheduleBfReduceInterestModel scheduleBfReduceInterestModel);

    List<ScheduleBfReduceInterestModel> getRepayment(ScheduleBfReduceInterestModel scheduleBfReduceInterestModel);

    int countAllDataRepayment(ScheduleBfReduceInterestModel scheduleBfReduceInterestModel);

    List<ScheduleBfReduceInterestModel> getAllRepayment(ScheduleBfReduceInterestModel scheduleBfReduceInterestModel);


}
