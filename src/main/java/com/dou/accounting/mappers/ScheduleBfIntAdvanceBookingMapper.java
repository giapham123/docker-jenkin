package com.dou.accounting.mappers;

import com.dou.accounting.models.ScheduleBfIntAdvanceBookingModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleBfIntAdvanceBookingMapper {

    List<ScheduleBfIntAdvanceBookingModel> getReduceInterest(ScheduleBfIntAdvanceBookingModel scheduleBfReduceInterestModel);

    List<ScheduleBfIntAdvanceBookingModel> getRepayment(ScheduleBfIntAdvanceBookingModel scheduleBfReduceInterestModel);

    int countAllDataRepayment(ScheduleBfIntAdvanceBookingModel scheduleBfReduceInterestModel);

    List<ScheduleBfIntAdvanceBookingModel> getAllRepayment(ScheduleBfIntAdvanceBookingModel scheduleBfReduceInterestModel);


}
