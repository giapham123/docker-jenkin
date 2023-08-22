package com.dou.accounting.mappers;

import com.dou.accounting.models.ReturnBookingCaseModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReturnBookingCaseMapper {

    List<ReturnBookingCaseModel> getReturnBookingCase(ReturnBookingCaseModel returnBookingCaseModel);

}
