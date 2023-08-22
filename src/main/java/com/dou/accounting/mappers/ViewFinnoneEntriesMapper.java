package com.dou.accounting.mappers;

import com.dou.accounting.models.ViewFinnoneEntriesModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ViewFinnoneEntriesMapper {

    List<ViewFinnoneEntriesModel> getDataViewFinnoneEntries(ViewFinnoneEntriesModel model);

    int countAllDataViewFinnoneEntries(ViewFinnoneEntriesModel model);

    List<ViewFinnoneEntriesModel> dataForExport(ViewFinnoneEntriesModel model);

}
