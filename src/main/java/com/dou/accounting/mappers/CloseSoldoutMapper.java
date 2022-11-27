package com.dou.accounting.mappers;

import com.dou.accounting.models.AccountingHisModel;
import com.dou.accounting.models.CloseSoldoutModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CloseSoldoutMapper {

    List<CloseSoldoutModel> loadingDetailsData(List<CloseSoldoutModel> closeSoldoutModel);

    int isSoldout(@Param("agreementId") String agreementId);

    int updateAppId(CloseSoldoutModel closeSoldoutModel);

    int insertInCloseSoldoutHis(CloseSoldoutModel closeSoldoutModel);

    int isExistApp(@Param("agreementId") String agreementId);
}
