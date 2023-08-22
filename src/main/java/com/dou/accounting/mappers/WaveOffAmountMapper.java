package com.dou.accounting.mappers;

import com.dou.accounting.models.WaveOffAmountModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WaveOffAmountMapper {

    List<WaveOffAmountModel> getWaveOffAmountData(WaveOffAmountModel waveOffAmountModel);

    int insertDataToWaveOff(WaveOffAmountModel waveOffAmountModel);

    int updateWaveOffAmount(WaveOffAmountModel waveOffAmountModel);

    int deleteAllData();

}
