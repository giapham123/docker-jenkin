package com.dou.adm.configuration;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@PropertySource("classpath:application.properties")
public class ResourceConfigurations {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceConfigurations.class);

    @Value("${storage.report.template}")
    private String reportStorage;

    @Value("${storage.document}")
    private String documentStorage;

    @Value("${ftp.server.address.ip}")
    private String ftpServerIp;

    @Value("${ftp.server.address.port}")
    private int ftpServerPort;

    @Value("${ftp.server.auth.username}")
    private String ftpServerUserName;

    @Value("${ftp.server.auth.password}")
    private String ftpServerPassword;

    @Value("${ftp.server.option.retry-times}")
    private int ftpServerRetryTimes;

    @Value("${ftp.server.option.waiting}")
    private int ftpServerWaiting;

//    @Bean("FILE_SERVER_DOCUMENT")
//    public ClientInfo clientInfoBean() {
//        String username = AES.decrypt(this.ftpServerUserName);
//        String password = AES.decrypt(this.ftpServerPassword);

        //return new ClientInfo(this.ftpServerIp, this.ftpServerPort, this.ftpServerUserName, this.ftpServerPassword, this.ftpServerRetryTimes, this.ftpServerWaiting);
//    }

    public String getDocumentStorage() {
        return this.documentStorage;
    }

    public Path getReportStorage(HttpServletRequest request) {
        try {
            String contextPath = request.getSession().getServletContext().getRealPath("/");
            Path path = Paths.get(contextPath, this.reportStorage);
            if (!Files.exists(path)) {
                FileUtils.forceMkdir(path.toFile());
            }

            return path;
        } catch (Exception e) {
            LOGGER.error("Can not prepare storage folder of report template.", e);
        }

        return null;
    }

    public Path getDocumentStorage(HttpServletRequest request) {
        try {
            String contextPath = request.getSession().getServletContext().getRealPath("/");
            Path path = Paths.get(contextPath, this.documentStorage);
            if (!Files.exists(path)) {
                FileUtils.forceMkdir(path.toFile());
            }

            return path;
        } catch (Exception e) {
            LOGGER.error("Can not prepare storage folder of ftp folder.", e);
        }

        return null;
    }
}

