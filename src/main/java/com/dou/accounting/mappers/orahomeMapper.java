package com.dou.accounting.mappers;

import com.dou.accounting.models.CloseSoldoutModel;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class orahomeMapper {

    @Autowired
    CloseSoldoutMapper closeSoldoutMapper;

    @TargetDataSource(CommonStrings.DATA_SOURCE_DEFAULT)
    public int insertInCloseSoldoutHis(CloseSoldoutModel closeSoldoutModel){
        return closeSoldoutMapper.insertInCloseSoldoutHis(closeSoldoutModel);
    }
}
