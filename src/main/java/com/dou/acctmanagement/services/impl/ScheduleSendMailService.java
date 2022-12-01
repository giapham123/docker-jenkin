package com.dou.acctmanagement.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Service
public class ScheduleSendMailService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduleSendMailService.class);
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private ReportRequestService reportRequestService;
    private static final String PATTERN_DATE = "MMddyyyy";

    @Value("${acct.sim.process.time-send-mail}")
    private String processSendMailCronTask;

    @Value("${acct.sim.process.receive}")
    private String receiverMail;

    String d_email = "Account_Management_System@mafc.com.vn",
            d_password = "",
            d_host = "mafc-com-vn.mail.eo.outlook.com",
            d_port = "25",
            m_subject = "ACCOUNT MANAGEMENT TOOL – REQUESTS REPORT DAILY",
            m_text = "Hey, From mail ok??.";

    @PostConstruct
    private void onStartUp(){
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        startJob();
    }

    public boolean isRunning() {
        return threadPoolTaskScheduler.getPoolSize() > 0;
    }

    public void stopJob(){
        threadPoolTaskScheduler.shutdown();
    }

    public void startJob(){
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_DATE);
                    String date = simpleDateFormat.format(new Date());
                    send(d_email, d_password, d_host, d_port, receiverMail, m_subject+ "_"+ date, m_text);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                return new CronTrigger(processSendMailCronTask).nextExecutionTime(triggerContext);
            }
        });
    }

    // Those are the values that have the email information
    public void send(String from, String pass, String host, String port, String to, String subject, String text) {

        Properties props = new Properties();

        // Read properties file.

        props.put("mail.smtp.user", from);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "true");

        SecurityManager security = System.getSecurityManager();

        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            msg.setText(text);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            reportRequestService.getRequestAllScheduleEmail();
            // Fill the message
            Multipart multipart = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_DATE);
            String date = simpleDateFormat.format(new Date());
            String fileName = "FileAccountManagerment_"+date;
            String pathFile = "ReportDaily";

            MimeBodyPart attachmentBodyPart= new MimeBodyPart();
            DataSource source = new FileDataSource(pathFile+"/"+ fileName+ ".xls"); // ex : "C:\\test.pdf"
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(fileName+".xls"); // ex : "test.pdf"

            multipart.addBodyPart(textBodyPart);  // add the text part
            multipart.addBodyPart(attachmentBodyPart); // add the attachement part
            msg.setContent(multipart);

            Transport.send(msg);

            //Remove file
            File fileRemove = new File(pathFile+"/"+ fileName+ ".xls");
            if (fileRemove.exists()) {
                fileRemove.delete();
            } else {
                System.err.println(
                        "I cannot find '" + fileRemove + "' ('" + fileRemove.getAbsolutePath() + "')");
            }
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    public class SMTPAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(d_email, d_password);
        }
    }

}
