package com.dou.accounting.services.impl;

import com.dou.accounting.mappers.CasRepaymentScheduleMapper;
import com.dou.accounting.models.CasRepaymentScheduleModel;
import com.dou.accounting.services.CasRepaymentScheduleInterface;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CasRepaymentScheduleService implements CasRepaymentScheduleInterface {

    @Autowired
    CasRepaymentScheduleMapper casRepaymentScheduleMapper;


    @Autowired
    private ResourceConfigurations configurations;

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public Map getDataCasRepayment(CasRepaymentScheduleModel casRepaymentScheduleModel) {
        Map rs = getData(casRepaymentScheduleModel);
        return rs;
    }

    @Override
    @TargetDataSource(CommonStrings.DATA_SOURCE_ORCALE)
    public byte[] exportCasRepaymentData(CasRepaymentScheduleModel casRepaymentScheduleModel) {
        try {

            Map rs = getData(casRepaymentScheduleModel);

            List<CasRepaymentScheduleModel> dataCas = (List<CasRepaymentScheduleModel>) rs.get("data");

            Path storage = configurations.getReportStorage(getCurrentRequest());

            String a = storage.resolve("templateData").toString();

            File file = new File(a);

            FileWriter outputfile = new FileWriter(file);

            int rsEarlyPayment = (int) rs.get("rsEarlyPayment");
            int rsTerminalAmount = (int) rs.get("rsTerminalAmount");
            CSVWriter writer = new CSVWriter(outputfile);
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"Cas Repayment Report"});
            data.add(new String[]{"Receivable_till_date", (String) rs.get("receivableTill")
                    ,"Not_Overdue_Principal", (String) rs.get("notOverdue"),"Early payment penalty", String.valueOf(rsEarlyPayment)});
            data.add(new String[]{"Rec_amount", (String) rs.get("recAmount"),"Waive off Covid", (String) rs.get("waiveOffCovid")});
            data.add(new String[]{"Termination Amount", String.valueOf(rsTerminalAmount)});
            data.add(new String[]{"AGREEMENT ID", "INSTLNUM", "DUEDATE", "INSTLAMT", "PRINCOMP",
                    "INTCOMP", "EFFRATE", "BALPRIN", "DAYS"});

            for (int i = 0; i < dataCas.size(); i++) {
                data.add(new String[]{dataCas.get(i).getAgreementId(), dataCas.get(i).getInstlNum(),
                        dataCas.get(i).getDueDt(), dataCas.get(i).getInstlAmt(), dataCas.get(i).getPrinComp(),
                        dataCas.get(i).getIntComp(), dataCas.get(i).getEffRate(), dataCas.get(i).getBalPrin(),
                        dataCas.get(i).getDays()});
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

    public Map getData(CasRepaymentScheduleModel casRepaymentScheduleModel){
        List<CasRepaymentScheduleModel> result = casRepaymentScheduleMapper.getDataCasRepayment(casRepaymentScheduleModel);

        String receivableTill = casRepaymentScheduleMapper.getReceivableTillDate(casRepaymentScheduleModel);
        String notOverdue = casRepaymentScheduleMapper.getNotOverduePrincipal(casRepaymentScheduleModel);
        String earlyPayment = casRepaymentScheduleMapper.getEarlyPaymentPenalty(casRepaymentScheduleModel);
        double rsEarlyPayment = 0;
        if(earlyPayment == null){
            earlyPayment = "0";
        }else {
            rsEarlyPayment =  (Double.parseDouble(earlyPayment) * Double.parseDouble(notOverdue)) / 100;
        }
        int rsEarlyPaymentPar = (int) Math.round(rsEarlyPayment);
        String recAmount = casRepaymentScheduleMapper.getRecAmount(casRepaymentScheduleModel);
        String waiveOffCovid = casRepaymentScheduleMapper.getWaiveOffCovid(casRepaymentScheduleModel);
        double rsTerminalAmount = Double.parseDouble(receivableTill) + Double.parseDouble(notOverdue) +
                rsEarlyPayment - Double.parseDouble(recAmount);
        Map rs = new HashMap();
        rs.put("data",result);
        rs.put("receivableTill",receivableTill);
        rs.put("notOverdue",notOverdue);
        rs.put("rsEarlyPayment",rsEarlyPaymentPar);
        rs.put("recAmount",recAmount);
        rs.put("waiveOffCovid",waiveOffCovid);
        rs.put("rsTerminalAmount",(int) Math.round(rsTerminalAmount));

        return rs;
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
