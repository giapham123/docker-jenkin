package com.dou.accounting.mappers;

import com.dou.accounting.models.PendingDisbursementModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PendingDisbursementMapper {

    int insertDataPendingDisbur(@Param("agreementId") String agreementId, @Param("userLogin") String userLogin);

    int isExistAgreementId(@Param("agreementId") String agreementId);

    int deleteAgreementId(@Param("agreementId") String agreementId);

    List<PendingDisbursementModel> getDataPendingDisbursement(@Param("agreementId") String agreementId);

    String processPending(Map<String,Object> param);
}
