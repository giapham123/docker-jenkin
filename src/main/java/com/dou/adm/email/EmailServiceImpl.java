package com.dou.adm.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Tu.Tran on 10/1/2018.
 */
@Service
public class EmailServiceImpl implements EmailService {

//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Autowired
//    private Configuration freemarkerConfig;
//
//    @Value("${spring.email.from}")
//    private String emailSender;

    @Async
    public void sendEmail(String emailFile, String recipient, String subject, Map<String, Object> model) throws Exception {

//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        // set loading location to src/main/resources/templates
//        Template template = freemarkerConfig.getTemplate(emailFile);
//        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//        helper.setFrom(emailSender);
//        helper.setTo(recipient);
//        helper.setText(text, true); // set to html
//        helper.setSubject(subject);
//        mailSender.send(message);
    }
}
