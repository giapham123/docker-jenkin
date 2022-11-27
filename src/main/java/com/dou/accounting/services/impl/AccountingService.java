package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.AccountingMapper;
import com.dou.accounting.models.AccountingModel;
import com.dou.accounting.services.AccountingInterface;
import com.dou.adm.configuration.ResourceConfigurations;
import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.shared.CommonStrings;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccountingService implements AccountingInterface {
    @Autowired
    private AccountingMapper _accountingMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public Map getListDataAccounting(AccountingModel accountingModel){
        int isProcTermiFinish = _accountingMapper.getProcTermiFinish();
        int getAllDataInTerminal = _accountingMapper.countAllDataInTerminal(accountingModel);
        Map rs = new HashMap();
        if(isProcTermiFinish != 0) {
            rs.put("data",_accountingMapper.getAccountingData(accountingModel));
            rs.put("totalPages",getAllDataInTerminal);
        }
        else {
            rs = null;
        }
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportAccountingData(AccountingModel accountingModel){
        try {
            List<AccountingModel> result =  _accountingMapper.getAccountingDataForExport(accountingModel);

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"TERMINATION SIMULATION REPORT"});
            data.add(new String[]{"Generated On (Date & Time): " + formatter.format(date)});
            data.add(new String[]{"Product", "Scheme", "Agreement No", "Agreement Id", "Principal O/S",
                    "Pending Installemnts", "Currentmonthint", "CurrLPI", "Over Due Charges", "Force-closure Charge",
                    "Excessamount", "Net Receiable", "Npa Stage", "Dpd", "Last EMI", "Last Duedate"});

            for (int i = 0; i < result.size(); i++) {
                data.add(new String[]{result.get(i).getProduct(), result.get(i).getScheme(),
                        result.get(i).getAgreementNo(), result.get(i).getAgreementId(), result.get(i).getPrincipalOs(),
                        result.get(i).getPendingInst(), result.get(i).getCurrentMonthint(), result.get(i).getCurrLpi(),
                        result.get(i).getOverDuecharges(), result.get(i).getForceClosureCharges(), result.get(i).getExcessAmount(),
                        result.get(i).getNetReceiable(), result.get(i).getNpaStage(), result.get(i).getDpd(),
                        result.get(i).getLastEmi(),result.get(i).getLastDuedt()});
            }
            writer.writeAll(data);

            File file1 = new File(a);
            writer.close();
            byte[] dataBase64 = FileUtils.readFileToByteArray(file1);
            return dataBase64;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static HttpServletRequest getCurrentRequest() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes)
                return ((ServletRequestAttributes) requestAttributes).getRequest();
        } catch (Exception e) {
            // Ignore case
        }
        return null;
    }
}
