package com.dou.accounting.services;

import com.dou.accounting.models.PendingDisbursementModel;
import com.dou.adm.shared.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface PendingDisburementInterface {

    ResponseObject uploadFile(MultipartFile file, String userLogin);

    ResponseObject insertSigAgre(String agreementId, String userLogin);

    ResponseObject deleteAgreementId(List<PendingDisbursementModel> model);

    ResponseObject getDataPendingDisbursement(String agreementId);

    ResponseObject processPending(String usrLg) throws ParseException;

    ResponseObject getProcess();

}
