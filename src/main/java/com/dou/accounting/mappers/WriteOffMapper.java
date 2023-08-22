package com.dou.accounting.mappers;

import com.dou.accounting.models.WriteOffModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WriteOffMapper {

    List<WriteOffModel> getWriteOffData(WriteOffModel writeOffModel);

    List<WriteOffModel> getWriteOffDataForExport(WriteOffModel writeOffModel);

    int countAllDataWriteOff(WriteOffModel writeOffModel);

}
