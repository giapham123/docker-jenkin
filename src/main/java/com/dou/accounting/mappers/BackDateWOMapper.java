package com.dou.accounting.mappers;

import com.dou.accounting.models.BackDateWOModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface BackDateWOMapper {


    List<BackDateWOModel> getDataBackDate(@Param("backDate") Date backDate, @Param("type") String type);

    String processPendingBackDate(Map<String,Object> param);
}
