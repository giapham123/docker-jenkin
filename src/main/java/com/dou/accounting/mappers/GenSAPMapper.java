package com.dou.accounting.mappers;

import com.dou.accounting.models.GenSAPModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GenSAPMapper {

    List<GenSAPModel> getGenSAPData(GenSAPModel model);

    String authorGenSap(Map<String,Object> param);

}
