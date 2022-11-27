package com.dou.adm.email;


import java.util.Map;

/**
 * Created by Tu.Tran on 10/1/2018.
 */
public interface EmailService {
    public void sendEmail(String emailFile, String recipient, String subject, Map<String, Object> model) throws Exception;
}
