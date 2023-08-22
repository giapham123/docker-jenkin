package com.dou.adm.configuration;

import org.apache.commons.io.FileUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStoreException;

@Configuration
@PropertySource("classpath:application.properties")
public class ResourceConfigurations {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceConfigurations.class);

    @Value("${storage.report.template}")
    private String reportStorage;

    @Value("${storage.document}")
    private String documentStorage;

    @Value("${storage.print-template}")
    private String printTemplateStorage;

    public String getDocumentStorage() {
        return documentStorage;
    }

    public Path getPrintTemplateStorage(HttpServletRequest request) {
        return getPath(this.printTemplateStorage, request);
    }

    public Path getReportStorage(HttpServletRequest request) {
        return getPath(this.reportStorage, request);
    }

    public Path getDocumentStorage(HttpServletRequest request) {
        return getPath(this.documentStorage, request);
    }

    private static Path getPath(String folderName, HttpServletRequest request) {
        try {
            String contextPath = request.getSession().getServletContext().getRealPath("/");
            Path path = Paths.get(contextPath, folderName);
            if (!Files.exists(path)) {
                FileUtils.forceMkdir(path.toFile());
            }

            return path;
        } catch (Exception e) {
            LOGGER.error("Can not prepare storage folder of " + folderName, e);
        }

        return null;
    }

    @Bean
    public RestTemplate getRestTemplate() throws KeyStoreException, Exception {
        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                .setConnectionManager(connectionManager).build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}

