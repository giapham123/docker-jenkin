package com.dou.accounting.mappers;

import com.dou.accounting.models.CollectionTransModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectionTransMapper {

    List<CollectionTransModel> getCollTransData(CollectionTransModel model);

}
