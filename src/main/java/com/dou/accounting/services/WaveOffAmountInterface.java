package com.dou.accounting.services;

import com.dou.accounting.models.WaveOffAmountModel;
import com.dou.adm.shared.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WaveOffAmountInterface {
    List<WaveOffAmountModel> getWaveOffAmountData(WaveOffAmountModel waveOffAmountModel);

    ResponseObject uploadFileWaveOffAmount(MultipartFile excelFile);

    int updateWaveOffAmount(WaveOffAmountModel waveOffAmountModel);

    boolean uploadStatus();
}
