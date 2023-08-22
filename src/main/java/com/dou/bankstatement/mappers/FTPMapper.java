package com.dou.bankstatement.mappers;

import com.dou.bankstatement.models.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FTPMapper {
    void bankStatement(BankStatementModel model);
    void insSapCLEPay(List<SapCLEPayModel> list);
    void insSapCLMomoApp(List<SapCLMomoAppModel> list);
    void insSapCLMomoMF(List<SapCLMomoMFModel> list);
    void insSapCLOnepay(List<SapCLOnepayModel> list);
    void insSapCLPayoo(List<SapCLPayooModel> list);
    void insSapCLShopeeApp(List<SapCLShopeeAppModel> list);
    void insSapCLShopeePayCT(List<SapCLShopeePayCTModel> list);
    void insSapCLSmartNet(List<SapCLSmartNetModel> list);
    void insSapCLViettelApp(List<SapCLViettelAppModel> list);
    void insSapCLViettelCounter(List<SapCLViettelCounterModel> list);
    void insSapCLViMo(List<SapCLViMoModel> list);
    void insSapCLVnPayBilling(List<SapCLVnPayBillingModel> list);
    void insSapCLVNPayGW(List<SapCLVNPayGWModel> list);
    void insSapCLVnPost(List<SapCLVnPostModel> list);
    void insSapCLVNPTPay(List<SapCLVNPTPayModel> list);
    void insSapCLZaloPay(List<SapCLZaloPayModel> list);
    void procSyncColl(Map<String,Object> params);
    void procDelColl(Map<String,Object> params);
    List<SapCLCollectorConfigModel> getSapCollectorConfig(String fileName);
}
