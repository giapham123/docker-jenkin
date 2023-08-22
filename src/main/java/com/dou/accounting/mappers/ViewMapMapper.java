package com.dou.accounting.mappers;

import com.dou.accounting.models.ViewMapModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ViewMapMapper {

    List<ViewMapModel> getDataViewMap(ViewMapModel model);

}
