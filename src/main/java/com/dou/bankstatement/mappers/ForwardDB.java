package com.dou.bankstatement.mappers;

import com.dou.bankstatement.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ForwardDB {
    @Autowired
    private FTPMapper _ftpMapper;

    public void bankStatement(BankStatementModel model){_ftpMapper.bankStatement(model);}
    public void insSapCLEPay(List<SapCLEPayModel> list){_ftpMapper.insSapCLEPay(list);}
    public void insSapCLMomoApp(List<SapCLMomoAppModel> list){_ftpMapper.insSapCLMomoApp(list);}
    public void insSapCLMomoMF(List<SapCLMomoMFModel> list){_ftpMapper.insSapCLMomoMF(list);}
    public void insSapCLOnepay(List<SapCLOnepayModel> list){_ftpMapper.insSapCLOnepay(list);}
    public void insSapCLPayoo(List<SapCLPayooModel> list){_ftpMapper.insSapCLPayoo(list);}
    public void insSapCLShopeeApp(List<SapCLShopeeAppModel> list){_ftpMapper.insSapCLShopeeApp(list);}
    public void insSapCLShopeePayCT(List<SapCLShopeePayCTModel> list){_ftpMapper.insSapCLShopeePayCT(list);}
    public void insSapCLSmartNet(List<SapCLSmartNetModel> list){_ftpMapper.insSapCLSmartNet(list);}
    public void insSapCLViettelApp(List<SapCLViettelAppModel> list){_ftpMapper.insSapCLViettelApp(list);}
    public void insSapCLViettelCounter(List<SapCLViettelCounterModel> list){_ftpMapper.insSapCLViettelCounter(list);}
    public void insSapCLViMo(List<SapCLViMoModel> list){_ftpMapper.insSapCLViMo(list);}
    public void insSapCLVnPayBilling(List<SapCLVnPayBillingModel> list){_ftpMapper.insSapCLVnPayBilling(list);}
    public void insSapCLVNPayGW(List<SapCLVNPayGWModel> list){_ftpMapper.insSapCLVNPayGW(list);}
    public void insSapCLVnPost(List<SapCLVnPostModel> list){_ftpMapper.insSapCLVnPost(list);}
    public void insSapCLVNPTPay(List<SapCLVNPTPayModel> list){_ftpMapper.insSapCLVNPTPay(list);}
    public void insSapCLZaloPay(List<SapCLZaloPayModel> list){_ftpMapper.insSapCLZaloPay(list);}
    public List<SapCLCollectorConfigModel> getSapCollectorConfig(String fileName){ return _ftpMapper.getSapCollectorConfig(fileName);}


}
