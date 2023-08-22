package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.TerminationReportAppMapper;
import com.dou.accounting.models.TerminationReportAppModel;
import com.dou.accounting.services.TerminationReportAppInterface;
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
public class TerminationReportAppService implements TerminationReportAppInterface {
    @Autowired
    private TerminationReportAppMapper terminationReportAppMapper;

    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public Map getDataTerminationReportApp(TerminationReportAppModel terminationReportAppModel){
        int isProcTermiFinish = terminationReportAppMapper.getProcTermiFinish();
        int getAllDataInTerminal = terminationReportAppMapper.countAllDataInTerminal(terminationReportAppModel);
        Map rs = new HashMap();
        rs.put("data",terminationReportAppMapper.getDataTerminationReportApp(terminationReportAppModel));
        rs.put("totalPages",getAllDataInTerminal);
//        if(isProcTermiFinish != 0) {
//            rs.put("data",_accountingMapper.getAccountingData(accountingModel));
//            rs.put("totalPages",getAllDataInTerminal);
//        }
//        else {
//            rs = null;
//        }
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportTerminationReportAppData(TerminationReportAppModel terminationReportAppModel){
        try {
            List<TerminationReportAppModel> result =  terminationReportAppMapper.exportTerminationReportAppData(terminationReportAppModel);

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"TERMINATION SIMULATION REPORT-APPID"});
            data.add(new String[]{"Generated On (Date & Time): " + formatter.format(date)});
            data.add(new String[]{"Product", "Scheme", "Agreement No", "Agreement Id", "Principal O/S",
                    "Pending Installemnts", "Currentmonthint", "CurrLPI", "Over Due Charges", "Force-closure Charge",
                    "Excessamount", "Net Receiable", "Npa Stage", "Dpd", "Last EMI", "Last Duedate",
            "Waive Off Amount","Advanced interest","Deduct amount","Net Receivable after deduct"});

            for (int i = 0; i < result.size(); i++) {
                data.add(new String[]{result.get(i).getProduct(), result.get(i).getScheme(),
                        result.get(i).getAgreementNo(), result.get(i).getAgreementId(), result.get(i).getPrincipalOs(),
                        result.get(i).getPendingInst(), result.get(i).getCurrentMonthint(), result.get(i).getCurrLpi(),
                        result.get(i).getOverDuecharges(), result.get(i).getForceClosureCharges(), result.get(i).getExcessAmount(),
                        result.get(i).getNetReceiable(), result.get(i).getNpaStage(), result.get(i).getDpd(),
                        result.get(i).getLastEmi(),result.get(i).getLastDuedt(),result.get(i).getWaiveOffAmount(),result.get(i).getAdvancedInter(),
                        result.get(i).getDeductAmount(),result.get(i).getNetReceiAfterDeduct()});
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
