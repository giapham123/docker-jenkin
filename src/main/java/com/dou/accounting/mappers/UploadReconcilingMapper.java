package com.dou.accounting.mappers;

import com.dou.accounting.models.UploadReconcilingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UploadReconcilingMapper {

    int insertDataToSapTxn(UploadReconcilingModel model);

    int getMaxIdInSap();

    int getMaxIdInTMP();

    List<UploadReconcilingModel> getChannel();

    List<UploadReconcilingModel> getTransType();

    List<UploadReconcilingModel> getDataUploadReconciling(UploadReconcilingModel model);

    List<UploadReconcilingModel> getDataUploadReconcilingTmp(UploadReconcilingModel model);

    String importDataToTxnImport(Map<String,Object> param);

    int deleteInSapGL(@Param("caseId") String caseId, @Param("leaVoucherId") String leaVoucherId);

    int deleteInSapImport(@Param("agreementId") String agreementId, @Param("leaVoucherId") String leaVoucherId);

}
