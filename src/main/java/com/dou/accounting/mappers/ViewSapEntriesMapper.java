package com.dou.accounting.mappers;

import com.dou.accounting.models.ViewSapEntriesModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ViewSapEntriesMapper {

    List<ViewSapEntriesModel> getDataViewSapEntries(ViewSapEntriesModel model);

    String pushSap(Map<String,Object> param);

}
