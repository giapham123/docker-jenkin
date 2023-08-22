package com.dou.accounting.mappers;

import com.dou.accounting.models.CollectorCompareModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollectorCompareMapper {


    String collectorCompare(Map<String,Object> param);

    List<CollectorCompareModel> getCollectorCompare(CollectorCompareModel model);

}
