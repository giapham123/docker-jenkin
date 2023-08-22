package com.dou.accounting.services;

import com.dou.accounting.models.CloseSoldoutModel;
import com.dou.adm.shared.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CloseSoldoutInterface {
    Map loadingDetailsData(MultipartFile excelFile);

    ResponseObject closeLsApp(List<CloseSoldoutModel> closeSoldoutModels);

    List<CloseSoldoutModel> getCloseSoldout(List<CloseSoldoutModel> closeSoldoutModels);

}
